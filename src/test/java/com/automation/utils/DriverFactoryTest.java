package com.automation.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Unit tests for DriverFactory browser creation */
class DriverFactoryTest {

  @Test
  void shouldThrowExceptionForUnsupportedBrowser() {
    assertThatThrownBy(() -> DriverFactory.createDriver("invalidbrowser", false))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("invalidbrowser");
  }
}
