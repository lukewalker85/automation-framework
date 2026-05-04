package com.automation.utils;

/** Stores variables for sharing between cucumber steps */
public class ScenarioContext {

  private String productName;
  private String productPrice;

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}
