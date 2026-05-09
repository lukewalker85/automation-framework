package com.automation.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.automation.base.BaseApiTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tests for User Operations */
class UserOperationsTest extends BaseApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(UserOperationsTest.class);

  @Test
  void shouldReturnUserDetailsForValidToken() {
    LOG.info("Getting User details with valid token");
    getMockServer()
        .stubFor(
            get(urlEqualTo("/users/1"))
                .withHeader("Authorization", equalTo("Bearer abc123"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"John Doe\"}")));

    RestAssured.given()
        .spec(getRequestSpecification())
        .header("Authorization", "Bearer abc123")
        .when()
        .get("/users/1")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .body("name", Matchers.equalTo("John Doe"))
        .body("id", Matchers.equalTo(1));
  }

  @Test
  void shouldReturn403WhenTokenMissing() {
    LOG.info("Getting User details with missing token");
    getMockServer()
        .stubFor(
            get(urlEqualTo("/users/1"))
                .withHeader("Authorization", absent())
                .willReturn(
                    aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Forbidden\"}")));

    RestAssured.given()
        .spec(getRequestSpecification())
        .when()
        .get("/users/1")
        .then()
        .statusCode(403)
        .contentType("application/json")
        .body("error", Matchers.equalTo("Forbidden"));
  }

  @Test
  void shouldReturn204ForAuthorisedDelete() {
    LOG.info("Deleting User with valid token");
    getMockServer()
        .stubFor(
            delete(urlEqualTo("/users/1"))
                .withHeader("Authorization", equalTo("Bearer abc123"))
                .willReturn(aResponse().withStatus(204)));

    RestAssured.given()
        .spec(getRequestSpecification())
        .header("Authorization", "Bearer abc123")
        .when()
        .delete("/users/1")
        .then()
        .statusCode(204);
  }

  @Test
  void shouldReturn403ForUnauthorisedDelete() {
    LOG.info("Deleting User with missing token");
    getMockServer()
        .stubFor(
            delete(urlEqualTo("/users/1"))
                .withHeader("Authorization", absent())
                .willReturn(
                    aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Forbidden\"}")));

    RestAssured.given()
        .spec(getRequestSpecification())
        .when()
        .delete("/users/1")
        .then()
        .statusCode(403)
        .contentType("application/json")
        .body("error", Matchers.equalTo("Forbidden"));
  }
}
