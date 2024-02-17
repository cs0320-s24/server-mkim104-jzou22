package edu.brown.cs.student.main.csv.handler;

import edu.brown.cs.student.main.csv.search.CSVSearcher;
import edu.brown.cs.student.main.server.RequestHandler;
import java.net.URLDecoder;
import spark.Request;
import spark.Response;

/**
 * This class handles HTTP requests to search within a loaded CSV file. It implements the
 * RequestHandler interface to process incoming requests.
 */
public class SearchCSVHandler implements RequestHandler {
  private final LoadCSVHandler loadCSVHandler;
  /**
   * Constructs a SearchCSVHandler object with the specified LoadCSVHandler.
   *
   * @param loadCSVHandler The handler for loading CSV files.
   */
  public SearchCSVHandler(LoadCSVHandler loadCSVHandler) {
    this.loadCSVHandler = loadCSVHandler;
  }
  /**
   * Handles the HTTP request to search within a loaded CSV file based on query parameters.
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return A message indicating the search results.
   * @throws Exception if an error occurs during searching.
   */
  // example query:
  // http://localhost:3232/searchcsv?searchValue=Rhode%20Island&header=true&columnIdentifier=City/Townd&columnType=
  @Override
  public Object handle(Request request, Response response) throws Exception {
    // retrieve the query params
    String searchValue = request.queryParams("searchValue");
    String header = request.queryParams("header");
    String columnIdentifier = request.queryParams("columnIdentifier");
    String columnType = request.queryParams("columnType");

    // errors checks the value
    if (searchValue == null || header == null || columnIdentifier == null || columnType == null) {
      response.status(400);
      return "[searchValue] [header] [columnIdentifier] [columnType] parameters are required.";
    }
    String newSearchValue = URLDecoder.decode(searchValue, "UTF-8");
    String newColumnIdentifier = URLDecoder.decode(columnIdentifier, "UTF-8");

    // error checks the correct value
    if (!header.equalsIgnoreCase("true")
        && !header.equalsIgnoreCase("false")
        && !columnIdentifier.equalsIgnoreCase("true")
        && !columnIdentifier.equalsIgnoreCase("false")) {
      response.status(400);
      return "[header] only takes in a 'true' or 'false'";
    }
    if (!columnType.equalsIgnoreCase("I")
        && !columnType.equalsIgnoreCase("N")
        && !columnType.equalsIgnoreCase("")) {
      response.status(400);
      return "[columnType] can only be 'N' for name, 'I' for index, or blank otherwise";
    }

    // handles error
    if (loadCSVHandler.getData() == null || !loadCSVHandler.getLoaded()) {
      response.status(404);
      return "Must load a valid CSV file before viewing";
    }

    Boolean headerExistence = Boolean.valueOf(header);

    CSVSearcher searcher =
        new CSVSearcher(
            this.loadCSVHandler.getData(),
            newSearchValue,
            newColumnIdentifier,
            headerExistence,
            columnType);

    return "Here are the following results: " + searcher.getResult();
  }
}
