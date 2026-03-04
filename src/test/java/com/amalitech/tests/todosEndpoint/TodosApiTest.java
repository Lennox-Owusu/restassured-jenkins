package com.amalitech.tests.todosEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.data.TestData;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Todos Endpoint")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodosApiTest extends TestConfig {

    // ── POSITIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @Story("Fetch Todos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /todos returns a 200 status with a non-empty JSON array")
    void testGetAllTodos() {
        given()
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString(ContentType.JSON.toString()))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/todo-list-schema.json"));
    }

    @Test
    @Order(2)
    @Story("Fetch Todos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /todos/1 returns the correct todo with all required fields")
    void testGetSingleTodo() {
        given()
                .when()
                .get("/todos/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("completed", anyOf(is(true), is(false)))
                .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
    }

    @Test
    @Order(3)
    @Story("Create Todo")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that POST /todos creates a new todo and returns 201 with the created resource")
    void testCreateTodo() {
        given()
                .body(TestData.createTodoBody())
                .when()
                .post("/todos")
                .then()
                .statusCode(201)
                .body("userId", equalTo(1))
                .body("title", equalTo("AmaliTech Test Todo"))
                .body("completed", is(false))
                .body("id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
    }

    @Test
    @Order(4)
    @Story("Update Todo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that PUT /todos/1 updates the todo and returns the updated fields")
    void testUpdateTodo() {
        given()
                .body(TestData.updateTodoBody())
                .when()
                .put("/todos/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Todo Title"))
                .body("completed", is(true))
                .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
    }

    @Test
    @Order(5)
    @Story("Delete Todo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that DELETE /todos/1 returns 200 and an empty body")
    void testDeleteTodo() {
        given()
                .when()
                .delete("/todos/1")
                .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }

    @Test
    @Order(6)
    @Story("Filter Todos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /todos?userId=1 returns only todos belonging to userId 1")
    void testGetTodosByQueryParam() {
        given()
                .queryParam("userId", 1)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    @Order(7)
    @Story("Filter Todos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /todos?completed=true returns only completed todos")
    void testGetCompletedTodos() {
        given()
                .queryParam("completed", true)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("completed", everyItem(is(true)));
    }

    // ── NEGATIVE TESTS ────────────────────────────────────────────────────────

    @Test
    @Order(8)
    @Story("Negative - Fetch Todos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /todos/99999 returns 404 for a non-existent todo")
    void testGetNonExistentTodo() {
        given()
                .when()
                .get("/todos/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(9)
    @Story("Negative - Fetch Todos")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that GET /todos/abc returns 404 for an invalid non-numeric ID")
    void testGetTodoWithInvalidId() {
        given()
                .when()
                .get("/todos/abc")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(10)
    @Story("Negative - Create Todo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that POST /todos with an empty body still returns a response (JSONPlaceholder is lenient)")
    void testCreateTodoWithEmptyBody() {
        given()
                .body("{}")
                .when()
                .post("/todos")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Order(11)
    @Story("Negative - Update Todo")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that PUT on a non-existent todo /todos/99999 returns 500")
    void testUpdateNonExistentTodo() {
        given()
                .body(TestData.updateTodoBody())
                .when()
                .put("/todos/99999")
                .then()
                .statusCode(500);
    }

    @Test
    @Order(12)
    @Story("Negative - Delete Todo")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that DELETE /todos/99999 returns 404 for a non-existent todo")
    void testDeleteNonExistentTodo() {
        given()
                .when()
                .delete("/todos/99999")
                .then()
                .statusCode(404);
    }
}