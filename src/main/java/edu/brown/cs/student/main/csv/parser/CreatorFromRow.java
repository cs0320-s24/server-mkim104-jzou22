package edu.brown.cs.student.main.csv.parser;
import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type.
 *
 * <p>Your parser class constructor should take a second parameter of this generic interface type.
 */
public interface CreatorFromRow<T> {
    /**
     * A method that must be defined when implementing this interface. This allows for greater generic
     * object conversion
     * @param row
     * @return
     * @throws FactoryFailureException
     */
    T create(List<String> row) throws FactoryFailureException;
}