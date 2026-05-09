package com.automation.base;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.automation.api.RequestSpecificationFactory;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Base class for all mocked api test classes. Manages the Wiremock server lifecycle */
public class BaseApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(BaseApiTest.class);

  private static WireMockServer wireMockServer;
  private static RequestSpecification spec;

  protected WireMockServer getMockServer() {
    return wireMockServer;
  }

  protected RequestSpecification getRequestSpecification() {
    return spec;
  }

  /** Sets up Wiremock server and RequestSpecification using servers baseUrl before all tests */
  @BeforeAll
  static void setUp() {
    LOG.info("Starting Wiremock server");
    wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    wireMockServer.start();
    spec = RequestSpecificationFactory.buildRequestSpec(wireMockServer.baseUrl());
  }

  /** Clears stubs before each test */
  @BeforeEach
  void clearStubs() {
    wireMockServer.resetAll();
  }

  /** Stops Wiremock server once all tests are complete */
  @AfterAll
  static void tearDown() {
    if (wireMockServer != null) {
      LOG.info("Stopping Wiremock server");
      wireMockServer.stop();
    }
  }
}
