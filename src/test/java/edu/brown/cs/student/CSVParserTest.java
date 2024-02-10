package edu.brown.cs.student;

import static org.junit.Assert.*;

import edu.brown.cs.student.main.creators.AnimalCreator;
import edu.brown.cs.student.main.creators.ClothesCreator;
import edu.brown.cs.student.main.csvparser.CSVParser;
import edu.brown.cs.student.main.domain.Animal;
import edu.brown.cs.student.main.domain.Clothes;
import edu.brown.cs.student.main.errorhandler.FactoryFailureException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;

public class CSVParserTest {

  @Test
  public void testParseAnimalsWithoutHeaders() throws IOException, FactoryFailureException {
    try (FileReader reader = new FileReader("data/tests/animals_no_headers.csv")) {
      CSVParser<Animal> parser = new CSVParser<>(reader, new AnimalCreator(), ',', '"', false);
      List<Animal> animals = parser.parse();
      assertEquals(3, animals.size());
      assertEquals("Dog", animals.get(0).getName());
      assertEquals("Labrador", animals.get(0).getSpecies());
    }
  }

  @Test
  public void testParseClothesWithoutHeaders() throws IOException, FactoryFailureException {
    try (FileReader reader = new FileReader("data/tests/clothes_no_headers.csv")) {
      CSVParser<Clothes> parser = new CSVParser<>(reader, new ClothesCreator(), ',', '"', false);
      List<Clothes> clothes = parser.parse();
      assertEquals(3, clothes.size());
      assertEquals("T-Shirt", clothes.get(0).getName());
    }
  }

  @Test
  public void testCSVWithDifferentReaderTypes() throws IOException, FactoryFailureException {
    String csvContent = "1,Dog,Labrador\n2,Cat,Siamese";
    try (StringReader stringReader = new StringReader(csvContent)) {
      CSVParser<Animal> parser =
          new CSVParser<>(stringReader, new AnimalCreator(), ',', '"', false);
      List<Animal> animals = parser.parse();
      assertNotNull(animals);
      assertEquals(2, animals.size());
    }
  }

  @Test(expected = FactoryFailureException.class)
  public void testCSVWithInconsistentColumnCount() throws IOException, FactoryFailureException {
    String filePathInconsistent = "data/tests/animals_inconsistent_col.csv";
    try (FileReader fileReader = new FileReader(filePathInconsistent)) {
      CSVParser<Animal> parser = new CSVParser<>(fileReader, new AnimalCreator(), ',', '"', false);
      parser.parse();
    }
  }

  @Test(expected = FactoryFailureException.class)
  public void testCSVOutsideProtectedDirectory() throws IOException, FactoryFailureException {
    String filePathOutside = "external_tests/outside.csv";
    try (FileReader fileReader = new FileReader(filePathOutside)) {
      CSVParser<Animal> parser = new CSVParser<>(fileReader, new AnimalCreator(), ',', '"', false);
      parser.parse();
    }
  }

  @Test
  public void testWithHeaders() throws Exception {
    String csvData = "Name,Age\nLatrice,30\nBobette,25";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row, ',', '"', true);
    List<List<String>> results = parser.parse();
    assertEquals("Expected 3 rows of data, including header", 3, results.size());
    assertEquals("Latrice", results.get(1).get(0));
    assertEquals("30", results.get(1).get(1));
  }

  @Test
  public void testWithoutHeaders() throws Exception {
    String csvData = "Latrice,30\nBobette,25";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row, ',', '"', false);
    List<List<String>> results = parser.parse();
    assertEquals("Expected 2 rows of data", 2, results.size());
    assertEquals("Latrice", results.get(0).get(0));
    assertEquals("30", results.get(0).get(1));
  }


  @Test(expected = IOException.class)
  public void testFileNotFound() throws Exception {
    new CSVParser<>(new FileReader("nonexistent.csv"), row -> row, ',', '"', true).parse();
  }
}
