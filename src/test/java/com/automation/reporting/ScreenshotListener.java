package com.automation.reporting;

import com.automation.base.BaseTest;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    this(new LocalScreenshotStore(Path.of("target/screenshots")));
  }

  /** Constructor for dependency injection in tests. */
  public ScreenshotListener(ScreenshotStore screenshotStore) {
    this.screenshotStore = screenshotStore;
  }

  /** Builds filename for screenshot on failure */
  public static String buildFileName(String testName) {
    return testName.replaceAll("[^a-zA-Z0-9\\-]", "_")
        + "_"
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
        + ".png";
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
    String filename = buildFileName(result.getName());
    try {
      screenshotStore.storeScreenshot(screenshotBytes, filename);
    } catch (IOException e) {
      LOG.error("Failed to save screenshot: {}", filename, e);
    }
  }
}
