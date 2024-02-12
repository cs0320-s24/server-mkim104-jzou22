package edu.brown.cs.student.main;

import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.csv.handler.LoadCSVHandler;
import edu.brown.cs.student.main.csv.handler.SearchCSVHandler;
import edu.brown.cs.student.main.csv.handler.ViewCSVHandler;
import edu.brown.cs.student.main.csv.parser.CSVParser;
import edu.brown.cs.student.main.server.RequestHandler;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;
public class Main {
  public static void main(String[] args) {
    CensusApiAdapter adapter = new CensusApiAdapter();

    // Initialize server
    Server server = new SparkServer();
    server.start();

    // Create an instance of BroadbandDataHandler
    BroadbandDataHandler broadbandDataHandler = new BroadbandDataHandler(adapter);

    // Create an instances of CSVHandlers
    LoadCSVHandler loadCSVHandler = new LoadCSVHandler();
    ViewCSVHandler viewCSVHandler = new ViewCSVHandler(loadCSVHandler);
    SearchCSVHandler searchCSVHandler = new SearchCSVHandler(loadCSVHandler.getLoaded());

    // Registering the broadband endpoint with the new handler
    server.registerHandler("/broadband", broadbandDataHandler);

    // Registering the csv endpoint with the new handler
    server.registerHandler("/loadcsv", loadCSVHandler);
    server.registerHandler("/viewcsv", viewCSVHandler);
    server.registerHandler("/searchcsv", searchCSVHandler);

    System.out.println("Server is running on port 3232");
  }
}
