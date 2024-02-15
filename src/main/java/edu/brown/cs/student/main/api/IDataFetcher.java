package edu.brown.cs.student.main.api;

import java.io.IOException;
import java.util.List;

public interface IDataFetcher {
  String fetchStateCode(String stateName) throws IOException;

  String fetchCountyCode(String stateName, String stateCode) throws IOException;

  List<List<String>> fetchBroadbandData(String state, String county) throws IOException;
}
