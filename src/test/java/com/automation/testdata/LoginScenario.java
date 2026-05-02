package com.automation.testdata;

/** Test data record for login scenarios. */
public record LoginScenario(String name, String username, String password, String expectedError) {
  @Override
  public String toString() {
    return name;
  }
}
