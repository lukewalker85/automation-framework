package com.automation.pages;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all page objects. Provides common element interaction methods with explicit waits.
 */
public class BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

  protected WebDriver driver;
  protected WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  /** Waits for the element to be clickable, then clicks it. */
  protected void click(WebElement element) {
    LOG.debug("Clicking element: {}", element);
    wait.until(ExpectedConditions.elementToBeClickable(element));
    element.click();
  }

  /** Waits for the element to be visible, clears it, then types the given text. */
  protected void type(WebElement element, String text) {
    LOG.debug("Typing into element: {}", element);
    wait.until(ExpectedConditions.visibilityOf(element));
    element.clear();
    element.sendKeys(text);
  }

  /** Waits for the element to be visible, then returns its text content. */
  protected String getText(WebElement element) {
    LOG.debug("Getting text from element: {}", element);
    wait.until(ExpectedConditions.visibilityOf(element));
    return element.getText();
  }
}
