package edu.brown.cs.student.main.csv.handler;

import edu.brown.cs.student.main.csv.parser.CSVParser;
import edu.brown.cs.student.main.server.RequestHandler;
import spark.Request;
import spark.Response;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class LoadCSVHandler implements RequestHandler {
    private static List<List<String>> data; // Store the CSV data
    private static Boolean loaded = false;
    public LoadCSVHandler() {
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String filePath = request.queryParams("filepath");
        System.out.println(1);

        // Set the restricted directory path.
        String restrictedDirectory = "data";
        if (filePath == null) {
            response.status(400);
            return "Loading A CSV must take in a file path";
        }

        // Checks for the file to be in the correct directory
        String[] filePathSplit = filePath.split("/");

        // Check that the file is within the restricted directory
        if (!filePathSplit[0].equals(restrictedDirectory)) {
            response.status(400); // BAD REQUEST
            return "Cannot access files outside the /data directory";
        }

        //Check that the file exists as well
        File file = new File(filePath);
        if (!file.exists()) {
            response.status(400);
            return "File does not exist";
        }

        // initialize the parser and read in the current csv file path
        char delimiter = ',';
        // delimiter for CSV's with double quotes.
        char doubleQuotes = '"';

        // parses the data into a list of strings
        CSVParser<List<String>> parser = new CSVParser<>(new FileReader(filePath), row -> row, delimiter, doubleQuotes, true);

        // stores the parsed data
        this.data = parser.parse();

        // uses these variables for other handlers
        loaded = true;
        return "CSV Loaded With Status: " + loaded.toString();
    }

    /**
     * Getting the data
     */
    public List<List<String>> getData(){
        return this.data;
    }

    public Boolean getLoaded() {
        return this.loaded;
    }
}
