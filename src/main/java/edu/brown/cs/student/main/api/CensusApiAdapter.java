package edu.brown.cs.student.main.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
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

  public CensusApiAdapter() {
    this.moshi = new Moshi.Builder().build();
    Type listOfListsOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
    this.jsonAdapter = moshi.adapter(listOfListsOfStringType);
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
      System.out.println("Fetching state codes...");
      if (data != null) {
        for (List<String> entry : data.subList(1, data.size())) {
          System.out.println("Caching state code for: " + entry.get(0) + " as " + entry.get(1));
          stateCodes.put(entry.get(0), entry.get(1));
        }
      }
    } catch (Exception e) {
      System.out.println("Error fetching state codes: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public String getStateCode(String stateName) {
    String code = stateCodes.get(stateName);
    System.out.println("Resolved state code for " + stateName + ": " + code);
    return code;
  }

  public String getCountyCode(String stateName, String countyName) throws IOException {
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
    System.out.println("Fetching county code for " + countyName + " in " + stateName);
    if (counties != null) {
      for (List<String> county : counties.subList(1, counties.size())) {
        System.out.println("Checking county: " + county.get(0));
        if (county.get(0).contains(countyName)) {
          System.out.println("Found code for " + countyName + ": " + county.get(2));
          return county.get(2);
        }
      }
    }
    System.out.println("County code not found for " + countyName);
    return null;
  }

  public List<List<String>> fetchBroadbandData(String state, String county) throws IOException {
    String urlString = constructApiUrl(state, county);
    String jsonResponse = makeApiRequest(urlString); // Fetch raw JSON string
    List<List<String>> data = jsonAdapter.fromJson(jsonResponse);
    return data; // Return the parsed data
  }

  private String constructApiUrl(String state, String countyCodeWildcard) {
    String baseApiUrl = "https://api.census.gov/data/2021/acs/acs1/subject";
    String variables = "get=NAME,S2802_C03_022E";
    String forCounty = "for=county:" + (countyCodeWildcard.equals("*") ? "*" : countyCodeWildcard);
    String inState = "in=state:" + state; // State code

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
    System.out.println(response.toString());
    return response.toString();
  }
}
