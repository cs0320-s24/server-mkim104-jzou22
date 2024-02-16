package edu.brown.cs.student.api_tests;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.api_tests.mocks.MockDataFetcher;
import edu.brown.cs.student.main.cache.ACSDataCache;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the CensusApiAdapter class.
 */
class CensusApiAdapterTests {

  private MockDataFetcher mockDataFetcher;
  private CensusApiAdapter adapter;

  @BeforeEach
  void setUp() {
    // Initialize MockDataFetcher with mock data
    mockDataFetcher = new MockDataFetcher();
    ACSDataCache cache = new ACSDataCache(100, 30, TimeUnit.MINUTES);
    adapter = new CensusApiAdapter(cache); // Use real adapter with cache for integration tests
  }

  @Test
  void testGetStateCode() {
    // Assuming getStateCode does not throw IOException
    String stateCode = adapter.getStateCode("California");
    assertEquals("06", stateCode, "The state code for California should be CA.");

    // Testing with a non-existent state to check error handling
    String invalidStateCode = adapter.getStateCode("InvalidState");
    assertNull(invalidStateCode, "The state code for a non-existent state should be null.");
  }

  @Test
  void testGetCountyCode() throws IOException {
    String countyCode = mockDataFetcher.fetchCountyCode("California", "Kings County");
    assertEquals("06", countyCode);
  }

  @Test
  void getCountyCode_RealRequest() {
    String stateName = "Rhode Island";
    String countyName = "Providence";
    String expectedCountyCode = "007";

    try {
      String countyCode = adapter.getCountyCode(stateName, countyName);
      assertNotNull(countyCode, "County code should not be null");
      assertEquals(
          expectedCountyCode,
          countyCode,
          "The fetched county code does not match the expected value.");
    } catch (IOException e) {
      fail("IOException should not have been thrown for a valid request");
    }
  }

  @Test
  void fetchBroadbandData_RealRequest() throws IOException {
    String state = "34";
    String county = "003";

    List<List<String>> broadbandData = adapter.fetchBroadbandData(state, county);

    assertNotNull(broadbandData, "Broadband data should not be null");
    assertFalse(broadbandData.isEmpty(), "Broadband data should not be empty");
  }

  @Test
  void getStateCode_KnownState_ReturnsExpectedCode() {
    String stateName = "New Jersey";
    String expectedStateCode = "34";

    String stateCode = adapter.getStateCode(stateName);

    assertEquals(expectedStateCode, stateCode, "The state code does not match the expected value.");
  }

  @Test
  void fetchBroadbandData_ApiErrorHandling() {
    String state = "XX"; // Invalid state code
    String county = "YYY"; // Invalid county code

    Exception exception =
        assertThrows(
            IOException.class,
            () -> adapter.fetchBroadbandData(state, county),
            "Expected to throw an IOException for invalid state/county codes");
  }

  @Test
  void getCountyCode_UsesCacheEfficiently() throws IOException {
    String stateName = "New Jersey";
    String countyName = "Bergen";
    adapter.getCountyCode(stateName, countyName);
    long startTime = System.currentTimeMillis();
    adapter.getCountyCode(stateName, countyName);
    long duration = System.currentTimeMillis() - startTime;
    assertTrue(duration < 100, "Fetching county code from cache should be fast");
  }

  @Test
  void testGetStateCode_EmptyStateName() {
    String stateCode = adapter.getStateCode("");
    assertNull(stateCode, "The state code for an empty state name should be null.");
  }


  @Test
  void testFetchBroadbandData_InvalidState() {
    String state = "XX"; // Invalid state code
    String county = "003";
    assertThrows(IOException.class,
            () -> adapter.fetchBroadbandData(state, county),
            "Expected IOException for invalid state code");
  }

  @Test
  void testFetchBroadbandData_InvalidCounty() {
    String state = "34";
    String county = "YYY"; // Invalid county code
    assertThrows(IOException.class,
            () -> adapter.fetchBroadbandData(state, county),
            "Expected IOException for invalid county code");
  }

}
