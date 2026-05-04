package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.CartPage;
import com.automation.pages.CheckoutCompletePage;
import com.automation.pages.CheckoutStepOnePage;
import com.automation.pages.CheckoutStepTwoPage;
import com.automation.utils.ConfigReader;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber step definitions for checkout scenarios. Maps Gherkin steps to actions on the
 * checkoutStepOnePage, checkoutStepTwoPage and CheckoutCompletePage.
 */
public class CheckoutSteps {

  private static final Logger LOG = LoggerFactory.getLogger(CheckoutSteps.class);

  private final BaseTest baseTest;
  private final ScenarioContext scenarioContext;
  private final ConfigReader configReader;
  private CheckoutStepOnePage checkoutStepOnePage;
  private CheckoutStepTwoPage checkoutStepTwoPage;

  public CheckoutSteps(BaseTest baseTest, ScenarioContext scenarioContext) {
    this.baseTest = baseTest;
    this.scenarioContext = scenarioContext;
    this.configReader = baseTest.getConfigReader();
  }

  @When("the checkout button is clicked")
  public void theCheckoutButtonIsClicked() {
    LOG.info("Clicking checkout button");
    new CartPage(baseTest.getDriver()).clickCheckoutButton();
  }

  @When("the shipping details are entered")
  public void theShippingDetailsAreEntered() {
    LOG.info("Entering shipping info");
    getCheckoutStepOnePage().enterFirstName(configReader.get("FIRST_NAME"));
    getCheckoutStepOnePage().enterLastName(configReader.get("LAST_NAME"));
    getCheckoutStepOnePage().enterPostcode(configReader.get("POSTCODE"));
  }

  @When("the continue button is clicked")
  public void theContinueButtonIsClicked() {
    LOG.info("Clicking continue button");
    getCheckoutStepOnePage().clickContinueButton();
  }

  @When("the finish button is clicked")
  public void theFinishButtonIsClicked() {
    LOG.info("Clicking finish button");
    getCheckoutStepTwoPage().clickFinishButton();
  }

  @When("the product name and price should match on checkout")
  public void theProductNameAndPriceShouldMatchOnCheckout() {
    String productNameCheckoutPage = getCheckoutStepTwoPage().getProductName();
    String productPriceCheckoutPage = getCheckoutStepTwoPage().getProductPrice();
    LOG.info(
        "Values found on checkout step two page - Product name: {} Product Price: {}",
        productNameCheckoutPage,
        productPriceCheckoutPage);
    assertThat(productNameCheckoutPage).isEqualTo(scenarioContext.getProductName());
    assertThat(productPriceCheckoutPage).isEqualTo(scenarioContext.getProductPrice());
  }

  @Then("they should see a first name is required error message")
  public void theyShouldSeeAFirstNameIsRequiredErrorMessage() {
    String errorMessage = getCheckoutStepOnePage().getErrorMessage();
    LOG.info("Error message found: {}", errorMessage);
    assertThat(errorMessage).contains("First Name is required");
  }

  @Then("they should be presented with the order complete message")
  public void theyShouldBePresentedWithTheOrderCompleteMessage() {
    String orderCompleteMessage =
        new CheckoutCompletePage(baseTest.getDriver()).getOrderCompleteMessage();
    LOG.info("Order complete message found: {}", orderCompleteMessage);
    assertThat(orderCompleteMessage).contains("Thank you for your order!");
  }

  private CheckoutStepOnePage getCheckoutStepOnePage() {
    if (checkoutStepOnePage == null) {
      checkoutStepOnePage = new CheckoutStepOnePage(baseTest.getDriver());
    }
    return checkoutStepOnePage;
  }

  private CheckoutStepTwoPage getCheckoutStepTwoPage() {
    if (checkoutStepTwoPage == null) {
      checkoutStepTwoPage = new CheckoutStepTwoPage(baseTest.getDriver());
    }
    return checkoutStepTwoPage;
  }
}
