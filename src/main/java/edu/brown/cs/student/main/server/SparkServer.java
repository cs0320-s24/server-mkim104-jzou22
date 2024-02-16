package edu.brown.cs.student.main.server;

import spark.Service;
/**
 * Implementation of the Server interface using Spark.
 * This class handles starting, stopping, and registering request handlers using Spark's service.
 */
public class SparkServer implements Server {
  private Service http;
  /**
   * Constructs a SparkServer instance and initializes the Spark service.
   */
  public SparkServer() {
    this.http = Service.ignite();
  }
  /**
   * Starts the Spark server on the specified port.
   */
  @Override
  public void start() {
    http.port(3232);
    System.out.println("Server started on port 3232");
  }
  /**
   * Stops the Spark server.
   */
  @Override
  public void stop() {
    http.stop();
  }
  /**
   * Registers a request handler for the specified path using Spark.
   *
   * @param path    The path for which the handler is registered.
   * @param handler The request handler to be registered.
   */
  @Override
  public void registerHandler(String path, RequestHandler handler) {
    // Handle different registers
    http.get(path, (req, res) -> handler.handle(req, res));
  }
}
