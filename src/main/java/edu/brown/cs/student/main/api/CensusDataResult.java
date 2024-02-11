package edu.brown.cs.student.main.api;

public class CensusDataResult {
  private String name;
  private String state;
  private String county;
  private double broadbandAccessPercentage;
  private String retrievalDateTime;

  // Getters
  public String getName() {
    return name;
  }

  public String getState() {
    return state;
  }

  public String getCounty() {
    return county;
  }

  public double getBroadbandAccessPercentage() {
    return broadbandAccessPercentage;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setCounty(String county) {
    this.county = county;
  }

  public String getRetrievalDateTime() {
    return retrievalDateTime;
  }

  public void setRetrievalDateTime(String retrievalDateTime) {
    this.retrievalDateTime = retrievalDateTime;
  }

  public void setBroadbandAccessPercentage(double broadbandAccessPercentage) {
    this.broadbandAccessPercentage = broadbandAccessPercentage;
  }
}
