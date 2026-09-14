package com.automation.listeners;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.TestAnnotation;

/** Unit tests for RetryTransformer */
class RetryTransformerTest {

  @Test
  void shouldAssignRetryAnalyzerToEveryTestAnnotation() {
    RetryTransformer retryTransformer = new RetryTransformer();
    ITestAnnotation annotation = new TestAnnotation();

    retryTransformer.transform(annotation, null, null, null);

    assertThat(annotation.getRetryAnalyzerClass()).isEqualTo(RetryAnalyzer.class);
  }
}
