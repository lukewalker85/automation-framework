package com.automation.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for RequestSpecificationFactory */
class RequestSpecificationFactoryTest {

  private QueryableRequestSpecification queryable;

  @BeforeEach
  void setQueryableRequestSpecification() {
    RequestSpecification spec = RequestSpecificationFactory.buildRequestSpec("https://example.com");
    queryable = SpecificationQuerier.query(spec);
  }

  @Test
  void shouldSetBaseUri() {
    assertThat(queryable.getBaseUri()).isEqualTo("https://example.com");
  }

  @Test
  void shouldSetContentType() {
    assertThat(queryable.getContentType()).isEqualTo("application/json");
  }

  @Test
  void shouldSetAcceptType() {
    assertThat(queryable.getHeaders().getValue("Accept")).contains("application/json");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfBaseUriIsNull() {
    assertThatThrownBy(() -> RequestSpecificationFactory.buildRequestSpec(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldThrowIllegalArgumentExceptionIfBaseUriIsEmpty() {
    assertThatThrownBy(() -> RequestSpecificationFactory.buildRequestSpec(""))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
