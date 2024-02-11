package edu.brown.cs.student.main.api;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import spark.Request;
import spark.Response;
import edu.brown.cs.student.main.server.RequestHandler;

public class BroadbandDataHandler implements RequestHandler {
    private CensusApiAdapter censusApiAdapter;
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<CensusDataResult> jsonAdapter = moshi.adapter(CensusDataResult.class);

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
            CensusDataResult result = censusApiAdapter.fetchBroadbandData(state, county);
            response.status(200); // OK
            response.type("application/json");
            // Serialize
            return jsonAdapter.toJson(result);
        } catch (Exception e) {
            response.status(500); // INTERNAL SERVER ERROR
            return "Failed to retrieve data: " + e.getMessage();
        }
    }
}
