package edu.brown.cs.student.main.testing.creators;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CreatorFromRow;
import java.util.List;

/** This is an object class that implements the CreatorFromRow. */
public class IntegerCreator implements CreatorFromRow<Integer> {
  /**
   * This create method is used to convert each row as the number of items in that row
   *
   * @param row
   * @return
   * @throws FactoryFailureException
   */
  @Override
  public Integer create(List<String> row) throws FactoryFailureException {
    return row.size();
  }
}
