package edu.brown.cs.student.main.api;

import edu.brown.cs.student.main.server.RequestHandler;
import spark.Request;
import spark.Response;

public class BroadbandDataHandler implements RequestHandler {
  private CensusApiAdapter censusApiAdapter;

  public BroadbandDataHandler(CensusApiAdapter censusApiAdapter) {
    this.censusApiAdapter = censusApiAdapter;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    if (state == null || county == null) {
      response.status(400); // BAD REQUEST
      return "State and county parameters are required.";
    }

    try {
      String jsonResponse = censusApiAdapter.fetchBroadbandData(state, county);
      response.type("application/json");
      return jsonResponse;
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500);
      return "Failed to retrieve data: " + e.getMessage();
    }
  }
}
