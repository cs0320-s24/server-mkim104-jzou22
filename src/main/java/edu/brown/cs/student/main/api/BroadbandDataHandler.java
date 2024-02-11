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

public class BroadbandDataHandler implements RequestHandler {
  private CensusApiAdapter censusApiAdapter;
  private final Moshi moshi;
  private final JsonAdapter<Map<String, Object>> jsonAdapter;

  public BroadbandDataHandler(CensusApiAdapter censusApiAdapter) {
    this.censusApiAdapter = censusApiAdapter;
    this.moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.jsonAdapter = moshi.adapter(type);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String stateCode = request.queryParams("state");
    String countyCode = request.queryParams("county");
    if (stateCode == null || countyCode == null) {
      response.status(400); // BAD REQUEST
      return "State and county parameters are required.";
    }

    try {
      List<List<String>> data = censusApiAdapter.fetchBroadbandData(stateCode, countyCode);
      if (data == null || data.size() < 2) { // No data found
        response.status(204); // NO CONTENT
        return "";
      }

      String fullName = data.get(1).get(0); // "County, State"
      String broadbandAccessPercentage = data.get(1).get(1);

      // Splitting fullName to extract county and state names separately
      String[] nameParts = fullName.split(", ");
      String countyName = nameParts[0];
      String stateName = nameParts.length > 1 ? nameParts[1] : "";

      Map<String, Object> enrichedResponse = new HashMap<>();
      enrichedResponse.put("stateName", stateName); // Use stateName
      enrichedResponse.put("countyName", countyName); // Use countyName
      enrichedResponse.put("broadbandAccessPercentage", broadbandAccessPercentage);

      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      enrichedResponse.put("retrievalDateTime", dtf.format(now));

      response.type("application/json");
      return jsonAdapter.toJson(enrichedResponse);
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500);
      return "Failed to retrieve data: " + e.getMessage();
    }
  }
}
