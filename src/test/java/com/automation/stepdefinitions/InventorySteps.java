package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber step definitions for inventory feature scenarios. Maps Gherkin steps to actions on the
 * InventoryPage.
 */
public class InventorySteps {

  private static final Logger LOG = LoggerFactory.getLogger(InventorySteps.class);
  private final BaseTest baseTest;
  private InventoryPage inventoryPage;

  private static final String ADD_TO_CART_PRODUCT = "Sauce Labs Backpack";

  public InventorySteps(BaseTest baseTest) {
    this.baseTest = baseTest;
  }

  /** Selects the specified sort option from the filter dropdown. */
  @When("the filter is changed to {string}")
  public void theFilterIsChangedTo(String sortOption) {
    getInventoryPage().selectSortOption(sortOption);
  }

  /** Clicks to add item to cart */
  @When("an item is added to the Cart")
  public void anItemIsAddedToTheCart() {
    getInventoryPage().clickAddToCart(ADD_TO_CART_PRODUCT);
  }

  /** Asserts the number of products displayed is not zero */
  @Then("a product is displayed")
  public void aProductIsDisplayed() {
    assertThat(getInventoryPage().getProductCount()).isNotZero();
  }

  /** Asserts the first and last item matches the input strings */
  @Then("the first item is {string} and the last item is {string}")
  public void theFirstItemIsAndTheLastItemIs(String firstItem, String lastItem) {
    assertThat(getInventoryPage().getFirstItemName()).isEqualTo(firstItem);
    assertThat(getInventoryPage().getLastItemName()).isEqualTo(lastItem);
  }

  /** Asserts the cart badge number is not zero */
  @Then("the cart badge displays the number of items in it")
  public void theCartBadgeDisplaysTheNumberOfItemsInIt() {
    assertThat(getInventoryPage().getCartBadgeNumber()).isNotZero();
  }

  private InventoryPage getInventoryPage() {
    if (inventoryPage == null) {
      inventoryPage = new InventoryPage(baseTest.getDriver());
    }
    return inventoryPage;
  }
}
