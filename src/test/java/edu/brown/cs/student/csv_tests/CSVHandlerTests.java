// package edu.brown.cs.student.csv_tests;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import edu.brown.cs.student.main.csv.handler.LoadCSVHandler;
// import edu.brown.cs.student.main.csv.handler.SearchCSVHandler;
// import edu.brown.cs.student.main.csv.handler.ViewCSVHandler;
// import edu.brown.cs.student.main.server.SparkServer;
// import java.io.IOException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.nio.charset.StandardCharsets;
// import org.junit.jupiter.api.*;
// import spark.utils.IOUtils;
//
// public class CSVHandlerTests {
//  private static LoadCSVHandler loadCSVHandler;
//  private static SearchCSVHandler searchCSVHandler;
//  private static ViewCSVHandler viewCSVHandler;
//  private static SparkServer server;
//
//  @BeforeAll
//  public static void setupBeforeAll() {
//    server = new SparkServer();
//    server.start();
//  }
//
//  @BeforeEach
//  public void setupBeforeEach() {
//    loadCSVHandler = new LoadCSVHandler();
//    searchCSVHandler = new SearchCSVHandler(loadCSVHandler);
//    viewCSVHandler = new ViewCSVHandler(loadCSVHandler);
//    server.registerHandler("/loadcsv", loadCSVHandler);
//    server.registerHandler("/searchcsv", searchCSVHandler);
//    server.registerHandler("/viewcsv", viewCSVHandler);
//  }
//
//  @AfterAll
//  public static void tearDownAfterEach() {
//    server.stop();
//  }
//
//  @Test
//  public void testViewAndSearchBeforeLoading() throws IOException {
//    URL url =
//        new URL(
//
// "http://localhost:3232/searchcsv?searchValue=Cranston&header=true&columnIdentifier=City/Townd&columnType=");
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//    connection.setRequestMethod("GET");
//
//    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
//
//    URL url1 = new URL("http://localhost:3232/viewcsv");
//    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
//    connection1.setRequestMethod("GET");
//
//    assertEquals(HttpURLConnection.HTTP_OK, connection1.getResponseCode());
//  }
//
//  @Test
//  public void testLoadCSVHandler() throws Exception {
//    // Make an HTTP request to your server to simulate loading a CSV file
//    URL url = new URL("http://localhost:3232/loadcsv?filepath=data/ri_income.csv");
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//    // Assert that the response code is a OK (200)
//    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
//    assertEquals(200, connection.getResponseCode());
//
//    // Read the response body if needed
//    String responseBody = IOUtils.toString(connection.getInputStream());
//    assertTrue(responseBody.contains("CSV Loaded With Status: true"));
//  }
//
//  @Test
//  public void testLoadCSVHandler_FileNotExists() throws Exception {
//    // Make an HTTP request to your server to simulate loading a CSV file
//    URL url = new URL("http://localhost:3232/loadcsv?filepath=data/non_existing_file.csv");
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//    connection.setRequestMethod("GET");
//
//    // Assert that the response code is OK (200)
//    assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
//    assertEquals(400, connection.getResponseCode());
//  }
//
//  @Test
//  public void testSearchLoadedFile() throws Exception {
//    // Load the CSV first
//    URL loadUrl = new URL("http://localhost:3232/loadcsv?filepath=data/ri_income.csv");
//    HttpURLConnection loadConnection = (HttpURLConnection) loadUrl.openConnection();
//    loadConnection.setRequestMethod("GET");
//    assertEquals(HttpURLConnection.HTTP_OK, loadConnection.getResponseCode());
//    URL searchUrl =
//        new URL(
//
// "http://localhost:3232/searchcsv?searchValue=Rhode%20Island&header=true&columnIdentifier=City/Town&columnType=");
//    HttpURLConnection searchConnection = (HttpURLConnection) searchUrl.openConnection();
//    searchConnection.setRequestMethod("GET");
//    assertEquals(HttpURLConnection.HTTP_OK, searchConnection.getResponseCode());
//    String responseBody =
//        new String(searchConnection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//    System.out.println(responseBody);
//    assertTrue(responseBody.contains("[[Rhode Island, 74,489.00, 95,198.00, 39,603.00]]"));
//  }
//
//  @Test
//  public void testSearchWrongValue() throws Exception {
//    // Make an HTTP request to your server to simulate  a CSV file
//    URL url =
//        new URL(
//
// "http://localhost:3232/searchcsv?searchValue=Rhode%20Island&header=true&columnIdentifier=City/Townd&columnType=");
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//    connection.setRequestMethod("GET");
//
//    // Assert that the response code is Bad Request (400)
//    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
//    assertEquals(200, connection.getResponseCode());
//  }
// }
