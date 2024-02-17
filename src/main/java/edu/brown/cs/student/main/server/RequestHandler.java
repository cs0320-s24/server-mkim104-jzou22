package edu.brown.cs.student.main.server;

import spark.Request;
import spark.Response;

/**
 * Functional interface representing a handler for processing HTTP requests. Implementing classes
 * are responsible for handling incoming requests and producing responses.
 */
@FunctionalInterface
public interface RequestHandler {
  /**
   * Handles the HTTP request and produces a response.
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return The result of processing the request.
   * @throws Exception if an error occurs during request handling.
   */
  Object handle(Request request, Response response) throws Exception;
}
