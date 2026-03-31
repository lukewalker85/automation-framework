package com.automation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        getDriver().get(ConfigReader.get("BASE_URL"));
        loginPage = new LoginPage(getDriver());
    }

    @Test
    public void successfulLoginTest() {
        loginPage.login(ConfigReader.get("STANDARD_USER"), ConfigReader.get("PASSWORD"));
        assertThat(getDriver().getCurrentUrl()).isEqualTo(ConfigReader.get("BASE_URL") + ConfigReader.get("INVENTORY_PATH"));
    }

    @Test
    public void invalidLoginTest() {
        loginPage.login(ConfigReader.get("LOCKED_OUT_USER"), ConfigReader.get("PASSWORD"));
        assertThat(loginPage.getErrorMessage()).contains("Sorry, this user has been locked out");
    }
 }