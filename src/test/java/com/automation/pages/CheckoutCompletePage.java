package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo checkout complete page. */
public class CheckoutCompletePage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(CheckoutCompletePage.class);

  @FindBy(className = "complete-header")
  private WebElement orderCompleteMessage;

  public CheckoutCompletePage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Returns the order complete message found from the checkout page */
  public String getOrderCompleteMessage() {
    String orderCompleteMessageFound = getText(orderCompleteMessage);
    LOG.info("Order complete message found: {}", orderCompleteMessageFound);
    return orderCompleteMessageFound;
  }
}
