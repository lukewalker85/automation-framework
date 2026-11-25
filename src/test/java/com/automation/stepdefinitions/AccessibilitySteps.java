package com.automation.stepdefinitions;

import com.automation.accessibility.AccessibilityChecker;
import com.automation.base.BaseTest;
import io.cucumber.java.en.Then;

/** Cucumber step definitions for accessibility scenarios. */
public class AccessibilitySteps {

  private final BaseTest baseTest;

  public AccessibilitySteps(BaseTest baseTest) {
    this.baseTest = baseTest;
  }

  /**
   * Runs axe-core on the current page and fails on blocking violations.
   *
   * @param page the page name, matched against the page column of the allowlist
   */
  @Then("the {word} page should have no blocking accessibility violations")
  public void thePageShouldHaveNoBlockingAccessibilityViolations(String page) {
    AccessibilityChecker.forDriver(baseTest.getDriver(), baseTest.getConfigReader()).check(page);
  }
}
