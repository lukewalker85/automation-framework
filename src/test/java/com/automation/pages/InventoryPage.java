package com.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo inventory page. */
public class InventoryPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(InventoryPage.class);

  @FindBy(className = "product_sort_container")
  private WebElement dropDown;

  @FindBy(className = "inventory_item_name")
  private List<WebElement> itemNameList;

  @FindBy(className = "shopping_cart_badge")
  private WebElement cartBadge;

  public InventoryPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Returns number of elements with the inventory item description classname */
  public int getProductCount() {
    LOG.debug("Getting element count for items");
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_description")));
    return getElementCount(By.className("inventory_item_description"));
  }

  /** Selects provided string from filter dropdown */
  public void selectSortOption(String option) {
    LOG.debug("Selecting filter dropdown option: {}", option);
    selectDropdown(dropDown, option);
  }

  /** Gets name of first displayed item */
  public String getFirstItemName() {
    LOG.debug("Getting first item name");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
    return itemNameList.get(0).getText();
  }

  /** Gets name of last displayed item */
  public String getLastItemName() {
    LOG.debug("Getting last item name");
    return itemNameList.get(itemNameList.size() - 1).getText();
  }

  /** Clicks add to cart for the provided item name */
  public void clickAddToCart(String itemName) {
    LOG.debug("Clicking add to cart for item: {}", itemName);
    String addToCartId = "add-to-cart-" + itemName.toLowerCase().replaceAll(" ", "-");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(addToCartId)));
    driver.findElement(By.id(addToCartId)).click();
  }

  /** Gets the number of items in the cart */
  public int getCartBadgeNumber() {
    LOG.debug("Getting number on cart badge");
    if (driver.findElements(By.className("shopping_cart_badge")).isEmpty()) {
      return 0;
    }
    return Integer.parseInt(cartBadge.getText());
  }
}
