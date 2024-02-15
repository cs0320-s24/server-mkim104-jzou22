package edu.brown.cs.student;

import java.io.IOException;

public interface ApiRequester {
  String makeRequest(String urlString) throws IOException;
}
