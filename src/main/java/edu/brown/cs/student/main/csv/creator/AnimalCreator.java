package edu.brown.cs.student.main.csv.creator;

import edu.brown.cs.student.main.csv.parser.CreatorFromRow;
import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.util.List;

/** This class implements the CreatorFromRow interface to create an Animal object from a CSV row. */
public class AnimalCreator implements CreatorFromRow<Animal> {

  /**
   * Field for the Animal object: ID, name, and species. The ID is expected to be a valid integer;
   * name and species are strings.
   *
   * @param row A List of Strings
   * @return An Animal object constructed from the CSV row data.
   * @throws FactoryFailureException if the ID is not a valid integer or if there are not enough
   *     columns in the row.
   */
  @Override
  public Animal create(List<String> row) throws FactoryFailureException {
    try {
      int id = Integer.parseInt(row.get(0));
      String name = row.get(1);
      String species = row.get(2);
      return new Animal(id, name, species);
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Invalid ID format for Animal", row);
    } catch (IndexOutOfBoundsException e) {
      throw new FactoryFailureException("Invalid Column Format", row);
    }
  }
}
