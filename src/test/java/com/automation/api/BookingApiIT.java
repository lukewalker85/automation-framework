package com.automation.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.automation.base.BaseLiveApiTest;
import io.restassured.RestAssured;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/** TestNG integration tests for booking api */
public class BookingApiIT extends BaseLiveApiTest {

  private static final Logger LOG = LoggerFactory.getLogger(BookingApiIT.class);

  @Test
  public void shouldReturnTokenForValidCredentials() {
    LOG.info("Requesting token with valid credentials");
    RestAssured.given()
        .spec(getRequestSpecification())
        .body(
            Map.of(
                "username",
                getConfigReader().get("API_USERNAME"),
                "password",
                getConfigReader().get("API_PASSWORD")))
        .when()
        .post("/auth")
        .then()
        .statusCode(200)
        .body("token", is(notNullValue()));
  }

  @Test
  public void shouldReturnBookingId() {
    LOG.info("Creating booking");
    RestAssured.given()
        .spec(getRequestSpecification())
        .body(buildBookingBody())
        .when()
        .post("/booking")
        .then()
        .statusCode(200)
        .body("bookingid", is(notNullValue()))
        .body("booking.firstname", equalTo("Jim"));
  }

  @Test
  public void shouldReturnBookingDetails() {
    int id = createBooking();
    LOG.info("Getting booking details from booking id: {}", id);
    RestAssured.given()
        .spec(getRequestSpecification())
        .when()
        .get("/booking/" + id)
        .then()
        .statusCode(200)
        .body("firstname", equalTo("Jim"))
        .body("lastname", equalTo("Brown"))
        .body("totalprice", equalTo(111))
        .body("depositpaid", equalTo(true))
        .body("bookingdates.checkin", equalTo("2018-01-01"))
        .body("bookingdates.checkout", equalTo("2019-01-01"))
        .body("additionalneeds", equalTo("Breakfast"));
  }

  @Test
  public void shouldUpdateBookingDetails() {
    int id = createBooking();
    String token = getAuthToken();
    LOG.info("Updating booking id: {} token: {}", id, token);
    RestAssured.given()
        .spec(getRequestSpecification())
        .cookie("token", token)
        .body(buildUpdatedBookingBody())
        .when()
        .put("/booking/" + id)
        .then()
        .statusCode(200)
        .body("firstname", equalTo("Bob"))
        .body("additionalneeds", equalTo("No Breakfast"));
  }

  @Test
  public void shouldReturn404WhenCheckingDeletedBooking() {
    int id = createBooking();
    String token = getAuthToken();
    LOG.info("Deleting booking id: {} token: {}", id, token);
    RestAssured.given()
        .spec(getRequestSpecification())
        .cookie("token", token)
        .when()
        .delete("/booking/" + id)
        .then()
        .statusCode(201);

    LOG.info("Booking deleted attempting to get details");
    RestAssured.given()
        .spec(getRequestSpecification())
        .when()
        .get("/booking/" + id)
        .then()
        .statusCode(404);
  }

  private Map<String, Object> buildBookingBody() {
    return Map.of(
        "firstname",
        "Jim",
        "lastname",
        "Brown",
        "totalprice",
        111,
        "depositpaid",
        true,
        "bookingdates",
        Map.of("checkin", "2018-01-01", "checkout", "2019-01-01"),
        "additionalneeds",
        "Breakfast");
  }

  private int createBooking() {
    LOG.info("Creating booking required for test");
    return RestAssured.given()
        .spec(getRequestSpecification())
        .body(buildBookingBody())
        .when()
        .post("/booking")
        .then()
        .statusCode(200)
        .extract()
        .path("bookingid");
  }

  private String getAuthToken() {
    LOG.info("Getting auth token required for test");
    return RestAssured.given()
        .spec(getRequestSpecification())
        .body(
            Map.of(
                "username",
                getConfigReader().get("API_USERNAME"),
                "password",
                getConfigReader().get("API_PASSWORD")))
        .when()
        .post("/auth")
        .then()
        .statusCode(200)
        .extract()
        .path("token");
  }

  private Map<String, Object> buildUpdatedBookingBody() {
    return Map.of(
        "firstname",
        "Bob",
        "lastname",
        "Brown",
        "totalprice",
        111,
        "depositpaid",
        true,
        "bookingdates",
        Map.of("checkin", "2018-01-01", "checkout", "2019-01-01"),
        "additionalneeds",
        "No Breakfast");
  }
}
