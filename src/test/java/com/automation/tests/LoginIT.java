package com.automation.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.testdata.LoginErrorMessages;
import com.automation.testdata.LoginScenario;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginIT extends BaseTest {

  private static final ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();

  @BeforeMethod
  public void setUpPage() {
    getDriver().get(getConfigReader().get("BASE_URL"));
    loginPage.set(new LoginPage(getDriver()));
  }

  @Test
  public void successfulLoginTest() {
    loginPage
        .get()
        .login(getConfigReader().get("STANDARD_USER"), getConfigReader().get("PASSWORD"));
    loginPage
        .get()
        .waitForUrl(getConfigReader().get("BASE_URL") + getConfigReader().get("INVENTORY_PATH"));
  }

  @Test(dataProvider = "failedLoginData")
  public void failedLoginTest(LoginScenario scenario) {
    loginPage.get().login(scenario.username(), scenario.password());
    assertThat(loginPage.get().getErrorMessage()).contains(scenario.expectedError());
  }

  @DataProvider(name = "failedLoginData")
  public Object[][] failedLoginData() {
    return new Object[][] {
      {
        new LoginScenario(
            "locked out user",
            getConfigReader().get("LOCKED_OUT_USER"),
            getConfigReader().get("PASSWORD"),
            LoginErrorMessages.LOCKED_OUT)
      },
      {
        new LoginScenario(
            "invalid password",
            getConfigReader().get("STANDARD_USER"),
            getConfigReader().get("INVALID_PASSWORD"),
            LoginErrorMessages.INVALID_CREDENTIALS)
      }
    };
  }

  @AfterMethod(alwaysRun = true)
  public void tearDownPage() {
    loginPage.remove();
  }
}
