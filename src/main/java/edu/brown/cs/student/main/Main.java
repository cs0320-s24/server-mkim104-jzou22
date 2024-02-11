package edu.brown.cs.student.main;

import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.server.RequestHandler;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;
import spark.Request;
import spark.Response;

public class Main {
  public static void main(String[] args) {
    CensusApiAdapter adapter = new CensusApiAdapter();

    // Initialize server
    Server server = new SparkServer();
    server.start();

    // Registering the broadband endpoint
    server.registerHandler(
        "/broadband",
        new RequestHandler() {
          @Override
          public Object handle(Request request, Response response) {
            String state = request.queryParams("state");
            String county = request.queryParams("county");
            if (state == null || county == null) {
              response.status(400); // BAD REQUEST
              return "State and county parameters are required.";
            }

            try {
              // Directly fetch and return the raw JSON string from the API
              String jsonResponse = adapter.fetchBroadbandData(state, county);

              System.out.println(
                  "JSON Response: "
                      + jsonResponse); // Logging the raw JSON response for verification

              response.type("application/json");
              return jsonResponse; // Directly return the raw JSON string
            } catch (Exception e) {
              e.printStackTrace(); // TO REMOVE: TESTING
              response.status(500); // INTERNAL SERVER ERROR
              return "Failed to retrieve data: " + e.getMessage();
            }
          }
        });
    System.out.println("Server is running on port 3232");
  }
}
