package com.automation.base;

import com.automation.utils.ConfigReader;
import com.automation.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for all test classes. Manages the WebDriver lifecycle and provides access to shared
 * configuration.
 */
public class BaseTest {

  private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
  private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
  private final ConfigReader configReader = new ConfigReader("config.properties");

  public WebDriver getDriver() {
    return driver.get();
  }

  public ConfigReader getConfigReader() {
    return configReader;
  }

  /** Sets up driver before test */
  @BeforeMethod
  public void setUp() {
    String configuredBrowser = configReader.get("BROWSER");
    String browser = configuredBrowser == null ? "firefox" : configuredBrowser.toLowerCase();
    boolean headless = "true".equalsIgnoreCase(configReader.get("HEADLESS"));
    driver.set(DriverFactory.createDriver(browser, headless));
  }

  /** Closes down driver once test has completed */
  @AfterMethod(alwaysRun = true)
  public void tearDown() {
    WebDriver currentDriver = driver.get();
    try {
      if (currentDriver != null) {
        currentDriver.quit();
        LOG.info("Browser closed");
      }
    } finally {
      driver.remove();
    }
  }
}
