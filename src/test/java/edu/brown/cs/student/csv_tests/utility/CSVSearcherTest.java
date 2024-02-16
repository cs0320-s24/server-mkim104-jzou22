package edu.brown.cs.student.csv_tests.utility;

import edu.brown.cs.student.main.csv.errorhandler.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CSVParser;
import edu.brown.cs.student.main.csv.parser.RowCreator;
import edu.brown.cs.student.main.csv.search.CSVSearcher;
import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * A test class to test the different inputs for the search class
 */
public class CSVSearcherTest {
  // Search Default
  @Test
  public void searchDefault() throws IOException, FactoryFailureException {
    String searchFileName = "data/census/dol_ri_earnings_disparity.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "2%", "Employed Percent", true, "N");

    // Initialize the expected result list
    List<List<String>> expected =
            List.of(List.of("RI", "Multiracial", "$971.89", "8883.049171", "$0.92", "2%"));
    // Assert that the result matches the expected list
    Assert.assertEquals(expected, searcher.getResult());
  }

  // Search For Not Exist
  @Test
  public void searchNotExist() throws IOException, FactoryFailureException {
    String searchFileName = "data/census/dol_ri_earnings_disparity.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "2.2222222%", "Employed Percent", true, "N");

    // Assert that the result is null
    Assert.assertEquals(List.of(), searcher.getResult());
  }

  // Wrong Column Identifier
  @Test
  public void searchWrongColumn() throws IOException, FactoryFailureException {
    String searchFileName = "data/census/dol_ri_earnings_disparity.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "2%", "State", true, "N");

    // Assert that the result matches the expected list
    Assert.assertEquals(List.of(), searcher.getResult());
  }

  // Search Column Index
  @Test
  public void searchIndex() throws IOException, FactoryFailureException {
    String searchFileName = "data/stars/stardata.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "5.36884", "4", true, "I");

    // Initialize the expected result list
    List<List<String>> expected =
            List.of(List.of("1", "Andreas", "282.43485", "0.00449", "5.36884"));
    // Assert that the result matches the expected list
    Assert.assertEquals(expected, searcher.getResult());
  }

  // Search Column Name
  @Test
  public void searchName() throws IOException, FactoryFailureException {
    String searchFileName = "data/stars/stardata.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "5.36884", "Z", true, "N");

    // Initialize the expected result list
    List<List<String>> expected =
            List.of(List.of("1", "Andreas", "282.43485", "0.00449", "5.36884"));
    // Assert that the result matches the expected list
    Assert.assertEquals(expected, searcher.getResult());
  }

  // No Column Identifier
  @Test
  public void searchNoID() throws IOException, FactoryFailureException {
    String searchFileName = "data/stars/stardata.csv";
    File searchFile = new File(searchFileName);
    FileReader searchReader = new FileReader(searchFile);
    RowCreator rowCreator = new RowCreator();
    CSVParser<List<String>> searchCSV = new CSVParser<>(searchReader, rowCreator);
    CSVSearcher searcher = new CSVSearcher(searchCSV.getParseArray(), "5.36884", "", false, "");

    // Initialize the expected result list
    List<List<String>> expected =
            List.of(List.of("1", "Andreas", "282.43485", "0.00449", "5.36884"));
    // Assert that the result matches the expected list
    Assert.assertEquals(expected, searcher.getResult());
  }
}