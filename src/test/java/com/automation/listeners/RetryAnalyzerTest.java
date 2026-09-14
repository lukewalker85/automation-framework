package com.automation.listeners;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testng.ITestResult;
import org.testng.internal.TestResult;

/** Unit tests for RetryAnalyzer */
class RetryAnalyzerTest {

  @Test
  void shouldRetryUntilMaxRetryCountReached() {
    RetryAnalyzer retryAnalyzer = new RetryAnalyzer(2);
    ITestResult result = TestResult.newEmptyTestResult();

    assertThat(retryAnalyzer.retry(result)).isTrue();
    assertThat(retryAnalyzer.retry(result)).isTrue();
    assertThat(retryAnalyzer.retry(result)).isFalse();
  }

  @Test
  void shouldNotRetryWhenMaxRetryCountIsZero() {
    RetryAnalyzer retryAnalyzer = new RetryAnalyzer(0);
    ITestResult result = TestResult.newEmptyTestResult();

    assertThat(retryAnalyzer.retry(result)).isFalse();
  }

  @Test
  void shouldKeepReturningFalseOnceExhausted() {
    RetryAnalyzer retryAnalyzer = new RetryAnalyzer(1);
    ITestResult result = TestResult.newEmptyTestResult();

    assertThat(retryAnalyzer.retry(result)).isTrue();
    assertThat(retryAnalyzer.retry(result)).isFalse();
    assertThat(retryAnalyzer.retry(result)).isFalse();
  }
}
