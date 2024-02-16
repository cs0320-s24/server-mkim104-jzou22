package edu.brown.cs.student.api_tests;

// PASS TEST 3/5; but ALL PASS SEPARATELY GIVEN THAT THE SERVER IS RUNNING
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.api_tests.mocks.PercentageCensusData;
import edu.brown.cs.student.main.cache.ACSDataCache;
import edu.brown.cs.student.main.server.SparkServer;
import okio.Buffer;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BroadBandAPIHandlerTest {
    private static SparkServer server;
    private final Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    private JsonAdapter<Map<String, Object>> adapter;
    private JsonAdapter<PercentageCensusData> censusDataJsonAdapter;

    @BeforeAll
    public static void setupBeforeAll() {
        server = new SparkServer();
        server.start();
    }

    @AfterAll
    public static void tearDownAfterAll() {
        server.stop();
    }

    @BeforeEach
    public void setupBeforeEach() {
        ACSDataCache acsDataCache = new ACSDataCache(100, 30, TimeUnit.MINUTES);
        CensusApiAdapter censusApiAdapter = new CensusApiAdapter(acsDataCache);
        server.registerHandler("/broadband", new BroadbandDataHandler(censusApiAdapter));

        Moshi moshi = new Moshi.Builder().build();
        this.adapter = moshi.adapter(this.mapStringObject);
        censusDataJsonAdapter = moshi.adapter(PercentageCensusData.class);
    }

    @Test
    public void testConnectionToBroadbandAPI() {
        try {
            String state = "state=California";
            String county = "county=Kings";
            URL url = new URL("http://localhost:3232/broadband?" + state + "&" + county);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Check response code
            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode);

            // Read and parse response body
            Map<String, Object> responseBody = adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));

            // Test the output of these data
            String expectedJson = censusDataJsonAdapter.toJson(new PercentageCensusData(83.5));
            String actualJson = "{\"data\":" + responseBody.get("broadbandAccessPercentage") + "}";
            assertEquals(expectedJson, actualJson);

            connection.disconnect();
        } catch (IOException e) {
            fail("Connection to Broadband API failed: " + e.getMessage());
        }
    }

    @Test
    public void testErrorConnectionToBroadbandAPI() {
        try {
            URL url = new URL("http://localhost:3232/broadband?");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Check response code
            int responseCode = connection.getResponseCode();
            assertEquals(400, responseCode);

            connection.disconnect();
        } catch (IOException e) {
            fail("Connection to Broadband API failed: " + e.getMessage());
        }
    }

    @Test
    public void testBadParamsToBroadbandAPI() {
        try {
            String state = "state=Disney";
            String county = "county=Compton";
            URL url = new URL("http://localhost:3232/broadband?" + state + "&" + county);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.disconnect(); // close gracefully
        } catch (IOException e) {
            fail("Connection to Broadband API failed: " + e.getMessage());
        }
    }

    @Test
    public void testMissingStateParamToBroadbandAPI() {
        try {
            String county = "county=Kings";
            URL url = new URL("http://localhost:3232/broadband?" + county);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println(connection.getResponseCode());
            assertEquals(400, connection.getResponseCode());

            connection.disconnect(); // close gracefully
        } catch (IOException e) {
            fail("Connection to Broadband API failed: " + e.getMessage());
        }
    }
    @Test
    public void testInternalServerErrorToBroadbandAPI() {
        try {
            // Simulate an internal server error on the API side
            server.stop(); // Stop the server to force an internal server error
            URL url = new URL("http://localhost:3232/broadband");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Check response code
            int responseCode = connection.getResponseCode();
            assertEquals(400, responseCode);

            // Restart the server
            server.start();

            connection.disconnect();
        } catch (IOException e) {
            fail("Connection to Broadband API failed: " + e.getMessage());
        }
    }
}
