package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csvsearch.CSVSearch;

/**
 * The Main class of our project. This is where execution begins.
 *
 * <p>This utility program searches for a specified value within a CSV file. It can search across
 * all columns or within a specific column, identified by header name or index.
 *
 * <p>Requirements: 1. CSV filename: The name of the CSV file to search, which must be located
 * within a predefined "data" directory. 2. Value to search: The string value to search for within
 * the CSV file. 3. Column identifier (Optional): The header name or column index to narrow down the
 * search to a specific column. If left unspecified, the search will be performed across all
 * columns. 4. Headers present (Optional): A boolean indicating whether the CSV file includes
 * headers. Defaults to true if not specified.
 *
 * <p>Usage: java -cp <classpath> edu.brown.cs.student.main.Main <filename> <value to search>
 * [column identifier] [headers present] Example: java -cp . edu.brown.cs.student.main.Main
 * data/example.csv "search term" 2 true
 */
public class Main {
  public static void main(String[] args) {
    // Print usage information if insufficient arguments are provided
    if (args.length < 2 || args.length > 4) {
      System.err.println(
          "Usage: <filename> <value to search> [column identifier] [headers present]\n"
              + "Example: java Main data/example.csv \"search term\" columnName true\n"
              + "Note: - <filename> should be within the \"data\" directory.\n"
              + "      - [column identifier] can be a column name (if headers are present) or a column index. Leave blank to search all columns.\n"
              + "      - [headers present] is true by default. Specify false if your CSV does not include headers.");
      System.exit(1);
    }

    String filename = args[0];
    String value = args[1];
    // Column may be a name or an index; if not provided, search all columns.
    String column = args.length > 2 ? args[2] : null;
    // Assume headers are present by default
    boolean headersPresent = args.length <= 3 || Boolean.parseBoolean(args[3]);

    try {
      CSVSearch utility = new CSVSearch(filename, headersPresent);
      utility.search(value, column);
    } catch (Exception e) {
      System.err.println("Error during search: " + e.getMessage());
      System.exit(1);
    }
  }
}
