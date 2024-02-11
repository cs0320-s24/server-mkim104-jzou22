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
import java.util.List;

public class CensusApiAdapter {
  private final Moshi moshi;
  private final JsonAdapter<List<CensusDataResult>> jsonAdapter;

  public CensusApiAdapter() {
    this.moshi = new Moshi.Builder().build();
    Type listOfCensusDataResultsType =
        Types.newParameterizedType(List.class, CensusDataResult.class);
    this.jsonAdapter = moshi.adapter(listOfCensusDataResultsType);
  }

  public String fetchBroadbandData(String state, String county) throws IOException {
    String urlString = constructApiUrl(state, county);
    return makeApiRequest(urlString); // Just return the raw JSON string
  }

  private String constructApiUrl(String state, String countyCodeWildcard) {
    String baseApiUrl = "https://api.census.gov/data/2021/acs/acs1/subject";
    String variables = "get=NAME,S2802_C03_022E"; // Variables you're interested in
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
