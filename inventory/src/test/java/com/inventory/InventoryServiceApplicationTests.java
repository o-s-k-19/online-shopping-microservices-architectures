package com.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql")
class InventoryServiceApplicationTests {

  @LocalServerPort
  int port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void shouldReturnProductInStock() {
    RestAssured.given().contentType(ContentType.JSON)
        .when()
        .get(
            "/api/inventories?skuCode=Iphone_16&skuCode=PC")
        .then().statusCode(200)
        .body("[0].skuCode",
            org.hamcrest.Matchers.equalTo("Iphone_16"))
        .body("[1].availableQuantity",
            org.hamcrest.Matchers.equalTo(5))
        .body("[0].inStock",
            org.hamcrest.Matchers.equalTo(true));
  }
}
