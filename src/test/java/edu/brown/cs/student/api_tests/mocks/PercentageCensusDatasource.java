package edu.brown.cs.student.api_tests.mocks;

public interface PercentageCensusDatasource {
    PercentageCensusData getStateCounty(String state, String country) throws IllegalArgumentException;

}
