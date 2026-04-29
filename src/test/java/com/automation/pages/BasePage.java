package com.automation.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

  /** Checks if element is visible. Returns false if not displayed within the wait timeout. */
  protected boolean isDisplayed(WebElement element) {
    LOG.debug("Checking if element is displayed: {}", element);
    try {
      wait.until(ExpectedConditions.visibilityOf(element));
    } catch (TimeoutException | StaleElementReferenceException e) {
      return false;
    }
    return true;
  }

  /** Waits for URL to exactly match the expected URL. */
  public void waitForUrl(String expectedUrl) {
    LOG.debug("Waiting for URL to be: {}", expectedUrl);
    wait.until(ExpectedConditions.urlToBe(expectedUrl));
  }

  /** Waits for dropdown and selects option by visible text */
  protected void selectDropdown(WebElement element, String option) {
    LOG.debug("Selecting option from dropdown: {}", option);
    wait.until(ExpectedConditions.visibilityOf(element));
    new Select(element).selectByVisibleText(option);
  }

  /** Waits for an element to be invisible */
  protected void waitForInvisibility(By locator) {
    LOG.debug("Waiting for locator to disappear: {}", locator);
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
  }

  /** Gets the count of elements by locator on the page */
  protected int getElementCount(By locator) {
    LOG.debug("Counting number of elements by locator: {}", locator);
    return driver.findElements(locator).size();
  }
}
