package com.automation.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Assigns {@link RetryAnalyzer} to every {@code @Test} method at annotation-processing time, so
 * retry behaviour applies framework-wide without annotating individual tests.
 *
 * <p>Registered as a suite-level {@code <listener>} (not {@code @Listeners}) since
 * IAnnotationTransformer must be active before TestNG processes {@code @Test} annotations.
 */
public class RetryTransformer implements IAnnotationTransformer {

  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    annotation.setRetryAnalyzer(RetryAnalyzer.class);
  }
}
