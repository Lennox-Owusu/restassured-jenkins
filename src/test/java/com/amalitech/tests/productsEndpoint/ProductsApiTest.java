package com.amalitech.tests.productsEndpoint;

import com.amalitech.config.TestConfig;
import com.amalitech.testdata.ProductTestData;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsApiTest extends TestConfig {

    // ─── GET ALL PRODUCTS ───────────────────────────────────────────────

    @Test
    public void testGetAllProducts() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products")
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ProductTestData.PRODUCTS_LIST_SCHEMA));
    }

    @Test
    public void testGetAllProductsWithLimit() {
        given()
                .spec(requestSpec)
                .queryParam("limit", ProductTestData.PRODUCT_LIMIT)
                .when()
                .get("/products")
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", equalTo(ProductTestData.PRODUCT_LIMIT));
    }

    @Test
    public void testGetAllProductsSortedDesc() {
        given()
                .spec(requestSpec)
                .queryParam("sort", ProductTestData.SORT_DESC)
                .when()
                .get("/products")
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetAllProductsSortedAsc() {
        given()
                .spec(requestSpec)
                .queryParam("sort", ProductTestData.SORT_ASC)
                .when()
                .get("/products")
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0));
    }

    // ─── GET SINGLE PRODUCT ─────────────────────────────────────────────

    @Test
    public void testGetSingleProduct() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/{id}", ProductTestData.VALID_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("id", equalTo(ProductTestData.VALID_PRODUCT_ID))
                .body("title", notNullValue())
                .body("price", greaterThan(0.0f))
                .body("category", notNullValue())
                .body("image", notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ProductTestData.PRODUCT_SCHEMA));
    }

    @Test
    public void testGetNonExistentProduct() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/{id}", ProductTestData.NON_EXISTENT_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body(equalTo("null"));
    }

    @Test
    public void testGetProductWithInvalidId() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/{id}", ProductTestData.INVALID_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK);
    }

    // ─── CATEGORIES ─────────────────────────────────────────────────────

    @Test
    public void testGetAllCategories() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/categories")
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetProductsByElectronicsCategory() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/category/{category}", ProductTestData.CATEGORY_ELECTRONICS)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0))
                .body("category", everyItem(equalTo(ProductTestData.CATEGORY_ELECTRONICS)));
    }

    @Test
    public void testGetProductsByJeweleryCategory() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/category/{category}", ProductTestData.CATEGORY_JEWELERY)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0))
                .body("category", everyItem(equalTo(ProductTestData.CATEGORY_JEWELERY)));
    }

    @Test
    public void testGetProductsByMensClothingCategory() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/category/{category}", ProductTestData.CATEGORY_MENS_CLOTHING)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0))
                .body("category", everyItem(equalTo(ProductTestData.CATEGORY_MENS_CLOTHING)));
    }

    @Test
    public void testGetProductsByWomensClothingCategory() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/category/{category}", ProductTestData.CATEGORY_WOMENS_CLOTHING)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("size()", greaterThan(0))
                .body("category", everyItem(equalTo(ProductTestData.CATEGORY_WOMENS_CLOTHING)));
    }

    // ─── CREATE PRODUCT ─────────────────────────────────────────────────

    @Test
    public void testCreateProduct() {
        given()
                .spec(requestSpec)
                .body(ProductTestData.createProductBody())
                .when()
                .post("/products")
                .then()
                .statusCode(ProductTestData.STATUS_CREATED)
                .body("id", notNullValue())
                .body("title", equalTo(ProductTestData.CREATE_PRODUCT_TITLE))
                .body("price", equalTo(ProductTestData.CREATE_PRODUCT_PRICE));
    }

    // ─── UPDATE PRODUCT ─────────────────────────────────────────────────

    @Test
    public void testUpdateProduct() {
        given()
                .spec(requestSpec)
                .body(ProductTestData.updateProductBody())
                .when()
                .put("/products/{id}", ProductTestData.VALID_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("id", equalTo(ProductTestData.VALID_PRODUCT_ID))
                .body("title", equalTo(ProductTestData.UPDATE_PRODUCT_TITLE))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ProductTestData.PRODUCT_SCHEMA));
    }

    @Test
    public void testPartialUpdateProduct() {
        given()
                .spec(requestSpec)
                .body(ProductTestData.partialUpdateProductBody())
                .when()
                .patch("/products/{id}", ProductTestData.VALID_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("id", equalTo(ProductTestData.VALID_PRODUCT_ID))
                .body("title", equalTo(ProductTestData.PARTIAL_UPDATE_TITLE));
    }

    // ─── DELETE PRODUCT ─────────────────────────────────────────────────

    @Test
    public void testDeleteProduct() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/products/{id}", ProductTestData.VALID_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK)
                .body("id", equalTo(ProductTestData.VALID_PRODUCT_ID));
    }

    @Test
    public void testDeleteNonExistentProduct() {
        given()
                .spec(requestSpec)
                .when()
                .delete("/products/{id}", ProductTestData.NON_EXISTENT_PRODUCT_ID)
                .then()
                .statusCode(ProductTestData.STATUS_OK);
    }
}