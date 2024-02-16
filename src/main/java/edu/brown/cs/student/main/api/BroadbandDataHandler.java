package edu.brown.cs.student.main.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.RequestHandler;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
/**
 * This class handles HTTP requests related to broadband data retrieval.
 * It implements the RequestHandler interface to process incoming requests.
 */
public class BroadbandDataHandler implements RequestHandler {
  private CensusApiAdapter censusApiAdapter;
  private final Moshi moshi;
  private final JsonAdapter<Map<String, Object>> jsonAdapter;

  /**
   * Constructs a BroadbandDataHandler object with the provided CensusApiAdapter.
   *
   * @param censusApiAdapter The adapter for accessing Census API.
   */
  public BroadbandDataHandler(CensusApiAdapter censusApiAdapter) {
    this.censusApiAdapter = censusApiAdapter;
    this.moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.jsonAdapter = moshi.adapter(type);
  }

  /**
   * Handles the HTTP request, retrieves broadband data based on state and county,
   * and returns a JSON response.
   *
   * @param request  The HTTP request object.
   * @param response The HTTP response object.
   * @return JSON representation of broadband data or error message.
   * @throws Exception if an error occurs during data retrieval or processing.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // access query params
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    if (stateName == null || countyName == null) {
      response.status(400); // BAD REQUEST
      return "State and county parameters are required.";
    }
    // error checking the params
    try {
      String stateCode = censusApiAdapter.getStateCode(stateName);
      if (stateCode == null) {
        response.status(400);
        return "Invalid state name provided.";
      }

      String countyCode = censusApiAdapter.getCountyCode(stateName, countyName);
      if (countyCode == null) {
        response.status(400);
        return "Invalid county name provided for the given state:";
      }

      List<List<String>> broadbandData = censusApiAdapter.fetchBroadbandData(stateCode, countyCode);
      if (broadbandData == null || broadbandData.isEmpty() || broadbandData.size() < 2) {
        response.status(204); // NO CONTENT
        return "";
      }
      String broadbandAccessPercentage = broadbandData.get(1).get(1);
      // store the responses from what was returned from the census api adapter
      Map<String, Object> responseData = new HashMap<>();
      responseData.put("state", stateName);
      responseData.put("county", countyName);
      responseData.put("broadbandAccessPercentage", broadbandAccessPercentage);

      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      responseData.put("retrievalDateTime", dtf.format(now));

      response.type("application/json");
      return jsonAdapter.toJson(responseData);
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500);
      return "Failed to retrieve data: " + e.getMessage();
    }
  }
}
