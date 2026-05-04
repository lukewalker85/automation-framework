package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo checkout step one page. */
public class CheckoutStepOnePage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(CheckoutStepOnePage.class);

  @FindBy(id = "first-name")
  private WebElement firstNameInput;

  @FindBy(id = "last-name")
  private WebElement lastNameInput;

  @FindBy(id = "postal-code")
  private WebElement postcodeInput;

  @FindBy(id = "continue")
  private WebElement continueBtn;

  @FindBy(className = "error-message-container")
  private WebElement errorMessageContainer;

  public CheckoutStepOnePage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Enters first name */
  public void enterFirstName(String firstName) {
    LOG.info("Entering first name: {}", firstName);
    type(firstNameInput, firstName);
  }

  /** Enters last name */
  public void enterLastName(String lastName) {
    LOG.info("Entering last name: {}", lastName);
    type(lastNameInput, lastName);
  }

  /** Enters postcode */
  public void enterPostcode(String postcode) {
    LOG.info("Entering postcode: {}", postcode);
    type(postcodeInput, postcode);
  }

  /** Clickss the continue button */
  public void clickContinueButton() {
    LOG.info("Clicking continue button");
    click(continueBtn);
  }

  /** Returns the error message found from the checkout step one page */
  public String getErrorMessage() {
    String errorMessage = getText(errorMessageContainer);
    LOG.info("Error message found: {}", errorMessage);
    return errorMessage;
  }
}
