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

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("BROWSER").toLowerCase();

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "firefox":
            default:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
        }

        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}