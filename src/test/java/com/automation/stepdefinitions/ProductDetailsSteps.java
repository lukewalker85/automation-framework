package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.pages.ProductDetailsPage;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber step definitions for product details feature scenarios. Maps Gherkin steps to actions on
 * the ProductDetailsPage.
 */
public class ProductDetailsSteps {

  private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsSteps.class);

  private final BaseTest baseTest;
  private final ScenarioContext scenarioContext;
  private InventoryPage inventoryPage;
  private ProductDetailsPage productDetailsPage;

  public ProductDetailsSteps(BaseTest baseTest, ScenarioContext scenarioContext) {
    this.baseTest = baseTest;
    this.scenarioContext = scenarioContext;
  }

  @Given("a product is clicked")
  public void aProductIsClicked() {
    getInventoryPage().clickFirstItemName();
  }

  // Step must run before aProductIsClicked
  @When("the product name and price are stored")
  public void theProductNameAndPriceIsStored() {
    scenarioContext.setProductName(getInventoryPage().getFirstItemName());
    scenarioContext.setProductPrice(getInventoryPage().getFirstItemPrice());
    LOG.info(
        "Stored values - Product name: {} Product Price: {}",
        scenarioContext.getProductName(),
        scenarioContext.getProductPrice());
  }

  @When("the back button is clicked")
  public void theBackButtonIsClicked() {
    getProductDetailsPage().clickBackButton();
  }

  @Then("they are on the details page for the clicked product")
  public void theyAreOnTheDetailsPageForTheClickedProduct() {
    String productNameDetailsPage = getProductDetailsPage().getProductName();
    LOG.info("Product name found: {}", productNameDetailsPage);
    assertThat(productNameDetailsPage).isEqualTo(scenarioContext.getProductName());
  }

  @Then("the product name and price match on details page")
  public void theProductNameAndPriceMatchOnDetailsPage() {
    String productNameDetailsPage = getProductDetailsPage().getProductName();
    String productPriceDetailsPage = getProductDetailsPage().getProductPrice();
    LOG.info(
        "Values found on details page - Product name: {} Product Price: {}",
        productNameDetailsPage,
        productPriceDetailsPage);
    assertThat(productNameDetailsPage).isEqualTo(scenarioContext.getProductName());
    assertThat(productPriceDetailsPage).isEqualTo(scenarioContext.getProductPrice());
  }

  private InventoryPage getInventoryPage() {
    if (inventoryPage == null) {
      inventoryPage = new InventoryPage(baseTest.getDriver());
    }
    return inventoryPage;
  }

  private ProductDetailsPage getProductDetailsPage() {
    if (productDetailsPage == null) {
      productDetailsPage = new ProductDetailsPage(baseTest.getDriver());
    }
    return productDetailsPage;
  }
}
