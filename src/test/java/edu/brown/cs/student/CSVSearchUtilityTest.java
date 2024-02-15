//package edu.brown.cs.student;
//
//import static org.junit.Assert.*;
//
//import edu.brown.cs.student.main.csv.search.CSVSearch;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class CSVSearchUtilityTest {
//
// private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
// private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
// private final PrintStream originalOut = System.out;
// private final PrintStream originalErr = System.err;
//
// @Before
// public void setUpStreams() {
//   System.setOut(new PrintStream(outContent)); // Capture System.out
//   System.setErr(new PrintStream(errContent)); // Capture System.err
// }
//
// @After
// public void restoreStreams() {
//   System.setOut(originalOut); // Restore System.out
//   System.setErr(originalErr); // Restore System.err
// }
//
// @Test
// public void testConstructor_validFile() {
//   String validFileName = "data/census/income_by_race.csv";
//   CSVSearch csvSearch = new CSVSearch(validFileName, true);
//   assertNotNull(csvSearch);
// }
//
// @Test
// public void testConstructor_invalidDirectory() {
//   String invalidDirectoryFileName = "external_tests/outside.csv";
//   new CSVSearch(invalidDirectoryFileName, true);
//   String expectedOutput = "Access to the file is restricted";
//   assertTrue(
//       "Error message not printed as expected for restricted directory",
//       errContent.toString().contains(expectedOutput));
// }
//
// @Test
// public void testConstructor_fileNonExist() {
//   String nonExistentFileName = "data/fake.csv";
//   new CSVSearch(nonExistentFileName, true);
//   String expectedOutput = "Error: File not found - " + nonExistentFileName;
//   assertTrue(
//       "Error message not printed as expected for file not found",
//       errContent.toString().contains(expectedOutput));
// }
//
// @Test
// public void testValidColumnSearch() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/tests/animals_headers.csv", true);
//   csvSearch.search("Fox", "Name");
//   System.out.println(outContent.toString());
//   assertTrue("Expected search output to contain 'Fox'", outContent.toString().contains("Fox"));
// }
//
// @Test
// public void testSearchWithoutColumnIdentifier() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/tests/animals_headers.csv", true);
//   csvSearch.search("Fox", null); // Assuming "Lion" is a value present in any column
//   assertTrue("Expected search output to contain 'Fox'", outContent.toString().contains("Fox"));
// }
//
// @Test
// public void testInvalidColumnIndexSearch() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/tests/animals_headers.csv", true);
//   csvSearch.search("Fox", "100"); // Assuming 100 is an out-of-bounds index
//   assertTrue(
//       "Expected 'No match found.' message for invalid column index",
//       outContent.toString().contains("No match found."));
// }
//
// @Test
// public void testValidColumnIndexSearch() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/tests/animals_headers.csv", true);
//   csvSearch.search("Fox", "1");
//   System.out.println(outContent.toString());
//   assertTrue("Expected search output to contain 'Fox'", outContent.toString().contains("Fox"));
// }
//
// @Test
// public void testNonExistentValueSearch() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/census/income_by_race.csv", true);
//   csvSearch.search("NonExistentValue", "ID Year");
//   assertTrue(
//       "Expected 'No match found.' message", outContent.toString().contains("No match found."));
// }
//
// @Test
// public void testCaseSensitivitySearch() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/census/income_by_race.csv", true);
//   csvSearch.search("total", "Race");
//   assertTrue(
//       "Expected output to contain case-insensitive match",
//       outContent.toString().toLowerCase().contains("total"));
// }
//
// @Test
// public void testSearchValueInWrongColumn() throws Exception {
//   CSVSearch csvSearch = new CSVSearch("data/tests/animals_headers.csv", true);
//   csvSearch.search("Giraffe", "Species");
//   assertTrue(
//       "Expected 'No match found.' message since 'Giraffe' is not in the 'Species' column",
//       outContent.toString().contains("No match found."));
// }
//}
