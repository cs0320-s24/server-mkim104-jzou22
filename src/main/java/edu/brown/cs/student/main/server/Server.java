package edu.brown.cs.student.main.server;
/**
 * Interface representing a server. Implementing classes are responsible for starting, stopping, and
 * registering request handlers.
 */
public interface Server {
  void start();

  void stop();
  /**
   * Registers a request handler for the specified path.
   *
   * @param path The path for which the handler is registered.
   * @param handler The request handler to be registered.
   */
  void registerHandler(String path, RequestHandler handler);
}
