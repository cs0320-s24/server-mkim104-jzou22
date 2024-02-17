> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details

Project Name: Server 
Team Members: Moon Hwan Kim (mkim104) and John Zou (jzou22)
Note: There Was Two Git Commits From Akang806. This Was Due To John Zou's Roomate Who Needed Access GitHub From His
Laptop And Forgot To Change It. Apologies :)
Link: https://github.com/cs0320-s24/server-mkim104-jzou22

This Java-based application interacts with the Census API to retrieve broadband data. It comprises components
for handling HTTP requests, fetching data from the Census API, caching retrieved data, and processing CSV files.
The main components include API Handlers responsible for HTTP request handling and interfacing with the Census API,
a Caching System utilizing an ACSDataCache for efficient data caching, CSV Handlers for parsing and searching
through census data stored in CSV files, and a simple HTTP server implemented using Spark framework. The 
BroadbandDataHandler class handles broadband-related requests, leveraging the CensusApiAdapter to fetch relevant data. 
The ACSDataCache provides a caching mechanism for storing and retrieving data from the Census API efficiently. 
CSV Handlers include LoadCSVHandler, ViewCSVHandler, and SearchCSVHandler for loading, viewing, and searching through
CSV files, respectively. The SparkServer initializes and starts the HTTP server, while the RequestHandler interface
defines the contract for handling HTTP requests. Unit tests in CensusApiAdapterTests ensure the correctness of
CensusApiAdapter functionalities, covering scenarios such as fetching state and county codes, handling API errors, 
and caching efficiency.
# Design Choices

In designing our program, we used an interface-driven approach to enhance flexibility, scalability, and maintainability.
One key design choice involved leveraging interfaces extensively, facilitating loose coupling between components and 
enabling seamless swapping of implementations. For instance, we employed interfaces effectively in our request handlers 
for broadband, loadcsv, searchcsv, and viewcsv. Each handler treats requests differently, but the interface allows for
uniform handling regardless of the endpoint. Similarly, our server design features an interface, providing developers
with the freedom to customize server operations such as starting and stopping. Additionally, we implemented a caching 
system using the ACSDataCache class to optimize runtime efficiency and reduce API calls. This cache efficiently stores
previously fetched data, significantly decreasing latency and enhancing overall performance.

# Errors/Bugs

There are no bugs however one thing to mention is the "log4j:WARN" when starting a server.
Not necessarily a bug but for our test suite for BroadBand and CSVHandler, you will have to uncomment 
and run the tests independently as to running them all. Reason being that starting the server then closing it
seems to be too resource intensive. There maybe another reason but we were unsure

# Tests

The testing process was divided into four main parts. Firstly, we tested the CSV Utilities, which encompass the core 
logic for searching and parsing within a CSV file. Expanding on this, we also evaluated the CSV Handler functionality
to ensure proper display and viewing of CSV data when stored locally. The remaining two tests focused on broadband data,
where we verified the accuracy of data retrieved from the Census API and its corresponding response to user queries. 
To facilitate testing, we employed mock tests, ensuring the correctness of both the broadband handler and its associated
functionalities. These four comprehensive tests provided coverage across the project, as the server revolves around two
main components: CSV and broadband data. Each component has its respective handlers, which were thoroughly tested, 
thereby ensuring the overall functionality and logic of the application.

# How to
To run the application, clone the repository, compile the Java files, and execute the Main class. You should then be 
able to make a request on the URL