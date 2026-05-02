package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/** Cucumber TestNG runner for all feature files. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.automation.stepdefinitions",
    plugin = {"pretty", "html:target/cucumber-reports.html"},
    monochrome = true)
public class CucumberRunnerIT extends AbstractTestNGCucumberTests {

  /** Provides Cucumber scenarios as a TestNG DataProvider with parallel execution enabled. */
  @Override
  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    return super.scenarios();
  }
}
