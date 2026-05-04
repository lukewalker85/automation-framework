package com.automation.utils;

/** Stores variables for sharing between cucumber steps */
public class ScenarioContext {

  private String productName;

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}
