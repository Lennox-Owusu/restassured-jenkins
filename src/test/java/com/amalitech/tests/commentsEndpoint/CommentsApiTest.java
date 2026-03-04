package com.amalitech.tests.commentsEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Comments Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentsApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Comments")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /comments returns a 200 status with a non-empty JSON array")
    void testGetAllComments() {
        given()
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/comment-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Comments")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /comments/1 returns the correct comment with all required fields")
    void testGetSingleComment() {
        given()
                .when()
                .get("/comments/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("postId", notNullValue())
                .body("name", notNullValue())
                .body("email", notNullValue())
                .body("body", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/comment-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Create Comment")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /comments creates a new comment and returns 201 with the created resource")
    void testCreateComment() {
        given()
                .body(TestData.createCommentBody())
                .when()
                .post("/comments")
                .then()
                .statusCode(201)
                .body("name", equalTo("AmaliTech Comment"))
                .body("email", equalTo("test@amalitech.com"))
                .body("body", equalTo("This is a test comment"))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/comment-schema.json"));
    }

    @Test
    @Order(4)
    @Story("Update Comment")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /comments/1 updates the comment and returns the updated fields")
    void testUpdateComment() {
        given()
                .body(TestData.updateCommentBody())
                .when()
                .put("/comments/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Comment"))
                .body("email", equalTo("updated@amalitech.com"))
                .body(matchesJsonSchemaInClasspath("schemas/comment-schema.json"));
    }

    @Test
    @Order(5)
    @Story("Delete Comment")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /comments/1 returns 200 and an empty body")
    void testDeleteComment() {
        given()
                .when()
                .delete("/comments/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(6)
    @Story("Filter Comments")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /comments?postId=1 returns only comments belonging to postId 1")
    void testGetCommentsByQueryParam() {
        given()
                .queryParam("postId", 1)
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(1)));
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(7)
    @Story("Negative - Fetch Comments")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /comments/99999 returns 404 for a non-existent comment")
    void testGetNonExistentComment() {
        given()
                .when()
                .get("/comments/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    @Story("Negative - Fetch Comments")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /comments/abc returns 404 for an invalid non-numeric ID")
    void testGetCommentWithInvalidId() {
        given()
                .when()
                .get("/comments/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(9)
    @Story("Negative - Create Comment")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /comments with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreateCommentWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/comments")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(10)
    @Story("Negative - Update Comment")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent comment /comments/99999 returns 500")
    void testUpdateNonExistentComment() {
        given()
                .body(TestData.updateCommentBody())
                .when()
                .put("/comments/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(11)
    @Story("Negative - Delete Comment")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /comments/99999 returns 404 for a non-existent comment")
    void testDeleteNonExistentComment() {
        given()
                .when()
                .delete("/comments/99999")
                .then()
                .statusCode(404);
    }
}