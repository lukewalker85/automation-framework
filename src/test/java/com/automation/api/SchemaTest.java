package com.automation.api;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tests for Schemas */
class SchemaTest {

  private static final Logger LOG = LoggerFactory.getLogger(SchemaTest.class);

  @Test
  void shouldThrowExceptionWhenSchemaContainsTypeMismatch() {
    LOG.info("Testing type mismatch schema");
    String invalidJson =
        """
        {
          "bookingid": 1,
          "booking": {
            "firstname": "Jim",
            "lastname": "Brown",
            "totalprice": "one hundred",
            "depositpaid": true,
            "bookingdates": {
              "checkin": "2024-01-01",
              "checkout": "2025-01-01"
            }
          }
        }
        """;
    assertThatThrownBy(
            () ->
                assertThat(
                    invalidJson, matchesJsonSchemaInClasspath("schemas/create-booking.json")))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("does not match any allowed primitive type");
  }
}
