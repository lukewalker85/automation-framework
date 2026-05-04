package com.automation.pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo cart page. */
public class CartPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(CartPage.class);

  @FindBy(className = "inventory_item_name")
  private List<WebElement> itemNameList;

  public CartPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Clicks the remove button for the specified item */
  public void clickRemoveButton(String itemName) {
    LOG.debug("Clicking remove for item: {}", itemName);
    String locator = "remove-" + itemName.toLowerCase().replace(" ", "-").trim();
    wait.until(ExpectedConditions.elementToBeClickable(By.id(locator))).click();
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locator)));
  }

  /** Returns list of displayed item names */
  public List<String> getItemNameList() {
    List<String> list = new ArrayList<>();
    for (WebElement element : itemNameList) {
      list.add(element.getText());
    }
    return list;
  }

  public void waitForItemNames() {
    wait.until(ExpectedConditions.visibilityOfAllElements(itemNameList));
  }
}
