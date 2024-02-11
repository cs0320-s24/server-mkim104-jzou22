package edu.brown.cs.student.main.server;

import spark.Request;
import spark.Response;

@FunctionalInterface
public interface RequestHandler {
  Object handle(Request request, Response response) throws Exception;
}
