package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.pages.ProductDetailsPage;
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
  private InventoryPage inventoryPage;
  private ProductDetailsPage productDetailsPage;

  private String productName;
  private String productPrice;

  public ProductDetailsSteps(BaseTest baseTest) {
    this.baseTest = baseTest;
  }

  @Given("a product is clicked")
  public void a_product_is_clicked() {
    getInventoryPage().clickFirstItemName();
  }

  @When("the product name and price is stored")
  public void the_product_name_and_price_is_stored() {
    productName = getInventoryPage().getFirstItemName();
    productPrice = getInventoryPage().getFirstItemPrice();
    LOG.info("Stored values - Product name: {} Product Price: {}", productName, productPrice);
  }

  @When("the back button is clicked")
  public void the_back_button_is_clicked() {
    getProductDetailsPage().clickBackButton();
  }

  @Then("they are on the details page for the clicked product")
  public void they_are_on_the_details_page_for_the_clicked_product() {
    String productNameDetailsPage = getProductDetailsPage().getProductName();
    LOG.info("Product name found: {}", productNameDetailsPage);
    assertThat(productNameDetailsPage).isEqualTo(productName);
  }

  @Then("the product name and price match on details page")
  public void the_product_name_and_price_match_on_details_page() {
    String productNameDetailsPage = getProductDetailsPage().getProductName();
    String productPriceDetailsPage = getProductDetailsPage().getProductPrice();
    LOG.info(
        "Values found on details page - Product name: {} Product Price: {}",
        productNameDetailsPage,
        productPriceDetailsPage);
    assertThat(productNameDetailsPage).isEqualTo(productName);
    assertThat(productPriceDetailsPage).isEqualTo(productPrice);
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
