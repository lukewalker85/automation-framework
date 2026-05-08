package com.automation.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Creates Request Specification for APIs */
public class RequestSpecificationFactory {

  private static final Logger LOG = LoggerFactory.getLogger(RequestSpecificationFactory.class);

  private RequestSpecificationFactory() {
    // Utility class - prevent instantiation
  }

  /** Builds a Request Specification that contains a base URI, content type and accepts */
  public static RequestSpecification buildRequestSpec(String baseUri) {
    if (baseUri == null || baseUri.isEmpty()) {
      throw new IllegalArgumentException("baseUri path cannot be null or empty");
    }
    LOG.info("Creating Request Specification, URI: {}", baseUri);
    return RestAssured.given()
        .baseUri(baseUri)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON);
  }
}
