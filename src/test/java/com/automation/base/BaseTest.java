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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

  private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
  private final ConfigReader configReader = new ConfigReader("config.properties");

  public WebDriver getDriver() {
    return driver.get();
  }

  public ConfigReader getConfigReader() {
    return configReader;
  }

  @BeforeMethod
  public void setUp() {
    String configuredBrowser = configReader.get("BROWSER");
    String browser = configuredBrowser == null ? "firefox" : configuredBrowser.toLowerCase();
    WebDriver webDriver;

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

  @AfterMethod(alwaysRun = true)
  public void tearDown() {
    WebDriver currentDriver = driver.get();
    try {
      if (currentDriver != null) {
        currentDriver.quit();
      }
    } finally {
      driver.remove();
    }
  }

  public boolean isHeadless() {
    return "true".equalsIgnoreCase(configReader.get("HEADLESS"));
  }
}
