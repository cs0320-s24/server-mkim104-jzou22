package edu.brown.cs.student.api_tests.mocks;

import edu.brown.cs.student.main.api.IDataFetcher;
import java.io.IOException;
import java.util.List;

/** Mock implementation of the IDataFetcher interface for testing purposes. */
public class MockDataFetcher implements IDataFetcher {
  /**
   * Fetches the state code for the given state name.
   *
   * @param stateName The name of the state.
   * @return The state code.
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public String fetchStateCode(String stateName) throws IOException {
    if (stateName.equals("California")) {
      return "031";
    }
    return "";
  }
  /**
   * Fetches the county code for the given state name and county name.
   *
   * @param stateName The name of the state.
   * @param countyName The name of the county.
   * @return The county code.
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public String fetchCountyCode(String stateName, String countyName) throws IOException {
    if (stateName.equals("California") && countyName.equals("Kings County")) {
      return "06";
    }
    return "";
  }
  /**
   * Fetches broadband data for the given state and county.
   *
   * @param state The state for which data is fetched.
   * @param county The county for which data is fetched.
   * @return The broadband data.
   */
  @Override
  public List<List<String>> fetchBroadbandData(String state, String county) {
    return null;
  }
}
