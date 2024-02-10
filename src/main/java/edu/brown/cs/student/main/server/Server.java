package edu.brown.cs.student.main.server;
import spark.Service;

public interface Server {
    void start();
    void stop();
    void registerHandler(String path, RequestHandler handler);
}
