// package edu.brown.cs.student.api_tests;
//
//// PASS TEST 3/5; but ALL PASS SEPARATELY GIVEN THAT THE SERVER IS RUNNING
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;
//
// import com.squareup.moshi.JsonAdapter;
// import com.squareup.moshi.Moshi;
// import com.squareup.moshi.Types;
// import edu.brown.cs.student.api_tests.mocks.PercentageCensusData;
// import edu.brown.cs.student.main.api.BroadbandDataHandler;
// import edu.brown.cs.student.main.api.CensusApiAdapter;
// import edu.brown.cs.student.main.cache.ACSDataCache;
// import edu.brown.cs.student.main.server.SparkServer;
// import java.io.IOException;
// import java.lang.reflect.Type;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.Map;
// import java.util.concurrent.TimeUnit;
// import okio.Buffer;
// import org.junit.jupiter.api.*;
//
// public class BroadBandAPIHandlerTest {
//    private static SparkServer server;
//    private final Type mapStringObject =
//            Types.newParameterizedType(Map.class, String.class, Object.class);
//    private JsonAdapter<Map<String, Object>> adapter;
//    private JsonAdapter<PercentageCensusData> censusDataJsonAdapter;
//
//    @BeforeAll
//    public static void setupBeforeAll() throws InterruptedException {
//        server = new SparkServer();
//        server.start();
//    }
//
//    @BeforeEach
//    public void setupBeforeEach() {
//        ACSDataCache acsDataCache = new ACSDataCache(100, 30, TimeUnit.MINUTES);
//        CensusApiAdapter censusApiAdapter = new CensusApiAdapter(acsDataCache);
//        server.registerHandler("/broadband", new BroadbandDataHandler(censusApiAdapter));
//        Moshi moshi = new Moshi.Builder().build();
//        this.adapter = moshi.adapter(this.mapStringObject);
//        censusDataJsonAdapter = moshi.adapter(PercentageCensusData.class);
//    }
//
//    @AfterAll
//    public static void tearDownAfterAll() {
//        server.stop();
//    }
//
//    @Test
//    public void testBadParamsToBroadbandAPI() {
//        try {
//            String state = "state=Disney";
//            String county = "county=Compton";
//            URL url = new URL("http://localhost:3232/broadband?" + state + "&" + county);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.disconnect();
//        } catch (IOException e) {
//            fail("Connection to Broadband API failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testMissingStateParamToBroadbandAPI() {
//        try {
//            String county = "county=Kings";
//            URL url = new URL("http://localhost:3232/broadband?" + county);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            System.out.println(connection.getResponseCode());
//            assertEquals(400, connection.getResponseCode());
//
//            connection.disconnect();
//        } catch (IOException e) {
//            fail("Connection to Broadband API failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testInternalServerErrorToBroadbandAPI() {
//        try {
//            // Simulate an internal server error on the API side
//            server.stop(); // Stop the server to force an internal server error
//            URL url = new URL("http://localhost:3232/broadband");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // Check response code
//            int responseCode = connection.getResponseCode();
//            assertEquals(400, responseCode);
//
//            // Restart the server
//            server.start();
//            connection.disconnect();
//            connection.disconnect();
//        } catch (IOException e) {
//            fail("Connection to Broadband API failed: " + e.getMessage());
//        }
//    }
// }
//
