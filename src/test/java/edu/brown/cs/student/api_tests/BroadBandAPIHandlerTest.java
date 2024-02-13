package edu.brown.cs.student.api_tests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.api_tests.mocks.MockedACSPercentageCensusAPISource;
import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.api.PercentageCensusData;
import edu.brown.cs.student.main.api.PercentageCensusDatasource;
import edu.brown.cs.student.main.cache.ACSDataCache;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BroadBandAPIHandlerTest {
    @BeforeAll
    public static void setup_before_everything() {
        Spark.port(3232);
        Logger.getLogger("").setLevel(Level.WARNING);
    }

    private final Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    private JsonAdapter<Map<String, Object>> adapter;
    private JsonAdapter<PercentageCensusData> censusDataJsonAdapter;
    @BeforeEach
    public void setup() {
        PercentageCensusDatasource mockedSource = new MockedACSPercentageCensusAPISource(new PercentageCensusData(83.5));
        ACSDataCache acsDataCache = new ACSDataCache(100, 30, TimeUnit.MINUTES);
        CensusApiAdapter censusApiAdapter = new CensusApiAdapter(acsDataCache);

        // TAKES IN ROUTE BUT PASSED IN A REQUESTED HANDLER, MIGHT HAVE TO USE SERVICE INSTEAD OF SPARK
        Spark.get("/broadband", (Route) new BroadbandDataHandler(censusApiAdapter));

        // new moshi for responses
        Moshi moshi = new Moshi.Builder().build();
        this.adapter = moshi.adapter(this.mapStringObject);
        censusDataJsonAdapter = moshi.adapter(PercentageCensusData.class);

    }

    @AfterEach
    public void tearDown() {
        Spark.unmap("/broadband");
        Spark.awaitStop();
    }

    /**
     * Helper to start a connection to a specific API endpoint/param
     */
    private HttpURLConnection tryRequest(String apiCall) throws IOException {
        URL requestURL = new URL("http://localhost" + Spark.port() + "/" + apiCall);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        return connection;
    }
    //
    final String state1 = "state=California";
    final String county1 = "county=Kinds";
    @Test
    public void testCensusDataRequestSuccess() throws IOException {
        HttpURLConnection loadConnection = tryRequest("broadband?" + state1 + county1);
        assertEquals(200, loadConnection.getResponseCode());

        Map<String, Object> responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
        assertEquals("success", responseBody.get("type"));

        assertEquals(censusDataJsonAdapter.toJson(new PercentageCensusData(83.5)),
                responseBody.get("broadbandAccessPercentage"));
        loadConnection.disconnect();
    }
}

