package edu.brown.cs.student.main.server;

public interface Server {
  void start();

  void stop();

  void registerHandler(String path, RequestHandler handler);
}
