package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.CartPage;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber step definitions for cart feature scenarios. Maps Gherkin steps to actions on the
 * CartPage.
 */
public class CartSteps {

  private static final Logger LOG = LoggerFactory.getLogger(CartSteps.class);

  private final BaseTest baseTest;
  private final ScenarioContext scenarioContext;
  private CartPage cartPage;

  public CartSteps(BaseTest baseTest, ScenarioContext scenarioContext) {
    this.baseTest = baseTest;
    this.scenarioContext = scenarioContext;
  }

  @When("the remove button is clicked")
  public void theRemoveButtonIsClicked() {
    LOG.info("Clicking remove for item: {}", scenarioContext.getProductName());
    getCartPage().clickRemoveButton(scenarioContext.getProductName());
  }

  @Then("the item should be displayed on the cart page")
  public void theItemShouldBeDisplayedOnTheCartPage() {
    getCartPage().waitForItemNames();
    assertThat(getCartPage().getItemNameList()).contains(scenarioContext.getProductName());
  }

  @Then("the item should be removed from the cart page")
  public void theItemShouldBeRemovedFromTheCartPage() {
    assertThat(getCartPage().getItemNameList()).isEmpty();
  }

  @Then("there should be two items on the cart page")
  public void thereShouldBeTwoItemsOnTheCartPage() {
    getCartPage().waitForItemNames();
    List<String> items = getCartPage().getItemNameList();
    LOG.info("Number of items on cart page: {}", items.size());
    assertThat(items).hasSize(2);
  }

  private CartPage getCartPage() {
    if (cartPage == null) {
      cartPage = new CartPage(baseTest.getDriver());
    }
    return cartPage;
  }
}
