package org.gs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import io.quarkus.test.junit.QuarkusTest;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.*;


@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieResourceTestIT {

  @Test
  @Order(1)
  void getAll() {
    given()
      .when().get("/movies")
      .then()
         .statusCode(200);
  }

  @Test
  @Order(1)
  void getById() {
    given()
      .when().get("/movies/1")
      .then()
         .statusCode(200);
  }

  @Test
  @Order(1)
  void getByIdKO() {
    given()
      .when().get("/movies/999")
      .then()
         .statusCode(404);
  }

  @Test
  @Order(1)
  void getByTitle() {
    given()
      .when().get("/movies/title/Inception")
      .then()
         .statusCode(200);
  }

  @Test
  @Order(1)
  void getByTitleKO() {
    given()
      .when().get("/movies/title/Nonexistent")
      .then()
         .statusCode(404);
  }

  @Test
  @Order(2)
  void getByCountry() {
    given()
      .when().get("/movies/country/USA")
      .then()
         .statusCode(200);
  }

  @Test
  @Order(2)
  void getByCountryKO() {
    given()
      .when().get("/movies/country/Nonexistent")
      .then()
         .statusCode(404);
  }

  @Test
  @Order(3)
  void create() {
    JsonObject movieJson = Json.createObjectBuilder()
      .add("title", "New Movie")
      .add("director", "John Doe")
      .add("year", 2022)
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(movieJson.toString())
      .when().post("/movies")
      .then()
         .statusCode(201);
  }

  @Test
  @Order(4)
  void updateById() {
    JsonObject movieJson = Json.createObjectBuilder()
      .add("title", "Updated Movie")
      .add("director", "John Doe")
      .add("year", 2022)
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(movieJson.toString())
      .when().put("/movies/1")
      .then()
         .statusCode(200);
  }

  @Test
  @Order(4)
  void updateByIdKO() {
    JsonObject movieJson = Json.createObjectBuilder()
      .add("title", "Updated Movie")
      .add("director", "John Doe")
      .add("year", 2022)
      .build();

    given()
      .contentType(MediaType.APPLICATION_JSON)
      .body(movieJson.toString())
      .when().put("/movies/999")
      .then()
         .statusCode(404);
  }

  @Test
  @Order(5)
  void deleteById() {
    given()
      .when().delete("/movies/1")
      .then()
         .statusCode(204);
  }

  @Test
  @Order(5)
  void deleteByIdKO() {
    given()
      .when().delete("/movies/999")
      .then()
         .statusCode(404);
  }
}