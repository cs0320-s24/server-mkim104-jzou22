package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.api.CensusDataResult;
import edu.brown.cs.student.main.server.RequestHandler;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;

public class Main {
  public static void main(String[] args) {
    // Initialize Moshi
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<CensusDataResult> jsonAdapter = moshi.adapter(CensusDataResult.class);

    // Create the API adapter
    CensusApiAdapter adapter = new CensusApiAdapter();

    // Initialize the server with your implementation
    Server server = new SparkServer();
    server.start();

    // Register the broadband endpoint handler
    server.registerHandler(
        "/broadband",
        (RequestHandler)
            (req, res) -> {
              String state = req.queryParams("state");
              String county = req.queryParams("county");
              if (state == null || county == null) {
                res.status(400);
                return "State and county parameters are required.";
              }

              try {
                CensusDataResult result = adapter.fetchBroadbandData(state, county);
                res.type("application/json");
                // Serialize the result to JSON
                return jsonAdapter.toJson(result);
              } catch (Exception e) {
                res.status(500);
                return "Failed to retrieve data: " + e.getMessage();
              }
            });

    System.out.println("Server is running on port 3232");
  }
}
