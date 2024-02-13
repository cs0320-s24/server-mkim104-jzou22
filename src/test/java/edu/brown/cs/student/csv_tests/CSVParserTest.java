package edu.brown.cs.student.csv_tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CSVParser;
import edu.brown.cs.student.main.csv.parser.CreatorFromRow;
import edu.brown.cs.student.main.csv.parser.RowCreator;
import edu.brown.cs.student.main.testing.creators.IntegerCreator;
import edu.brown.cs.student.main.testing.creators.StringCreator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.main.testing.Student;
import edu.brown.cs.student.main.testing.creators.StudentCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * A test class that tests the feature and different inputs of the CSVParser
 */
public class CSVParserTest {
    private static List<List<String>> groceryList;
    private static List<List<String>> coffeeList;

    // Initial Data
    @BeforeAll
    public static void initialize() {
        groceryList = new ArrayList<>();
        coffeeList = new ArrayList<>();

        ArrayList<String> grocery1 = new ArrayList<>();
        grocery1.add("Item");
        grocery1.add("Category");
        grocery1.add("Price");
        groceryList.add(grocery1);

        ArrayList<String> grocery2 = new ArrayList<>();
        grocery2.add("Apple");
        grocery2.add("Fruit");
        grocery2.add("1.99");
        groceryList.add(grocery2);

        ArrayList<String> grocery3 = new ArrayList<>();
        grocery3.add("Banana");
        grocery3.add("Fruit");
        grocery3.add("0.99");
        groceryList.add(grocery3);

        ArrayList<String> grocery4 = new ArrayList<>();
        grocery4.add("Carrot");
        grocery4.add("Vegetable");
        grocery4.add("0.50");
        groceryList.add(grocery4);

        ArrayList<String> grocery5 = new ArrayList<>();
        grocery5.add("Milk");
        grocery5.add("Dairy");
        grocery5.add("2.49");
        groceryList.add(grocery5);

        // Adding coffee items
        ArrayList<String> coffee1 = new ArrayList<>();
        coffee1.add("Starbucks");
        coffee1.add("Coffee");
        coffee1.add("3.50");
        coffeeList.add(coffee1);

        ArrayList<String> coffee2 = new ArrayList<>();
        coffee2.add("Dunkin Donuts");
        coffee2.add("Coffee");
        coffee2.add("2.99");
        coffeeList.add(coffee2);

        ArrayList<String> coffee3 = new ArrayList<>();
        coffee3.add("Costa Coffee");
        coffee3.add("Coffee");
        coffee3.add("3.20");
        coffeeList.add(coffee3);

        ArrayList<String> coffee4 = new ArrayList<>();
        coffee4.add("Tim Hortons");
        coffee4.add("Coffee");
        coffee4.add("2.75");
        coffeeList.add(coffee4);

        ArrayList<String> coffee5 = new ArrayList<>();
        coffee5.add("Cafe Nero");
        coffee5.add("Coffee");
        coffee5.add("3.10");
        coffeeList.add(coffee5);
    }

    // Regular Parsing
    @Test
    public void defaultParser() throws FactoryFailureException, IOException {
        String groceryCSVFilePath = "data/user/grocery.csv";
        File groceryFile = new File(groceryCSVFilePath);
        FileReader groceryReader = new FileReader(groceryFile);
        CreatorFromRow rowCreator = new RowCreator();
        CSVParser<List<String>> groceryParse = new CSVParser(groceryReader, rowCreator);
        Assert.assertEquals(groceryList, groceryParse.getParseArray());
    }

    // Different Reader Parsing
    @Test
    public void readerParser() throws IOException, FactoryFailureException {
        String groceryCSVString =
                "Item,Category,Price\n"
                        + "Apple,Fruit,1.99\n"
                        + "Banana,Fruit,0.99\n"
                        + "Carrot,Vegetable,0.50\n"
                        + "Milk,Dairy,2.49";
        StringReader groceryReader = new StringReader(groceryCSVString);
        CreatorFromRow rowCreator = new RowCreator();
        CSVParser<List<String>> groceryParse = new CSVParser(groceryReader, rowCreator);
        Assert.assertEquals(groceryList, groceryParse.getParseArray());
    }

    // Empty File Reader
    @Test
    public void emptyParser() throws IOException, FactoryFailureException {
        String emptyCSVFilePath = "data/user/empty.csv";
        File emptyFile = new File(emptyCSVFilePath);
        FileReader emptyReader = new FileReader(emptyFile);
        CreatorFromRow rowCreator = new RowCreator();
        CSVParser<List<String>> emptyParse = new CSVParser(emptyReader, rowCreator);
        Assert.assertTrue(emptyParse.getParseArray().isEmpty());
    }

    // Invalid File Reader
    @Test
    public void invalidParser() {
        String invalidCSVFilePath = "data/does/not/exist.csv";
        File invalidFile = new File(invalidCSVFilePath);
        assertThrows(
                FileNotFoundException.class,
                () -> {
                    FileReader invalidReader = new FileReader(invalidFile);
                    CreatorFromRow rowCreator = new RowCreator();
                    new CSVParser<>(invalidReader, rowCreator);
                });
    }

    // Without Column Header
    @Test
    public void withoutHeaerParser() throws IOException, FactoryFailureException {
        String withoutCSVFilePath = "data/user/without.csv";
        File withoutFile = new File(withoutCSVFilePath);
        FileReader withoutReader = new FileReader(withoutFile);
        CreatorFromRow rowCreator = new RowCreator();
        CSVParser<List<String>> withoutParse = new CSVParser(withoutReader, rowCreator);
        Assert.assertEquals(coffeeList, withoutParse.getParseArray());
    }

    // Outside Data Folder Data
    @Test
    public void outsideProtectedFile() {
        String outsideCSVFilePath = "outside/does/not/exist.csv";
        File outsideFile = new File(outsideCSVFilePath);
        assertThrows(
                FileNotFoundException.class,
                () -> {
                    FileReader outsideReader = new FileReader(outsideFile);
                    CreatorFromRow rowCreator = new RowCreator();
                    new CSVParser<>(outsideReader, rowCreator);
                });
    }

    // Inconsistent Columns
    @Test
    public void inconsistentColumn() throws IOException, FactoryFailureException {
        String inconsistentFileName = "data/malformed/malformed_signs.csv";
        File inconsistentFile = new File(inconsistentFileName);
        FileReader inconsistentReader = new FileReader(inconsistentFile);
        CreatorFromRow rowCreator = new RowCreator();
        try {
            new CSVParser(inconsistentReader, rowCreator);
            Assert.fail("Expected IndexOutOfBoundsException was not thrown");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Inconsistent number of columns in CSV file", e.getMessage());
        }
    }

    // Test The String Creator
    @Test
    public void stringCreator() throws IOException, FactoryFailureException {
        String groceryCSVFilePath = "data/user/grocery.csv";
        File groceryFile = new File(groceryCSVFilePath);
        FileReader groceryReader = new FileReader(groceryFile);
        CreatorFromRow stringCreator = new StringCreator();
        CSVParser<List<String>> groceryParse = new CSVParser(groceryReader, stringCreator);

        List<String> expectedList =
                List.of(
                        "Item,Category,Price",
                        "Apple,Fruit,1.99",
                        "Banana,Fruit,0.99",
                        "Carrot,Vegetable,0.50",
                        "Milk,Dairy,2.49");
        Assert.assertEquals(expectedList, groceryParse.getParseArray());
    }

    // Test The String Creator
    @Test
    public void integerCreator() throws IOException, FactoryFailureException {
        String groceryCSVFilePath = "data/user/grocery.csv";
        File groceryFile = new File(groceryCSVFilePath);
        FileReader groceryReader = new FileReader(groceryFile);
        CreatorFromRow integerCreator = new IntegerCreator();
        CSVParser<List<String>> groceryParse = new CSVParser(groceryReader, integerCreator);

        List<Integer> expectedList = List.of(3, 3, 3, 3, 3);
        Assert.assertEquals(expectedList, groceryParse.getParseArray());
    }

    // Test the Student Obejct
    @Test
    public void studentCreator() throws IOException, FactoryFailureException {
        String studentCSVFilePath = "data/user/student.csv";
        File studentFile = new File(studentCSVFilePath);
        FileReader studentReader = new FileReader(studentFile);
        CreatorFromRow studentCreator = new StudentCreator();
        CSVParser<Student> studentParse = new CSVParser(studentReader, studentCreator);

        Student student1 = new Student("john", "20", "computer science");

        Assert.assertTrue(student1.studentEquals(studentParse.getParseArray().get(0)));
    }
}