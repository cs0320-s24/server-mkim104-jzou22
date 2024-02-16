package edu.brown.cs.student.api_tests.mocks;

import edu.brown.cs.student.main.api.IDataFetcher;

import java.io.IOException;
import java.util.List;

public class MockDataFetcher implements IDataFetcher {

  @Override
  public String fetchStateCode(String stateName) throws IOException {
    if (stateName.equals("California")) {
      return "031";
    }
    return "";
  }

  @Override
  public String fetchCountyCode(String stateName, String countyName) throws IOException {
    if (stateName.equals("California") && countyName.equals("Kings County")) {
      return "06";
    }
    return "";
  }

  @Override
  public List<List<String>> fetchBroadbandData(String state, String county) {
    return null;
  }
}
