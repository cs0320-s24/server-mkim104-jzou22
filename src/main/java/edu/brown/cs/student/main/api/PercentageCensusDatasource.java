package edu.brown.cs.student.main.api;

public interface PercentageCensusDatasource {
    PercentageCensusData getStateCounty(String state, String country) throws IllegalArgumentException;

}
