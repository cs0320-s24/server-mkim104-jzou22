package edu.brown.cs.student.main.csv.search;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a search class which is called by the user repl. This search takes in the parsed data and
 * depending on the user input, it will search through the parse data.
 */
public class CSVSearcher {
  List<List<String>> result = new ArrayList<>();

  /**
   * Initializes a Searcher object for searching through CSV data.
   *
   * @param data The CSVParser object containing the CSV data.
   * @param searchValue The value to search for in the CSV data.
   * @param columnIdentifier The identifier of the column to search within.
   * @param header A boolean indicating whether the CSV file contains a header row.
   * @param columnType The type of columnIdentifier (N for name, I for index).
   * @throws IOException If an I/O error occurs while reading the CSV data.
   * @throws FactoryFailureException If factory failure occurs while performing operations.
   */
  public CSVSearcher(
      List<List<String>> data,
      String searchValue,
      String columnIdentifier,
      Boolean header,
      String columnType)
      throws IOException, FactoryFailureException {

    // when columnIdentifier does not exist or when the types don't exist
    if (columnIdentifier.equals("") || columnType.equals("")) {
      this.searchDefault(data, searchValue, header);
    }
    // otherwise columnIdentifier does exist
    else {
      // specify the type of the column identifier
      if (columnType.equals("N") && header) {
        // we search by column name
        List<String> headerLine = data.get(0);

        // Iterate through the header to find the column identifier
        int columnIndex = -1;
        for (int i = 0; i < headerLine.size(); i++) {
          if (headerLine.get(i).equals(columnIdentifier)) {
            columnIndex = i;
            break;
          }
        }

        if (columnIndex == -1) {
          System.err.println("Column Name Does Not Exist");
        }

        this.searchByHeader(data, searchValue, columnIndex, header);
      }

      // the column identifier is an index
      if (columnType.equals("I")) {
        this.searchByHeader(data, searchValue, Integer.parseInt(columnIdentifier), header);
      }
    }
  }

  /**
   * Searches through the CSV data using default search method.
   *
   * @param data The CSVParser object containing the CSV data.
   * @param searchValue The value to search for in the CSV data.
   * @param header A boolean indicating whether the CSV file contains a header row.
   */
  private void searchDefault(List<List<String>> data, String searchValue, Boolean header) {
    if (header) {
      data.remove(0);
    }
    for (List<String> rowData : data) {
      if (rowData.contains(searchValue)) {
        this.result.add(rowData);
      }
    }
  }
  /**
   * Searches through the CSV data using the specified column index as the identifier. This is used
   * whenever we have a column identifier or whenever a type is defined is given.
   *
   * @param data The CSVParser object containing the CSV data.
   * @param searchValue The value to search for in the specified column.
   * @param columnIndex The index of the column to search within.
   * @param header A boolean indicating whether the CSV file contains a header row.
   */
  private void searchByHeader(
      List<List<String>> data, String searchValue, Integer columnIndex, Boolean header) {
    if (header) {
      data.remove(0);
    }
    for (List<String> rowData : data) {
      if (rowData.get(columnIndex).equals(searchValue)) {
        this.result.add(rowData);
      }
    }
  }

  /**
   * Returns the search data which is used for the testing class
   *
   * @return
   */
  public List<List<String>> getResult() {
    return this.result;
  }
}
