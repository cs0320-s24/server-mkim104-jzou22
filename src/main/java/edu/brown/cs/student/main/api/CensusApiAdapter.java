package edu.brown.cs.student.main.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.cache.ACSDataCache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensusApiAdapter {
  private final Moshi moshi;
  private Map<String, String> stateCodes = new HashMap<>();
  private final JsonAdapter<List<List<String>>> jsonAdapter;
  private ACSDataCache acsDataCache;

  public CensusApiAdapter(ACSDataCache cache) {
    this.moshi = new Moshi.Builder().build();
    Type listOfListsOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
    this.jsonAdapter = moshi.adapter(listOfListsOfStringType);
    this.acsDataCache = cache;
    fetchAndCacheStateCodes();
  }

  private void fetchAndCacheStateCodes() {
    try {
      URL url = new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder json = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }
      reader.close();
      List<List<String>> data = jsonAdapter.fromJson(json.toString());
      if (data != null) {
        for (List<String> entry : data.subList(1, data.size())) {
          stateCodes.put(entry.get(0), entry.get(1));
          acsDataCache.put("stateCode:" + entry.get(0), entry.get(1)); // Cache the state codes
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getStateCode(String stateName) {
    String cachedCode = (String) acsDataCache.getIfPresent("stateCode:" + stateName);
    if (cachedCode != null) {
      return cachedCode;
    }
    // If not in cache, return directly from the local map/handle the miss
    return stateCodes.get(stateName);
  }

  public String getCountyCode(String stateName, String countyName) throws IOException {
    String cacheKey = "countyCode:" + stateName + ":" + countyName;
    String cachedCode = (String) acsDataCache.getIfPresent(cacheKey);
    if (cachedCode != null) {
      return cachedCode;
    }
    String stateCode = getStateCode(stateName);
    if (stateCode == null) {
      throw new IOException("State code not found for state name: " + stateName);
    }
    URL url =
        new URL(
            "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder json = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      json.append(line);
    }
    reader.close();
    List<List<String>> counties = jsonAdapter.fromJson(json.toString());
    if (counties != null) {
      for (List<String> county : counties.subList(1, counties.size())) {
        if (county.get(0).contains(countyName)) {
          acsDataCache.put(cacheKey, county.get(2)); // Cache the found county code
          return county.get(2);
        }
      }
    }
    return null; // County code not found
  }

  public List<List<String>> fetchBroadbandData(String state, String county) throws IOException {
    String cacheKey = "broadbandData:" + state + ":" + county;
    List<List<String>> cachedData = (List<List<String>>) acsDataCache.getIfPresent(cacheKey);
    if (cachedData != null) {
      return cachedData;
    }

    String urlString = constructApiUrl(state, county);
    String jsonResponse = makeApiRequest(urlString);
    List<List<String>> data = jsonAdapter.fromJson(jsonResponse);
    if (data != null && !data.isEmpty()) {
      acsDataCache.put(cacheKey, data); // Cache the broadband data
    }
    return data;
  }

  private String constructApiUrl(String state, String countyCodeWildcard) {
    String baseApiUrl = "https://api.census.gov/data/2021/acs/acs1/subject";
    String variables = "get=NAME,S2802_C03_022E";
    String forCounty = "for=county:" + (countyCodeWildcard.equals("*") ? "*" : countyCodeWildcard);
    String inState = "in=state:" + state;
    return String.format("%s?%s&%s&%s", baseApiUrl, variables, forCounty, inState);
  }

  private String makeApiRequest(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    StringBuilder response = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    } finally {
      connection.disconnect();
    }
    return response.toString();
  }
}
