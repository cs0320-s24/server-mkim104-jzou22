package edu.brown.cs.student.main.csv.parser;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


/**
 * This is the user class which is responsible for the parsing of a CSV.
 * It uses a buffered reader to read each line, and then you use a regex.
 *
 * @param <T>
 */
public class CSVParser<T> {
  List<T> parseArray = new ArrayList<>();
  int initialColumnSize = -1;
  static final Pattern csvRegex =
          Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Parse constructor which takes in the reader object and the creator type
   * and then parses.
   * @param read
   * @param creator
   * @throws IOException
   * @throws FactoryFailureException
   */
  public CSVParser(Reader read, CreatorFromRow<T> creator)
          throws IOException, FactoryFailureException {
    this.parse(read, creator);
  }

  /**
   * Parses the content of a CSV file read from the provided Reader object,
   * using the specified CreatorFromRow for creating objects from each row.
   *
   * @param read    The Reader object containing the CSV file content.
   * @param creator The CreatorFromRow implementation to create objects from CSV rows.
   * @return        A List of objects created from the CSV rows.
   * @throws IOException              If an I/O error occurs while reading the file.
   * @throws FactoryFailureException  If the creation of objects fails due to any reason.
   */
  private List<T> parse(Reader read, CreatorFromRow<T> creator)
          throws IOException, FactoryFailureException {
    // we wrap our Reader object as a BufferedReader so that we can read
    BufferedReader bufferReader = new BufferedReader(read);

    // reads the first line
    String line = bufferReader.readLine();

    while ((line != null)) {
      String[] lineContent = this.csvRegex.split(line);

      // initializes the column size
      if (this.initialColumnSize == -1) {
        this.initialColumnSize = lineContent.length;
      }

      for (int i = 0; i < lineContent.length; i++) {
        lineContent[i] = this.postprocess(lineContent[i]).trim();
      }
      // convert for the create method
      List<String> rowList = Arrays.asList(lineContent);

      // check if they are the same size
      if (this.initialColumnSize != rowList.size()) {
        throw new IndexOutOfBoundsException("Inconsistent number of columns in CSV file");
      }

      // output of the create method
      T result = creator.create(rowList);

      // store the output
      this.parseArray.add(result);

      // next iteration
      line = bufferReader.readLine();
    }

    return this.parseArray;
  }

  /**
   * Elimiate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  public static String postprocess(String arg) {
    return arg
            // Remove extra spaces at beginning and end of the line
            .trim()
            // Remove a beginning quote, if present
            .replaceAll("^\"", "")
            // Remove an ending quote, if present
            .replaceAll("\"$", "")
            // Replace double-double-quotes with double-quotes
            .replaceAll("\"\"", "\"");
  }

  /**
   * Get the parse data
   * @return
   */
  public List<T> getParseArray() {
    return this.parseArray;
  }
}
