package edu.brown.cs.student.api_tests.mocks;

import edu.brown.cs.student.main.api.PercentageCensusData;
import edu.brown.cs.student.main.api.PercentageCensusDatasource;

public class MockedACSPercentageCensusAPISource implements PercentageCensusDatasource {
    private final PercentageCensusData data;

    public MockedACSPercentageCensusAPISource(PercentageCensusData data) {
        this.data = data;
    }

    @Override
    public PercentageCensusData getStateCounty(String state, String country) throws IllegalArgumentException {
        return data;
    }
}
