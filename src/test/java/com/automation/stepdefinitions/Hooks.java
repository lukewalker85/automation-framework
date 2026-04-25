package com.automation.stepdefinitions;

import com.automation.base.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber lifecycle hooks. Manages browser setup and teardown around each scenario by delegating
 * to BaseTest.
 */
public class Hooks {

  private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

  private final BaseTest baseTest;

  public Hooks(BaseTest baseTest) {
    this.baseTest = baseTest;
  }

  /** Sets up driver before cucumber test */
  @Before
  public void setUp(Scenario scenario) {
    LOG.info("Starting scenario: {}", scenario.getName());
    baseTest.setUp();
  }

  /** Closes down driver once cucumber test has completed */
  @After
  public void tearDown(Scenario scenario) {
    baseTest.tearDown();
    LOG.info("Finished scenario: {}", scenario.getName());
  }
}
