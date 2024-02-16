package edu.brown.cs.student.api_tests.mocks;

/**
 * Interface representing a percentage census data source.
 * Implementing classes are responsible for providing percentage census data for a given state and county.
 */
public interface PercentageCensusDatasource {
    /**
     * Retrieves percentage census data for the specified state and county.
     *
     * @param state   The state for which data is requested.
     * @param country The county for which data is requested.
     * @return Percentage census data for the specified state and county.
     * @throws IllegalArgumentException if the state or county is invalid or unsupported.
     */
    PercentageCensusData getStateCounty(String state, String country) throws IllegalArgumentException;

}
