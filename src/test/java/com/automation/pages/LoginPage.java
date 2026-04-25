package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page object for the SauceDemo login page. Provides methods to log in and retrieve error messages.
 */
public class LoginPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);

  @FindBy(id = "user-name")
  private WebElement usernameField;

  @FindBy(id = "password")
  private WebElement passwordField;

  @FindBy(id = "login-button")
  private WebElement loginButton;

  @FindBy(css = ".error-message-container")
  private WebElement errorMessage;

  public LoginPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Inputs provided username and password and clicks the login button. */
  public void login(String username, String password) {
    LOG.info("Logging in with username: {}", username);
    type(usernameField, username);
    type(passwordField, password);
    click(loginButton);
  }

  /** Returns error message when login fails. */
  public String getErrorMessage() {
    return getText(errorMessage);
  }
}
