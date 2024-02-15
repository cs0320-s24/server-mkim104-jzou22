package edu.brown.cs.student.main.csv.parser;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CreatorFromRow;

import java.util.List;

/**
 * This is the generic custom class that is used for the search class specifically because
 * we want to covert each row into a string array.
 */
public class RowCreator implements CreatorFromRow<List<String>> {
    /**
     * This is the method that is implemented from the interface, and it returns alist of string
     * @param row
     * @return
     * @throws FactoryFailureException
     */
    @Override
    public List<String> create(List<String> row) throws FactoryFailureException {
        return row;
    }
}