package com.automation.listeners;

import com.automation.utils.ConfigReader;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failing test up to {@code RETRY_COUNT} times (see config.properties). Applied to every
 * {@code @Test} method framework-wide via {@link RetryTransformer}, so individual tests don't need
 * to declare a retryAnalyzer themselves.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);
  private static final int DEFAULT_RETRY_COUNT = 0;
  private static final ConfigReader CONFIG = new ConfigReader("config.properties");

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
      LOG.warn("Retrying {} - attempt {} of {}", result.getName(), attempt, maxRetryCount);
      return true;
    }
    return false;
  }
}
