package edu.brown.cs.student.main.csvsearch;

import edu.brown.cs.student.main.csvparser.CSVParser;
import edu.brown.cs.student.main.errorhandler.FactoryFailureException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Search class by column names or indices and handles files with or without headers.
 */
public class CSVSearch {
  private CSVParser<List<String>> parser = null;
  private List<String> headers;
  private final char delimiter = ',';
  private final char doubleQuotes = '"'; //delimeter for CSV's with double quotes.
  private List<List<String>> data; // Store the CSV data
  private final String restrictedDirectory = "data"; // Set the restricted directory path.

  /**
   * Constructor:CSVSearch object for a given CSV.
   *
   * @param filename The path to the CSV file to be searched.
   * @param headersPresent Indicates if the CSV file contains headers.
   * @throws SecurityException If the file is located outside the restricted directory.
   * @throws FileNotFoundException If the specified file does not exist.
   * @throws IOException If an I/O error occurs reading from the file.
   * @throws FactoryFailureException If parsing the CSV file fails.
   */
  public CSVSearch(String filename, boolean headersPresent) {
    try {
      if (!filename.startsWith(restrictedDirectory + "/")) {
        throw new SecurityException("Access to the file is restricted");
      }
      parser =
              new CSVParser<>(
                      new FileReader(filename), row -> row, delimiter, doubleQuotes, headersPresent);
      List<List<String>> allData = parser.parse();
      if (headersPresent && !allData.isEmpty()) {
        headers = allData.get(0);
        data = allData.subList(1, allData.size());
      } else {
        data = allData;
      }
    } catch (FileNotFoundException e) {
      System.err.println("Error: File not found - " + filename);
    } catch (IOException | FactoryFailureException e) {
      System.err.println("Error: Reading file: " + e.getMessage());
    } catch (SecurityException | IllegalStateException e) {
      System.err.println("Error: Location Invalid " + e.getMessage());
    }
  }

  /**
   * Searches for a value in the specified column of the CSV data.
   * If the column identifier is null or empty, searches across all columns.
   *
   * @param value The value to search for.
   * @param columnIdentifier The name OR index of the column to search in.
   */
  public void search(String value, String columnIdentifier) {
    boolean matchFound = false;

    for (List<String> row : data) {
      boolean matches =
              (columnIdentifier == null || columnIdentifier.isEmpty())
                      ? rowContainsValue(row, value)
                      : columnMatches(row, value, columnIdentifier);

      if (matches) {
        System.out.println("Match Found: " + row);
        matchFound = true;
      }
    }

    if (!matchFound) {
      System.out.println("No match found.");
    }
  }

  /**
   * Checks if any cell in a row contains the specified value/.
   *
   * @param row The row to check.
   * @param value The value to search for.
   * @return true if the value is found in the row; false otherwise.
   */
  private boolean rowContainsValue(List<String> row, String value) {
    return row.stream().anyMatch(cell -> cell.equalsIgnoreCase(value));
  }

  /**
   * Checks if a specified column in a row contains the specified value.
   *
   * @param row The row to check.
   * @param value The value to search for.
   * @param columnIdentifier The name or index of the column to search.
   * @return true if the value is found in the specified column;
   */
  private boolean columnMatches(List<String> row, String value, String columnIdentifier) {
    int columnIndex = getColumnIndex(columnIdentifier);
    if (columnIndex < 0 || columnIndex >= row.size()) {
      return false; // Column index out of bounds
    }
    return row.get(columnIndex).equalsIgnoreCase(value);
  }

  /**
   * Retrieves the index of the column specified by the column identifier.
   *
   * @param columnIdentifier The name or index of the column.
   * @return The index of the column if found; -1 if not found or if headers are not present.
   */
  private int getColumnIndex(String columnIdentifier) {
    // Check if the columnIdentifier is numeric
    try {
      int colIndex = Integer.parseInt(columnIdentifier);
      // If headers are present, ensure columnIndex is within bounds
      if (headers != null && (colIndex < 0 || colIndex >= headers.size())) {
        return -1; // Column index out of bounds
      }
      return colIndex;
    } catch (NumberFormatException e) {
      // columnIdentifier is not numeric, proceed to search in headers if available
      if (headers != null) {
        int index = headers.indexOf(columnIdentifier);
        return index;
      } else {
        // Headers are not available, and columnIdentifier is not numeric
        return -1;
      }
    }
  }

  /**
   * Sets a custom exception handler for the CSV parser.
   *
   * @param errorHandler The exception handler to set.
   */
  public void setExceptionHandler(Consumer<Exception> errorHandler) {
    this.parser.setExceptionHandler(errorHandler);
  }
}
