package edu.brown.cs.student.main.server;

import spark.Service;

public class SparkServer implements Server {
  private Service http;
  public SparkServer() {
    this.http = Service.ignite();
  }

  @Override
  public void start() {
    http.port(3232);
    System.out.println("Server started on port 3232");
  }

  @Override
  public void stop() {
    http.stop();
  }

  @Override
  public void registerHandler(String path, RequestHandler handler) {
    // Handle different registers
    http.get(path, (req, res) -> handler.handle(req, res));
  }
}
