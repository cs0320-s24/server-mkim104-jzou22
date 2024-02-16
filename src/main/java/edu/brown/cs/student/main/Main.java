package edu.brown.cs.student.main;

import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.cache.ACSDataCache;
import edu.brown.cs.student.main.csv.handler.LoadCSVHandler;
import edu.brown.cs.student.main.csv.handler.SearchCSVHandler;
import edu.brown.cs.student.main.csv.handler.ViewCSVHandler;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;
import java.util.concurrent.TimeUnit;

public class Main {
  /**
   * Main class responsible for initializing and starting the server, as well as registering request handlers.
   */
  public static void main(String[] args) {
    ACSDataCache acsDataCache = new ACSDataCache(100, 30, TimeUnit.MINUTES);

    CensusApiAdapter adapter = new CensusApiAdapter(acsDataCache);

    Server server = new SparkServer();
    server.start();

    BroadbandDataHandler broadbandDataHandler = new BroadbandDataHandler(adapter);

    server.registerHandler("/broadband", broadbandDataHandler);

    // Create an instances of CSVHandlers
    LoadCSVHandler loadCSVHandler = new LoadCSVHandler();
    ViewCSVHandler viewCSVHandler = new ViewCSVHandler(loadCSVHandler);
    SearchCSVHandler searchCSVHandler = new SearchCSVHandler(loadCSVHandler);
    // Registering the csv endpoint with the new handler
    server.registerHandler("/loadcsv", loadCSVHandler);
    server.registerHandler("/viewcsv", viewCSVHandler);
    server.registerHandler("/searchcsv", searchCSVHandler);

    System.out.println("Server is running on port 3232");
  }
}
