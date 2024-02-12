package edu.brown.cs.student.main.csv.parser;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class: This is the parser to parse the input (CSV) and create the necessary objects using rows.
 * This class should be able to handle any type T objects.
 *
 * @param <T>: To handle any object data type.
 */

/**
 * Constructor for CSVParser.
 *
 * @param reader Reads the CSV.
 * @param row_creator Creates and converts rows.
 */
public class CSVParser<T> {
  private final CreatorFromRow<T> row_creator;
  private final boolean hasHeader;
  private final BufferedReader reader;
  private Consumer<Exception> errorHandler;
  private char delimiter = ',';
  private char quoteChar = '"';

  public CSVParser(
      Reader reader,
      CreatorFromRow<T> rowcreator,
      char delimiter,
      char quoteChar,
      boolean hasHeader) {
    this.reader = new BufferedReader(reader);
    this.row_creator = rowcreator;
    this.delimiter = delimiter;
    this.hasHeader = hasHeader;
    this.quoteChar = quoteChar;
    this.errorHandler =
        null; // I know we do not like null but I want the developer to set the error they want.
  }

  /**
   * Story 2: Allows the user to set a custom error handler for error handling.
   *
   * @param errorHandler A Consumer that takes an Exception as input.
   */
  public void setExceptionHandler(Consumer<Exception> errorHandler) {
    this.errorHandler = errorHandler;
  }

  /**
   * Main function to parse the CSV and convert each row into type T.
   *
   * @return List of type T created from the rows of CSV.
   * @throws IOException If I/O error.
   * @throws FactoryFailureException If creation of object fails or other misc errors.
   */
  public List<T> parse() throws IOException, FactoryFailureException {
    List<T> results = new ArrayList<>();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        try {
          List<String> row = parseLine(line);
          T obj = row_creator.create(row);
          results.add(obj);
        } catch (FactoryFailureException e) {
          handleException(e);
        }
      }
    } catch (IOException e) {
      handleException(e);
    } finally {
      closeReader();
    }
    return results;
  }

  /**
   * Custom Exception handling to allow developer to edit!
   *
   * @param e The exception we want to customize.
   * @throws FactoryFailureException Default option if none is set by developer
   */
  private void handleException(Exception e) throws FactoryFailureException, IOException {
    if (errorHandler != null) {
      errorHandler.accept(e);
    } else {
      defaultErrorHandler(e);
    }
  }

  /**
   * Default error handler if none is set.
   *
   * @param e The exception to handle.
   */
  private void defaultErrorHandler(Exception e) throws FactoryFailureException, IOException {
    // Rethrow the exception if it's a FactoryFailureException or IOException
    if (e instanceof FactoryFailureException) {
      throw (FactoryFailureException) e;
    } else if (e instanceof IOException) {
      throw (IOException) e;
    } else {
      // Developer can log and add another exception if they want to here.
      throw new FactoryFailureException(
          "Error encountered: " + e.getMessage(), Collections.emptyList());
    }
  }

  /**
   * Basic function for closing ofBufferedReader.
   *
   * @throws IOException If error occurs when closed.
   */
  private void closeReader() throws IOException {
    try {
      reader.close();
    } catch (IOException e) {
      if (errorHandler != null) {
        errorHandler.accept(e);
      } else {
        throw e;
      }
    }
  }
  /**
   * Parser Function by line.
   *
   * @param line The line to parse.
   * @return A list of strings which are the fields in the line.
   */
  private List<String> parseLine(String line) throws FactoryFailureException {
    if (line.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> keys = new ArrayList<>();
    boolean inQuotes = false;
    StringBuilder stringBuilder = new StringBuilder();
    char[] chars = line.toCharArray();

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == quoteChar) {
        if (inQuotes && i < chars.length - 1 && chars[i + 1] == quoteChar) {
          stringBuilder.append(c);
          i++; // Skip the escaped quote.
        } else {
          inQuotes = !inQuotes;
        }
      } else if (c == delimiter && !inQuotes) {
        keys.add(stringBuilder.toString().trim());
        stringBuilder.setLength(0); // Resetting field builder for next entry in for loop.
      } else {
        stringBuilder.append(c);
      }
    }
    keys.add(stringBuilder.toString().trim());
    return keys;
  }
}
