package com.automation.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Manages Auth Tokens */
public class AuthTokenManager {

  private AuthTokenManager() {
    // Utility class - prevent instantiation
  }

  private static final Logger LOG = LoggerFactory.getLogger(AuthTokenManager.class);

  /** Gets auth token using spec from RequestSpecificationFactory */
  public static String getAuthToken(String username, String password, RequestSpecification spec) {
    if (username == null
        || password == null
        || spec == null
        || username.isEmpty()
        || password.isEmpty()) {
      throw new IllegalArgumentException("username, password or spec cannot be null");
    }
    LOG.info("Retrieving auth token");
    return RestAssured.given()
        .spec(spec)
        .body(Map.of("username", username, "password", password))
        .when()
        .post("/auth")
        .then()
        .statusCode(200)
        .extract()
        .path("token");
  }
}
