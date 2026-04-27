package com.automation.reporting;

import com.automation.base.BaseTest;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** TestNG Listener that captures a screenshot on failure */
public class ScreenshotListener implements ITestListener {

  private final ScreenshotStore screenshotStore;
  private static final Logger LOG = LoggerFactory.getLogger(ScreenshotListener.class);

  /** No-arg constructor for TestNG registration. Saves to target/screenshots. */
  public ScreenshotListener() {
    this(new LocalScreenshotStore(LocalScreenshotStore.DEFAULT_DIR));
  }

  /** Constructor for dependency injection in tests. */
  public ScreenshotListener(ScreenshotStore screenshotStore) {
    this.screenshotStore = screenshotStore;
  }

  @Override
  public void onTestFailure(ITestResult result) {

    if (!(result.getInstance() instanceof BaseTest baseTest)) {
      LOG.warn("Instance not BaseTest");
      return;
    }

    WebDriver driver = baseTest.getDriver();

    if (driver == null) {
      LOG.warn("Driver is null");
      return;
    }

    byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    String filename = ScreenshotStore.buildFileName(result.getName());
    try {
      screenshotStore.storeScreenshot(screenshotBytes, filename);
    } catch (IOException e) {
      LOG.error("Failed to save screenshot: {}", filename, e);
    }
  }
}
