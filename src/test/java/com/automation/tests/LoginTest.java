package com.automation.tests;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;

public class LoginTest extends BaseTest {

    private static ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();

    @BeforeMethod
    public void setUpPage() {
        getDriver().get(ConfigReader.get("BASE_URL"));
        loginPage.set(new LoginPage(getDriver()));
    }

    @Test
    public void successfulLoginTest() {
        loginPage.get().login(ConfigReader.get("STANDARD_USER"), ConfigReader.get("PASSWORD"));
        assertThat(getDriver().getCurrentUrl()).isEqualTo(ConfigReader.get("BASE_URL") + ConfigReader.get("INVENTORY_PATH"));
    }

    @Test
    public void lockedOutUserTest() {
        loginPage.get().login(ConfigReader.get("LOCKED_OUT_USER"), ConfigReader.get("PASSWORD"));
        assertThat(loginPage.get().getErrorMessage()).contains("Sorry, this user has been locked out");
    }

    @Test
    public void invalidPasswordTest() {
        loginPage.get().login(ConfigReader.get("STANDARD_USER"), ConfigReader.get("INVALID_PASSWORD"));
        assertThat(loginPage.get().getErrorMessage()).contains("Username and password do not match any user in this service");
    }
 }