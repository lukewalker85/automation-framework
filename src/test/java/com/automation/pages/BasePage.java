package com.automation.pages;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

  protected WebDriver driver;
  protected WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  protected void click(WebElement element) {
    wait.until(ExpectedConditions.elementToBeClickable(element));
    element.click();
  }

  protected void type(WebElement element, String text) {
    wait.until(ExpectedConditions.visibilityOf(element));
    element.clear();
    element.sendKeys(text);
  }

  protected String getText(WebElement element) {
    wait.until(ExpectedConditions.visibilityOf(element));
    return element.getText();
  }
}
