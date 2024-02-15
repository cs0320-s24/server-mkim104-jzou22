package edu.brown.cs.student.main.testing.creators;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CreatorFromRow;

import java.util.List;

/**
 * This is an object class that implements the CreatorFromRow.
 *
 */
public class StringCreator implements CreatorFromRow<String> {
    /**
     * This create method is used to convert each row as the concatenation of string
     * @param row
     * @return
     * @throws FactoryFailureException
     */
    @Override
    public String create(List<String> row) throws FactoryFailureException {
        return String.join(",", row);
    }
}