package com.automation.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.equalTo;

import com.automation.base.BaseApiTest;
import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tests for Auth */
class AuthTest extends BaseApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(AuthTest.class);

  @Test
  void shouldReturnTokenForValidCredentials() {
    LOG.info("Testing login with valid credentials");
    getMockServer()
        .stubFor(
            post(urlEqualTo("/auth/login"))
                .withRequestBody(
                    equalToJson("{\"username\":\"validUser\",\"password\":\"validPass\"}"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"abc123\"}")));

    RestAssured.given()
        .spec(getRequestSpecification())
        .body(Map.of("username", "validUser", "password", "validPass"))
        .when()
        .post("/auth/login")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .body("token", equalTo("abc123"));
  }

  @Test
  void shouldReturn401ForInvalidCredentials() {
    LOG.info("Testing login with invalid credentials");
    getMockServer()
        .stubFor(
            post(urlEqualTo("/auth/login"))
                .withRequestBody(containing("wrongUser"))
                .willReturn(
                    aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Invalid credentials\"}")));

    RestAssured.given()
        .spec(getRequestSpecification())
        .body(Map.of("username", "wrongUser", "password", "validPass"))
        .when()
        .post("/auth/login")
        .then()
        .statusCode(401)
        .contentType("application/json")
        .body("error", equalTo("Invalid credentials"));
  }
}
