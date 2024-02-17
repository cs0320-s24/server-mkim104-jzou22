package edu.brown.cs.student.main.csv.handler;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.RequestHandler;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;

/**
 * This class handles HTTP requests to view the contents of a loaded CSV file in JSON format. It
 * implements the RequestHandler interface to process incoming requests.
 */
public class ViewCSVHandler implements RequestHandler {
  private final LoadCSVHandler loadCSVHandler;
  private final Moshi moshi;
  private final JsonAdapter<Map<String, Object>> jsonAdapter;
  /**
   * Constructs a ViewCSVHandler object with the specified LoadCSVHandler.
   *
   * @param loadCSVHandler The handler for loading CSV files.
   */
  public ViewCSVHandler(LoadCSVHandler loadCSVHandler) {
    this.loadCSVHandler = loadCSVHandler;
    this.moshi = new Moshi.Builder().build();
    // define the type that we want to convert
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.jsonAdapter = this.moshi.adapter(type);
  }
  /**
   * Handles the HTTP request to view the contents of a loaded CSV file in JSON format.
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return JSON representation of the CSV data.
   * @throws Exception if an error occurs during conversion or handling.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // handles error
    if (loadCSVHandler.getData() == null || !loadCSVHandler.getLoaded()) {
      response.status(404);
      return "Must load a valid CSV file before viewing";
    }

    Map<String, Object> jsonMap = convertCsvToMap(loadCSVHandler.getData());
    response.type("application/json");
    return jsonAdapter.toJson(jsonMap);
  }

  /**
   * Converts the 2D array representing CSV data into a map for JSON conversion.
   *
   * @param data The 2D array representing CSV data.
   * @return A map representing the CSV data for JSON conversion.
   */
  public Map<String, Object> convertCsvToMap(List<List<String>> data) {
    Map<String, Object> jsonMap = new HashMap<>();
    List<String> header = data.get(0);
    List<Map<String, Object>> rows = new ArrayList<>();

    for (int i = 1; i < data.size(); i++) {
      Map<String, Object> rowMap = new HashMap<>();
      List<String> values = data.get(i);

      // map the header to the correct value
      for (int j = 0; j < header.size(); j++) {
        rowMap.put(header.get(j), values.get(j));
      }

      rows.add(rowMap);
    }
    jsonMap.put("data", rows);
    return jsonMap;
  }
}
