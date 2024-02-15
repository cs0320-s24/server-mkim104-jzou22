package edu.brown.cs.student.main.testing.creators;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CreatorFromRow;
import edu.brown.cs.student.main.testing.Student;

import java.util.List;

/**
 * This is a student creator class that implements the creator for row
 */
public class StudentCreator implements CreatorFromRow<Student> {
    @Override
    public Student create(List<String> row) throws FactoryFailureException {
        return new Student(row.get(0), row.get(1), row.get(2));
    }
}