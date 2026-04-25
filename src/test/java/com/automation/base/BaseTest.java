package com.automation.base;

import com.automation.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
    WebDriver webDriver;

    LOG.info("Starting browser: {} (Headless: {})", browser, configReader.get("HEADLESS"));
    switch (browser) {
      case "chrome":
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (isHeadless()) {
          chromeOptions.addArguments("--headless");
        }
        webDriver = new ChromeDriver(chromeOptions);
        break;

      case "edge":
        WebDriverManager.edgedriver().setup();
        EdgeOptions edgeOptions = new EdgeOptions();
        if (isHeadless()) {
          edgeOptions.addArguments("--headless");
        }
        webDriver = new EdgeDriver(edgeOptions);
        break;
      case "firefox":
      default:
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (isHeadless()) {
          firefoxOptions.addArguments("--headless");
        }
        webDriver = new FirefoxDriver(firefoxOptions);
        break;
    }

    webDriver.manage().window().maximize();
    driver.set(webDriver);
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

  /** Checks headless argument for browser */
  public boolean isHeadless() {
    return "true".equalsIgnoreCase(configReader.get("HEADLESS"));
  }
}
