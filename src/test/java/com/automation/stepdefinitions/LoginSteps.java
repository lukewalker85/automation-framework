package com.automation.stepdefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.testdata.LoginErrorMessages;
import com.automation.utils.ConfigReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class LoginSteps {

    private LoginPage loginPage;
    private final BaseTest baseTest;

    public LoginSteps(BaseTest baseTest) {
        this.baseTest = baseTest;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        baseTest.getDriver().get(ConfigReader.get("BASE_URL"));
        loginPage = new LoginPage(baseTest.getDriver());
    }

    @When("they login with valid credentials")
    public void theyLogInWithValidCredentials() {
        loginPage.login(ConfigReader.get("STANDARD_USER"), ConfigReader.get("PASSWORD"));
    }

    @When("they login with locked out credentials")
    public void theyLoginWithLockedOutCredentials() {
        loginPage.login(ConfigReader.get("LOCKED_OUT_USER"), ConfigReader.get("PASSWORD"));
    }

    @When("they login with invalid credentials")
    public void theyLoginWithInvalidCredentials() {
        loginPage.login(ConfigReader.get("STANDARD_USER"), ConfigReader.get("INVALID_PASSWORD"));
    }

    @Then("they should be redirected to the inventory page")
    public void theyShouldBeRedirectedToTheInventoryPage() {
        assertThat(baseTest.getDriver().getCurrentUrl()).isEqualTo(ConfigReader.get("BASE_URL") + ConfigReader.get("INVENTORY_PATH"));
    }

    @Then("they should see a locked out error message")
    public void theyShouldSeeALockedOutMessage() {
        assertThat(loginPage.getErrorMessage()).contains(LoginErrorMessages.LOCKED_OUT);
    }

    @Then("they should see an invalid credentials error message")
    public void theyShouldSeeAnInvalidCredentialsErrorMessage() {
        assertThat(loginPage.getErrorMessage()).contains(LoginErrorMessages.INVALID_CREDENTIALS);
    }
}