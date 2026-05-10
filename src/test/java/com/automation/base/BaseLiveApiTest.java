package com.automation.base;

import com.automation.api.RequestSpecificationFactory;
import com.automation.utils.ConfigReader;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

/** Base class for all live api test classes. */
public class BaseLiveApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(BaseLiveApiTest.class);
  private final ConfigReader configReader = new ConfigReader("config.properties");

  private RequestSpecification spec;

  protected RequestSpecification getRequestSpecification() {
    return spec;
  }

  protected ConfigReader getConfigReader() {
    return configReader;
  }

  /**
   * Sets up RequestSpecification using the base URI from config. Overrides the Accept header as
   * restful-booker requires an explicit content type. Overrides filters to use request and response
   * logging filters
   */
  @BeforeClass
  public void setup() {
    LOG.info("Creating RequestSpecification");
    spec =
        RequestSpecificationFactory.buildRequestSpec(configReader.get("BASE_API_URI"))
            .accept("application/json")
            .filter(new RequestLoggingFilter())
            .filter(new ResponseLoggingFilter());
  }
}
