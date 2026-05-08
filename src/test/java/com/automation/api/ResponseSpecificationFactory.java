package com.automation.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Creates Response Specification for APIs */
public class ResponseSpecificationFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseSpecificationFactory.class);

  private ResponseSpecificationFactory() {
    // Utility class - prevent instantiation
  }

  /** Builds a Response Specification that contains a response code and content type */
  public static ResponseSpecification buildResponseSpec(int responseCode) {
    LOG.info("Creating Response Specification, response code: {}", responseCode);
    ResponseSpecification spec =
        RestAssured.expect().statusCode(responseCode).contentType(ContentType.JSON);
    return spec;
  }
}
