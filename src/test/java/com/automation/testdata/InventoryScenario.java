package com.automation.testdata;

/** Test data record for inventory sort scenarios. */
public record InventoryScenario(String sortOption, String firstItem, String lastItem) {
  @Override
  public String toString() {
    return sortOption;
  }
}
