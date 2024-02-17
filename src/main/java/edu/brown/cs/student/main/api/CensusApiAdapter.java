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

/**
 * This class adapts the Census API to fetch state and county codes, as well as broadband data. It
 * implements the IDataFetcher interface to provide methods for fetching codes and data.
 */
public class CensusApiAdapter implements IDataFetcher {
  private final Moshi moshi;
  private Map<String, String> stateCodes = new HashMap<>();
  private final JsonAdapter<List<List<String>>> jsonAdapter;
  private ACSDataCache acsDataCache;

  /**
   * Constructs a CensusApiAdapter object with the specified ACSDataCache.
   *
   * @param cache The cache to store fetched data.
   */
  public CensusApiAdapter(ACSDataCache cache) {
    this.moshi = new Moshi.Builder().build();
    Type listOfListsOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
    this.jsonAdapter = moshi.adapter(listOfListsOfStringType);
    this.acsDataCache = cache;
    fetchAndCacheStateCodes();
  }
  /**
   * Fetches and caches the state codes from the Census API.
   *
   * @return A list of lists containing state codes and names.
   */
  public List<List<String>> fetchAndCacheStateCodes() {
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
          stateCodes.put(entry.get(0), entry.get(1)); // Maybe delete
          acsDataCache.put("stateCode:" + entry.get(0), entry.get(1)); // Cache the state codes
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  /**
   * Retrieves the state code corresponding to the given state name.
   *
   * @param stateName The name of the state.
   * @return The state code.
   */
  public String getStateCode(String stateName) {
    String cachedCode = (String) acsDataCache.getIfPresent("stateCode:" + stateName);
    if (cachedCode != null) {
      return cachedCode;
    }
    // If not in cache, return directly from the local map/handle the miss
    String code = stateCodes.get(stateName);
    if (code == null) {
      System.err.println("State code not found for state name: " + stateName);
    }
    return code;
  }
  /**
   * Retrieves the county code corresponding to the given state and county names.
   *
   * @param stateName The name of the state.
   * @param countyName The name of the county.
   * @return The county code.
   * @throws IOException if an I/O error occurs.
   */
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
    try {
      URL url =
          new URL(
              "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                  + stateCode);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new IOException(
            "Failed to fetch county codes: HTTP error code: " + conn.getResponseCode());
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder json = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }
      reader.close();
      List<List<String>> counties = jsonAdapter.fromJson(json.toString());
      if (counties == null || counties.isEmpty()) {
        throw new IOException("No county data found for state code: " + stateCode);
      }
      for (List<String> county : counties.subList(1, counties.size())) {
        if (county.get(0).contains(countyName)) {
          acsDataCache.put(cacheKey, county.get(2));
          return county.get(2);
        }
      }
      throw new IOException("County code not found for county name: " + countyName);
    } catch (IOException e) {
      System.err.println("IO Exception occurred while fetching county codes: " + e.getMessage());
      throw e; // Re-throw the exception to indicate failure to the caller
    }
  }

  /**
   * Getter Methods Below For State, Country, and Data Code.
   *
   * @param stateName
   * @return
   * @throws IOException
   */
  @Override
  public String fetchStateCode(String stateName) throws IOException {
    return this.getStateCode(stateName);
  }

  @Override
  public String fetchCountyCode(String stateName, String countyName) throws IOException {
    return this.getCountyCode(stateName, countyName);
  }

  public List<List<String>> fetchBroadbandData(String state, String county) throws IOException {
    String cacheKey = "broadbandData:" + state + ":" + county;
    List<List<String>> cachedData = (List<List<String>>) acsDataCache.getIfPresent(cacheKey);
    if (cachedData != null) {
      return cachedData;
    }

    String urlString = constructApiUrl(state, county);
    try {
      String jsonResponse = makeApiRequest(urlString);
      List<List<String>> data = jsonAdapter.fromJson(jsonResponse);
      if (data == null || data.isEmpty()) {
        throw new IOException(
            "No broadband data found for state: " + state + ", county: " + county);
      }
      acsDataCache.put(cacheKey, data);
      return data;
    } catch (IOException e) {
      System.err.println("IO Exception occurred while fetching broadband data: " + e.getMessage());
      throw e;
    }
  }

  /**
   * A helper method to construct the api request for the census api
   *
   * @param state
   * @param countyCodeWildcard
   * @return
   */
  private String constructApiUrl(String state, String countyCodeWildcard) {
    String baseApiUrl = "https://api.census.gov/data/2021/acs/acs1/subject";
    String variables = "get=NAME,S2802_C03_022E";
    String forCounty = "for=county:" + (countyCodeWildcard.equals("*") ? "*" : countyCodeWildcard);
    String inState = "in=state:" + state;
    return String.format("%s?%s&%s&%s", baseApiUrl, variables, forCounty, inState);
  }

  /**
   * This is the actual logic for how our server access data from the Census API given the user's
   * query
   *
   * @param urlString
   * @return
   * @throws IOException
   */
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
