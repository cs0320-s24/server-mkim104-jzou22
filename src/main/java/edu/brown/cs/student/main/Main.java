package edu.brown.cs.student.main;

import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
import edu.brown.cs.student.main.cache.ACSDataCache;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.SparkServer;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    ACSDataCache acsDataCache = new ACSDataCache(100, 30, TimeUnit.MINUTES);

    CensusApiAdapter adapter = new CensusApiAdapter(acsDataCache);

    Server server = new SparkServer();
    server.start();

    BroadbandDataHandler broadbandDataHandler = new BroadbandDataHandler(adapter);

    server.registerHandler("/broadband", broadbandDataHandler);

    server.registerHandler(
        "/csv", broadbandDataHandler);
    System.out.println("Server is running on port 3232");
  }
}
