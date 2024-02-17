package edu.brown.cs.student.main.api;

import java.io.IOException;
import java.util.List;

/** This interface defines methods for fetching state codes, county codes, and broadband data. */
public interface IDataFetcher {
  /**
   * Fetches the state code corresponding to the given state name.
   *
   * @param stateName The name of the state.
   * @return The state code.
   * @throws IOException if an I/O error occurs.
   */
  String fetchStateCode(String stateName) throws IOException;
  /**
   * Fetches the county code corresponding to the given state name and state code.
   *
   * @param stateName The name of the state.
   * @param stateCode The code of the state.
   * @return The county code.
   * @throws IOException if an I/O error occurs.
   */
  String fetchCountyCode(String stateName, String stateCode) throws IOException;
  /**
   * Fetches broadband data for the specified state and county.
   *
   * @param state The state code.
   * @param county The county code.
   * @return A list of lists containing broadband data.
   * @throws IOException if an I/O error occurs.
   */
  List<List<String>> fetchBroadbandData(String state, String county) throws IOException;
}
