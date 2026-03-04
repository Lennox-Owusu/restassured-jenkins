package com.amalitech.tests.usersEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Users Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /users returns a 200 status with a non-empty JSON array")
    void testGetAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/user-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /users/1 returns the correct user with all required top-level fields")
    void testGetSingleUser() {
        given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body("username", notNullValue())
                .body("email", notNullValue())
                .body("phone", notNullValue())
                .body("website", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Fetch Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /users/1 returns all nested address and geo fields")
    void testGetUserAddressFields() {
        given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("address.street", notNullValue())
                .body("address.suite", notNullValue())
                .body("address.city", notNullValue())
                .body("address.zipcode", notNullValue())
                .body("address.geo.lat", notNullValue())
                .body("address.geo.lng", notNullValue());
    }

    @Test
    @Order(4)
    @Story("Fetch Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /users/1 returns all nested company fields")
    void testGetUserCompanyFields() {
        given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("company.name", notNullValue())
                .body("company.catchPhrase", notNullValue())
                .body("company.bs", notNullValue());
    }

    @Test
    @Order(5)
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /users creates a new user and returns 201 with the created resource")
    void testCreateUser() {
        given()
                .body(TestData.createUserBody())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Lennox Owusu"))
                .body("username", equalTo("lennox"))
                .body("email", equalTo("lennox@amalitech.com"))
                .body("phone", equalTo("1-234-567-8900"))
                .body("website", equalTo("amalitech.com"))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test
    @Order(6)
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /users/1 updates the user and returns all updated fields")
    void testUpdateUser() {
        given()
                .body(TestData.updateUserBody())
                .when()
                .put("/users/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Updated User"))
                .body("username", equalTo("updateduser"))
                .body("email", equalTo("updated@amalitech.com"))
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test
    @Order(7)
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /users/1 returns 200 and an empty body")
    void testDeleteUser() {
        given()
                .when()
                .delete("/users/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(8)
    @Story("Fetch Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the full JSON schema of GET /users/1 matches the expected user schema")
    void testGetUserJsonSchema() {
        given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test
    @Order(9)
    @Story("Fetch Users")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /users/1 returns the expected Content-Type and Connection response headers")
    void testResponseHeaders() {
        given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .header("Connection", equalTo("keep-alive"));
    }

    @Test
    @Order(10)
    @Story("Filter Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /users?id=1 returns exactly one user with the correct id")
    void testGetUsersByQueryParam() {
        given()
                .queryParam("id", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].id", equalTo(1))
                .body("[0].name", notNullValue());
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(11)
    @Story("Negative - Fetch Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /users/99999 returns 404 for a non-existent user")
    void testGetNonExistentUser() {
        given()
                .when()
                .get("/users/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(12)
    @Story("Negative - Fetch Users")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /users/abc returns 404 for an invalid non-numeric ID")
    void testGetUserWithInvalidId() {
        given()
                .when()
                .get("/users/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(13)
    @Story("Negative - Create User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /users with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreateUserWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(14)
    @Story("Negative - Update User")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent user /users/99999 returns 500")
    void testUpdateNonExistentUser() {
        given()
                .body(TestData.updateUserBody())
                .when()
                .put("/users/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(15)
    @Story("Negative - Delete User")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /users/99999 returns 404 for a non-existent user")
    void testDeleteNonExistentUser() {
        given()
                .when()
                .delete("/users/99999")
                .then()
                .statusCode(404);
    }
}