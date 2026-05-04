package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo checkout step two page. */
public class CheckoutStepTwoPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(CheckoutStepTwoPage.class);

  @FindBy(className = "inventory_item_name")
  private WebElement checkoutItemName;

  @FindBy(className = "inventory_item_price")
  private WebElement checkoutItemPrice;

  @FindBy(id = "finish")
  private WebElement finishBtn;

  public CheckoutStepTwoPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Clicks the finish button */
  public void clickFinishButton() {
    LOG.info("Clicking finish button");
    click(finishBtn);
  }

  /** Gets the product name from the checkout step two page */
  public String getProductName() {
    String productName = getText(checkoutItemName).trim();
    LOG.info("Product name found: {}", productName);
    return productName;
  }

  /** Gets the product price from the checkout step two page */
  public String getProductPrice() {
    String productPrice = getText(checkoutItemPrice).trim();
    LOG.info("Product price found: {}", productPrice);
    return productPrice;
  }
}
