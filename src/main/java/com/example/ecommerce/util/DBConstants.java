package com.example.ecommerce.util;

public final class DBConstants {
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_USER_ROLES = "user_roles";
    public static final String TABLE_TENANTS = "tenants";
    public static final String TABLE_ROLES = "roles";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_FAVORITES = "favorites";

    // Generic column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_TENANT_ID = "tenant_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TOTAL_QUANTITY = "total_quantity";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_KEYCLOAK_USER_ID = "keycloak_user_id";
    public static final String COLUMN_ROLE_ID = "role_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_AVAILABLE_QUANTITY = "available_quantity";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";


    public static final String COLUMN_MOBILE_NUMBER = "mobile_number";
    private DBConstants(){}
}
