package com.amalitech.tests.postsEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Posts Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostsApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Posts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /posts returns a 200 status with a non-empty JSON array")
    void testGetAllPosts() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/post-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Posts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /posts/1 returns the correct post with all required fields")
    void testGetSinglePost() {
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Create Post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /posts creates a new post and returns 201 with the created resource")
    void testCreatePost() {
        given()
                .body(TestData.createPostBody())
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("AmaliTech Test Post"))
                .body("body", equalTo("This is a test post body"))
                .body("userId", equalTo(1))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test
    @Order(4)
    @Story("Update Post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /posts/1 updates the post and returns the updated fields")
    void testUpdatePost() {
        given()
                .body(TestData.updatePostBody())
                .when()
                .put("/posts/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("Updated body content"))
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test
    @Order(5)
    @Story("Delete Post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /posts/1 returns 200 and an empty body")
    void testDeletePost() {
        given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(6)
    @Story("Filter Posts")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /posts?userId=1 returns only posts belonging to userId 1")
    void testGetPostsByQueryParam() {
        given()
                .queryParam("userId", 1)
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("userId", everyItem(equalTo(1)));
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(7)
    @Story("Negative - Fetch Posts")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /posts/99999 returns 404 for a non-existent post")
    void testGetNonExistentPost() {
        given()
                .when()
                .get("/posts/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    @Story("Negative - Fetch Posts")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /posts/abc returns 404 for an invalid non-numeric ID")
    void testGetPostWithInvalidId() {
        given()
                .when()
                .get("/posts/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(9)
    @Story("Negative - Create Post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /posts with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreatePostWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(10)
    @Story("Negative - Update Post")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent post /posts/99999 returns 500")
    void testUpdateNonExistentPost() {
        given()
                .body(TestData.updatePostBody())
                .when()
                .put("/posts/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(11)
    @Story("Negative - Delete Post")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /posts/99999 returns 404 for a non-existent post")
    void testDeleteNonExistentPost() {
        given()
                .when()
                .delete("/posts/99999")
                .then()
                .statusCode(404);
    }
}