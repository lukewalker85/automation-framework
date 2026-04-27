package com.automation.stepdefinitions;

import com.automation.base.BaseTest;
import com.automation.reporting.LocalScreenshotStore;
import com.automation.reporting.ScreenshotListener;
import com.automation.reporting.ScreenshotStore;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.io.IOException;
import java.nio.file.Path;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber lifecycle hooks. Manages browser setup and teardown around each scenario by delegating
 * to BaseTest.
 */
public class Hooks {

  private final ScreenshotStore screenshotStore =
      new LocalScreenshotStore(Path.of("target/screenshots"));
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

  /** Takes screenshot on test failure and closes down driver once cucumber test has completed */
  @After
  public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
      captureScreenshot(scenario.getName());
    }
    try {
      baseTest.tearDown();
    } finally {
      LOG.info("Finished scenario: {}", scenario.getName());
    }
  }

  private void captureScreenshot(String scenarioName) {
    WebDriver driver = baseTest.getDriver();
    if (driver == null) {
      LOG.warn("Driver is null, skipping screenshot");
      return;
    }
    byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    String filename = ScreenshotListener.buildFileName(scenarioName);
    try {
      screenshotStore.storeScreenshot(screenshotBytes, filename);
    } catch (IOException e) {
      LOG.error("Failed to save screenshot: {}", filename, e);
    }
  }
}
