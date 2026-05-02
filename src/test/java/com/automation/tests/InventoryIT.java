package com.automation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import com.automation.testdata.InventoryScenario;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** TestNG integration tests for the inventory page. */
public class InventoryIT extends BaseTest {

  private static final ThreadLocal<InventoryPage> inventoryPage = new ThreadLocal<>();
  private static final String ADD_TO_CART_PRODUCT = "Sauce Labs Backpack";

  @BeforeMethod
  public void loginToSauceDemoSite() {
    getDriver().get(getConfigReader().get("BASE_URL"));
    LoginPage loginPage = new LoginPage(getDriver());
    loginPage.login(getConfigReader().get("STANDARD_USER"), getConfigReader().get("PASSWORD"));
    loginPage.waitForUrl(
        getConfigReader().get("BASE_URL") + getConfigReader().get("INVENTORY_PATH"));
    inventoryPage.set(new InventoryPage(getDriver()));
  }

  @Test
  public void aProductIsDisplayed() {
    assertThat(inventoryPage.get().getProductCount()).isNotZero();
  }

  @Test
  public void theCartBadgeDisplaysTheNumberOfItemsInIt() {
    inventoryPage.get().clickAddToCart(ADD_TO_CART_PRODUCT);
    assertThat(inventoryPage.get().getCartBadgeNumber()).isNotZero();
  }

  @Test(dataProvider = "sortOptionsData")
  public void sortInventoryTest(InventoryScenario inventoryScenario) {
    inventoryPage.get().selectSortOption(inventoryScenario.sortOption());
    assertThat(inventoryPage.get().getFirstItemName()).isEqualTo(inventoryScenario.firstItem());
    assertThat(inventoryPage.get().getLastItemName()).isEqualTo(inventoryScenario.lastItem());
  }

  @DataProvider(name = "sortOptionsData")
  public Object[][] sortOptionsData() {
    return new Object[][] {
      {
        new InventoryScenario(
            "Name (A to Z)", "Sauce Labs Backpack", "Test.allTheThings() T-Shirt (Red)")
      },
      {
        new InventoryScenario(
            "Name (Z to A)", "Test.allTheThings() T-Shirt (Red)", "Sauce Labs Backpack")
      },
      {
        new InventoryScenario(
            "Price (low to high)", "Sauce Labs Onesie", "Sauce Labs Fleece Jacket")
      },
      {
        new InventoryScenario(
            "Price (high to low)", "Sauce Labs Fleece Jacket", "Sauce Labs Onesie")
      }
    };
  }

  @AfterMethod(alwaysRun = true)
  public void tearDownPage() {
    inventoryPage.remove();
  }
}
