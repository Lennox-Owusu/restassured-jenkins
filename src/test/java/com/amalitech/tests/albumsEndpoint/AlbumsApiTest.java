package com.amalitech.tests.albumsEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Albums Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumsApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Albums")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /albums returns a 200 status with a non-empty JSON array")
    void testGetAllAlbums() {
        given()
                .when()
                .get("/albums")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/album-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Albums")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /albums/1 returns the correct album with all required fields")
    void testGetSingleAlbum() {
        given()
                .when()
                .get("/albums/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Create Album")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /albums creates a new album and returns 201 with the created resource")
    void testCreateAlbum() {
        given()
                .body(TestData.createAlbumBody())
                .when()
                .post("/albums")
                .then()
                .statusCode(201)
                .body("title", equalTo("AmaliTech Test Album"))
                .body("userId", equalTo(1))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"));
    }

    @Test
    @Order(4)
    @Story("Update Album")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /albums/1 updates the album and returns the updated fields")
    void testUpdateAlbum() {
        given()
                .body(TestData.updateAlbumBody())
                .when()
                .put("/albums/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Album Title"))
                .body(matchesJsonSchemaInClasspath("schemas/album-schema.json"));
    }

    @Test
    @Order(5)
    @Story("Delete Album")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /albums/1 returns 200 and an empty body")
    void testDeleteAlbum() {
        given()
                .when()
                .delete("/albums/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(6)
    @Story("Filter Albums")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /albums?userId=1 returns only albums belonging to userId 1")
    void testGetAlbumsByQueryParam() {
        given()
                .queryParam("userId", 1)
                .when()
                .get("/albums")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("userId", everyItem(equalTo(1)));
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(7)
    @Story("Negative - Fetch Albums")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /albums/99999 returns 404 for a non-existent album")
    void testGetNonExistentAlbum() {
        given()
                .when()
                .get("/albums/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    @Story("Negative - Fetch Albums")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /albums/abc returns 404 for an invalid non-numeric ID")
    void testGetAlbumWithInvalidId() {
        given()
                .when()
                .get("/albums/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(9)
    @Story("Negative - Create Album")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /albums with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreateAlbumWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/albums")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(10)
    @Story("Negative - Update Album")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent album /albums/99999 returns 500")
    void testUpdateNonExistentAlbum() {
        given()
                .body(TestData.updateAlbumBody())
                .when()
                .put("/albums/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(11)
    @Story("Negative - Delete Album")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /albums/99999 returns 404 for a non-existent album")
    void testDeleteNonExistentAlbum() {
        given()
                .when()
                .delete("/albums/99999")
                .then()
                .statusCode(404);
    }
}