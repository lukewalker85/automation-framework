package com.automation.stepdefinitions;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Cucumber step definitions for common feature scenarios. */
public class CommonSteps {

  private static final Logger LOG = LoggerFactory.getLogger(CommonSteps.class);
  private final BaseTest baseTest;
  private final ConfigReader config;

  public CommonSteps(BaseTest baseTest) {
    this.baseTest = baseTest;
    this.config = baseTest.getConfigReader();
  }

  /** Logs into the saucedemo site with valid details */
  @Given("the user is logged in")
  public void theUserIsLoggedIn() {
    String baseUrl = config.get("BASE_URL");
    LOG.info("Navigating to base page. URL: {}", baseUrl);
    baseTest.getDriver().get(baseUrl);
    LoginPage loginPage = new LoginPage(baseTest.getDriver());
    loginPage.login(config.get("STANDARD_USER"), config.get("PASSWORD"));
    loginPage.waitForUrl(config.get("BASE_URL") + config.get("INVENTORY_PATH"));
  }
}
