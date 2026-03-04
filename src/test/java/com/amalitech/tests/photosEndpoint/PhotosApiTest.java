package com.amalitech.tests.photosEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Photos Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PhotosApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Photos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /photos returns a 200 status with a non-empty JSON array")
    void testGetAllPhotos() {
        given()
                .when()
                .get("/photos")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/photo-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Photos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /photos/1 returns the correct photo with all required fields")
    void testGetSinglePhoto() {
        given()
                .when()
                .get("/photos/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("albumId", notNullValue())
                .body("title", notNullValue())
                .body("url", startsWith("http"))
                .body("thumbnailUrl", startsWith("http"))
                .body(matchesJsonSchemaInClasspath("schemas/photo-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Create Photo")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /photos creates a new photo and returns 201 with the created resource")
    void testCreatePhoto() {
        given()
                .body(TestData.createPhotoBody())
                .when()
                .post("/photos")
                .then()
                .statusCode(201)
                .body("albumId", equalTo(1))
                .body("title", equalTo("AmaliTech Test Photo"))
                .body("url", startsWith("http"))
                .body("thumbnailUrl", startsWith("http"))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/photo-schema.json"));
    }

    @Test
    @Order(4)
    @Story("Update Photo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /photos/1 updates the photo and returns the updated fields")
    void testUpdatePhoto() {
        given()
                .body(TestData.updatePhotoBody())
                .when()
                .put("/photos/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Photo Title"))
                .body("url", startsWith("http"))
                .body("thumbnailUrl", startsWith("http"))
                .body(matchesJsonSchemaInClasspath("schemas/photo-schema.json"));
    }

    @Test
    @Order(5)
    @Story("Delete Photo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /photos/1 returns 200 and an empty body")
    void testDeletePhoto() {
        given()
                .when()
                .delete("/photos/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(6)
    @Story("Filter Photos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /photos?albumId=1 returns only photos belonging to albumId 1")
    void testGetPhotosByQueryParam() {
        given()
                .queryParam("albumId", 1)
                .when()
                .get("/photos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("albumId", everyItem(equalTo(1)));
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(7)
    @Story("Negative - Fetch Photos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /photos/99999 returns 404 for a non-existent photo")
    void testGetNonExistentPhoto() {
        given()
                .when()
                .get("/photos/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    @Story("Negative - Fetch Photos")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /photos/abc returns 404 for an invalid non-numeric ID")
    void testGetPhotoWithInvalidId() {
        given()
                .when()
                .get("/photos/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(9)
    @Story("Negative - Create Photo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /photos with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreatePhotoWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/photos")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(10)
    @Story("Negative - Update Photo")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent photo /photos/99999 returns 500")
    void testUpdateNonExistentPhoto() {
        given()
                .body(TestData.updatePhotoBody())
                .when()
                .put("/photos/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(11)
    @Story("Negative - Delete Photo")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /photos/99999 returns 404 for a non-existent photo")
    void testDeleteNonExistentPhoto() {
        given()
                .when()
                .delete("/photos/99999")
                .then()
                .statusCode(404);
    }
}