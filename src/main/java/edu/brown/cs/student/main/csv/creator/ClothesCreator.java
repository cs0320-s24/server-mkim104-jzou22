package edu.brown.cs.student.main.csv.creator;

import edu.brown.cs.student.main.csv.parser.CreatorFromRow;
import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.util.List;

/** Implements the CreatorFromRow interface to create a Clothes object from a CSV row. */
public class ClothesCreator implements CreatorFromRow<Clothes> {

  /**
   * Fields for the Clothes object: code, name price, and sale status. Price is expected to be a
   * valid double, sale status is converted to boolean.
   *
   * @param row A List of Strings, where each String represents a column value in the CSV row.
   * @return A Clothes object constructed from the CSV row data.
   * @throws FactoryFailureException if the price format is invalid or if there are not enough
   *     columns in the row.
   */
  @Override
  public Clothes create(List<String> row) throws FactoryFailureException {
    try {
      String code = row.get(0);
      String name = row.get(1);
      double price = Double.parseDouble(row.get(2));
      boolean sale = parseSaleStatus(row.get(3));
      return new Clothes(code, name, price, sale);
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Invalid price format for Clothes", row);
    } catch (IndexOutOfBoundsException e) {
      throw new FactoryFailureException("Invalid Column Format", row);
    }
  }

  /**
   * Parses the sale status from a string value. The sale status is considered true if the string is
   * "True" (case insensitive), false otherwise.
   *
   * @param saleStatus The string representation of the sale status.
   * @return true if saleStatus is "True" (case insensitive), false otherwise.
   */
  private boolean parseSaleStatus(String saleStatus) {
    return "True".equalsIgnoreCase(saleStatus);
  }
}
