package edu.brown.cs.student.main;
import edu.brown.cs.student.main.csvsearch.CSVSearch;
import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;

public class Main {
  public static void main(String[] args) {
    Server server = new SparkServer();
    server.start();

    CensusApiAdapter censusApiAdapter = new CensusApiAdapter();
    BroadbandDataHandler broadbandHandler = new BroadbandDataHandler(censusApiAdapter);

    server.registerHandler("/broadband", broadbandHandler);
  }
}
