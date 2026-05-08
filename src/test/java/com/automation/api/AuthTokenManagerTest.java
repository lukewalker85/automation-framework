package com.automation.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Unit tests for AuthTokenManager */
class AuthTokenManagerTest {

  @Test
  void shouldThrowIllegalArgumentExceptionIfUsernameIsNull() {
    assertThatThrownBy(
            () ->
                AuthTokenManager.getAuthToken(
                    null,
                    "test",
                    RequestSpecificationFactory.buildRequestSpec("https://example.com")))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfUsernameIsEmpty() {
    assertThatThrownBy(
            () ->
                AuthTokenManager.getAuthToken(
                    "", "", RequestSpecificationFactory.buildRequestSpec("https://example.com")))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
