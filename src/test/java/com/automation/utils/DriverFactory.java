package com.automation.utils;

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

/** Creates configured WebDriver instances for supported browsers */
public class DriverFactory {

  private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

  private DriverFactory() {
    // Utility class - prevent instantiation
  }

  /** Creates a WebDriver for the specified browser, optionally in headless mode */
  public static WebDriver createDriver(String browser, boolean headless) {

    WebDriver webDriver;

    LOG.info("Starting browser: {} (Headless: {})", browser, headless);
    switch (browser) {
      case "chrome" -> {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (headless) {
          chromeOptions.addArguments("--headless=new");
        }
        webDriver = new ChromeDriver(chromeOptions);
      }

      case "edge" -> {
        WebDriverManager.edgedriver().setup();
        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
          edgeOptions.addArguments("--headless=new");
        }
        webDriver = new EdgeDriver(edgeOptions);
      }
      case "firefox" -> {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (headless) {
          firefoxOptions.addArguments("--headless");
        }
        webDriver = new FirefoxDriver(firefoxOptions);
      }
      default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
    }

    webDriver.manage().window().maximize();
    return webDriver;
  }
}
