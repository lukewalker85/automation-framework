package com.automation.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automation.utils.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;


public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("BROWSER").toLowerCase();
        WebDriver webDriver;

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;
            case "firefox":
            default:
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;
        }

        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}