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

public class ViewCSVHandler implements RequestHandler {
  private final LoadCSVHandler loadCSVHandler;
  private final Moshi moshi;
  private final JsonAdapter<Map<String, Object>> jsonAdapter;

  public ViewCSVHandler(LoadCSVHandler loadCSVHandler) {
    this.loadCSVHandler = loadCSVHandler;
    this.moshi = new Moshi.Builder().build();
    // define the type that we want to convert
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.jsonAdapter = this.moshi.adapter(type);
  }

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
   * Method to convert the 2d array into a hashmap so it can be converted to a json file
   *
   * @param data
   * @return
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
