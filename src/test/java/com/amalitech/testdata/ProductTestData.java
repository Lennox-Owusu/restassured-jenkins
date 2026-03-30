package com.amalitech.testdata;

public class ProductTestData {

    // ─── EXPECTED STATUS CODES ───────────────────────────────────────────
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;

    // ─── VALID PRODUCT IDS ───────────────────────────────────────────────
    public static final int VALID_PRODUCT_ID = 1;
    public static final int VALID_PRODUCT_ID_2 = 2;
    public static final int NON_EXISTENT_PRODUCT_ID = 99999;
    public static final String INVALID_PRODUCT_ID = "abc";

    // ─── QUERY PARAMS ────────────────────────────────────────────────────
    public static final int PRODUCT_LIMIT = 5;
    public static final String SORT_DESC = "desc";
    public static final String SORT_ASC = "asc";

    // ─── CATEGORIES ──────────────────────────────────────────────────────
    public static final String CATEGORY_ELECTRONICS = "electronics";
    public static final String CATEGORY_JEWELERY = "jewelery";
    public static final String CATEGORY_MENS_CLOTHING = "men's clothing";
    public static final String CATEGORY_WOMENS_CLOTHING = "women's clothing";

    // ─── CREATE PRODUCT ──────────────────────────────────────────────────
    public static final String CREATE_PRODUCT_TITLE = "Test Product";
    public static final float CREATE_PRODUCT_PRICE = 29.99f;
    public static final String CREATE_PRODUCT_DESCRIPTION = "A test product description";
    public static final String CREATE_PRODUCT_CATEGORY = "electronics";
    public static final String CREATE_PRODUCT_IMAGE = "https://fakestoreapi.com/img/test.jpg";

    public static String createProductBody() {
        return """
                {
                    "title": "%s",
                    "price": %s,
                    "description": "%s",
                    "category": "%s",
                    "image": "%s"
                }
                """.formatted(
                CREATE_PRODUCT_TITLE,
                CREATE_PRODUCT_PRICE,
                CREATE_PRODUCT_DESCRIPTION,
                CREATE_PRODUCT_CATEGORY,
                CREATE_PRODUCT_IMAGE
        );
    }

    // ─── UPDATE PRODUCT ──────────────────────────────────────────────────
    public static final String UPDATE_PRODUCT_TITLE = "Updated Product";
    public static final float UPDATE_PRODUCT_PRICE = 49.99f;
    public static final String UPDATE_PRODUCT_DESCRIPTION = "Updated description";
    public static final String UPDATE_PRODUCT_CATEGORY = "electronics";
    public static final String UPDATE_PRODUCT_IMAGE = "https://fakestoreapi.com/img/test.jpg";

    public static String updateProductBody() {
        return """
                {
                    "title": "%s",
                    "price": %s,
                    "description": "%s",
                    "category": "%s",
                    "image": "%s"
                }
                """.formatted(
                UPDATE_PRODUCT_TITLE,
                UPDATE_PRODUCT_PRICE,
                UPDATE_PRODUCT_DESCRIPTION,
                UPDATE_PRODUCT_CATEGORY,
                UPDATE_PRODUCT_IMAGE
        );
    }

    // ─── PARTIAL UPDATE PRODUCT ──────────────────────────────────────────
    public static final String PARTIAL_UPDATE_TITLE = "Partially Updated Product";

    public static String partialUpdateProductBody() {
        return """
                {
                    "title": "%s"
                }
                """.formatted(PARTIAL_UPDATE_TITLE);
    }

    // ─── SCHEMAS ─────────────────────────────────────────────────────────
    public static final String PRODUCT_SCHEMA = "schemas/product-schema.json";
    public static final String PRODUCTS_LIST_SCHEMA = "schemas/products-list-schema.json";
}