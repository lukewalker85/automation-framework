package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo product details page. */
public class ProductDetailsPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsPage.class);

  @FindBy(className = "inventory_details_name")
  private WebElement productName;

  @FindBy(className = "inventory_details_price")
  private WebElement productPrice;

  @FindBy(id = "back-to-products")
  private WebElement backBtn;

  public ProductDetailsPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Gets product name from details page */
  public String getProductName() {
    LOG.info("Getting product name from details page");
    return getText(productName);
  }

  /** Gets product price from details page */
  public String getProductPrice() {
    LOG.info("Getting product price from details page");
    return getText(productPrice);
  }

  /** Clicks back from details page */
  public void clickBackButton() {
    LOG.info("Clicking back button from details page");
    click(backBtn);
  }
}
