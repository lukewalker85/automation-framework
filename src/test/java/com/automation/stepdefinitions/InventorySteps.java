package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Cucumber step definitions for inventory feature scenarios. Maps Gherkin steps to actions on the
 * InventoryPage.
 */
public class InventorySteps {

  private final BaseTest baseTest;
  private final ScenarioContext scenarioContext;
  private InventoryPage inventoryPage;

  public InventorySteps(BaseTest baseTest, ScenarioContext scenarioContext) {
    this.baseTest = baseTest;
    this.scenarioContext = scenarioContext;
  }

  /** Selects the specified sort option from the filter dropdown. */
  @When("the filter is changed to {string}")
  public void theFilterIsChangedTo(String sortOption) {
    getInventoryPage().selectSortOption(sortOption);
  }

  /** Clicks to add the specified item to cart */
  @When("the add to cart button is clicked for {string}")
  public void anItemIsAddedToTheCart(String item) {
    scenarioContext.setProductName(item);
    getInventoryPage().clickAddToCart(item);
  }

  /** Clicks the cart icon */
  @When("the cart icon is clicked")
  public void theCartIconIsClicked() {
    getInventoryPage().clickCartIcon();
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
    assertThat(getInventoryPage().getCartBadgeNumber()).isEqualTo(1);
  }

  private InventoryPage getInventoryPage() {
    if (inventoryPage == null) {
      inventoryPage = new InventoryPage(baseTest.getDriver());
    }
    return inventoryPage;
  }
}
