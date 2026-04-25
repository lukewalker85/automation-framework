package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.testdata.LoginErrorMessages;
import com.automation.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber step definitions for login feature scenarios. Maps Gherkin steps to actions on the
 * LoginPage.
 */
public class LoginSteps {

  private static final Logger LOG = LoggerFactory.getLogger(LoginSteps.class);
  private LoginPage loginPage;
  private final BaseTest baseTest;
  private final ConfigReader config;

  public LoginSteps(BaseTest baseTest) {
    this.baseTest = baseTest;
    this.config = baseTest.getConfigReader();
  }

  /** Navigates to the SauceDemo login page and initialises the page object. */
  @Given("the user is on the login page")
  public void theUserIsOnTheLoginPage() {
    String baseUrl = config.get("BASE_URL");
    LOG.info("Navigating to base page. URL: {}", baseUrl);
    baseTest.getDriver().get(baseUrl);
    loginPage = new LoginPage(baseTest.getDriver());
  }

  @When("they login with valid credentials")
  public void theyLogInWithValidCredentials() {
    loginPage.login(config.get("STANDARD_USER"), config.get("PASSWORD"));
  }

  @When("they login with locked out credentials")
  public void theyLoginWithLockedOutCredentials() {
    loginPage.login(config.get("LOCKED_OUT_USER"), config.get("PASSWORD"));
  }

  @When("they login with invalid credentials")
  public void theyLoginWithInvalidCredentials() {
    loginPage.login(config.get("STANDARD_USER"), config.get("INVALID_PASSWORD"));
  }

  @Then("they should be redirected to the inventory page")
  public void theyShouldBeRedirectedToTheInventoryPage() {
    String currentUrl = baseTest.getDriver().getCurrentUrl();
    LOG.info("Current URL: {}", currentUrl);
    assertThat(currentUrl).isEqualTo(config.get("BASE_URL") + config.get("INVENTORY_PATH"));
  }

  @Then("they should see a locked out error message")
  public void theyShouldSeeALockedOutMessage() {
    String errorMessage = loginPage.getErrorMessage();
    LOG.info("Error message found: {}", errorMessage);
    assertThat(errorMessage).contains(LoginErrorMessages.LOCKED_OUT);
  }

  @Then("they should see an invalid credentials error message")
  public void theyShouldSeeAnInvalidCredentialsErrorMessage() {
    String errorMessage = loginPage.getErrorMessage();
    LOG.info("Error message found: {}", errorMessage);
    assertThat(errorMessage).contains(LoginErrorMessages.INVALID_CREDENTIALS);
  }
}
