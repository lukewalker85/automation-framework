package com.automation.listeners;

import com.automation.utils.ConfigReader;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failing test up to {@code RETRY_COUNT} times (see the active environment's config file,
 * per {@link ConfigReader#forEnvironment()}). Applied to every {@code @Test} method framework-wide
 * via {@link RetryTransformer}, so individual tests don't need to declare a retryAnalyzer
 * themselves.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);
  private static final int DEFAULT_RETRY_COUNT = 0;
  private static final ConfigReader CONFIG = ConfigReader.forEnvironment();

  private final int maxRetryCount;
  private final AtomicInteger attempts = new AtomicInteger(0);

  public RetryAnalyzer() {
    this(CONFIG.getInt("RETRY_COUNT", DEFAULT_RETRY_COUNT));
  }

  RetryAnalyzer(int maxRetryCount) {
    this.maxRetryCount = maxRetryCount;
  }

  @Override
  public boolean retry(ITestResult result) {
    int attempt = attempts.incrementAndGet();
    if (attempt <= maxRetryCount) {
      String failureReason = describe(result);
      LOG.warn(
          "Retrying {} - attempt {} of {}, previous failure: {}",
          result.getName(),
          attempt,
          maxRetryCount,
          failureReason);
      if (Allure.getLifecycle().getCurrentTestCaseOrStep().isPresent()) {
        Allure.step(
            String.format(
                "Retry %d/%d: previous attempt failed with '%s' - retrying",
                attempt, maxRetryCount, failureReason),
            Status.FAILED);
      }
      return true;
    }
    return false;
  }

  private static String describe(ITestResult result) {
    Throwable throwable = result.getThrowable();
    if (throwable == null) {
      return "unknown failure";
    }
    String message = throwable.getMessage();
    return message == null ? throwable.getClass().getSimpleName() : message;
  }
}
