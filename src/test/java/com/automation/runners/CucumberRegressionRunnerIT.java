package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/** Cucumber TestNG runner for all regression tests. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.automation.stepdefinitions",
    tags = "@smoke or @regression",
    plugin = {
      "pretty",
      "html:target/cucumber-regression-reports.html",
      "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true)
public class CucumberRegressionRunnerIT extends AbstractTestNGCucumberTests {

  /** Provides Cucumber scenarios as a TestNG DataProvider with parallel execution enabled. */
  @Override
  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    return super.scenarios();
  }
}
