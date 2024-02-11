package edu.brown.cs.student.main;

import edu.brown.cs.student.main.api.BroadbandDataHandler;
import edu.brown.cs.student.main.api.CensusApiAdapter;
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

    // Registering the broadband endpoint with the new handler
    server.registerHandler("/broadband", broadbandDataHandler);

    System.out.println("Server is running on port 3232");
  }
}
