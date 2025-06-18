package com.example.kelvinshoe.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShoppingApp.db";
    private static final int DATABASE_VERSION = 1;

    // Tạo bảng Users
    public static final String TABLE_USERS = "Users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";
    public static final String COL_FULL_NAME = "full_name";

    // Tạo bảng Products
    public static final String TABLE_PRODUCTS = "Products";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_STOCK = "stock";

    // Tạo bảng Cart
    public static final String TABLE_CART = "Cart";
    public static final String COL_CART_ID = "cart_id";
    public static final String COL_QUANTITY = "quantity";
    // Tạo bảng Orders
    public static final String TABLE_ORDERS = "Orders";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_TOTAL_PRICE = "total_price";
    public static final String COL_ORDER_DATE = "order_date";

    // Tạo bảng Notifications
    public static final String TABLE_NOTIFICATIONS = "Notifications";
    public static final String COL_NOTIFICATION_ID = "notification_id";
    public static final String COL_MESSAGE = "message";
    public static final String COL_IS_READ = "is_read";
    public static final String COL_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT NOT NULL UNIQUE," +
                COL_PASSWORD + " TEXT NOT NULL," +
                COL_EMAIL + " TEXT NOT NULL UNIQUE," +
                COL_FULL_NAME + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT NOT NULL," +
                COL_DESCRIPTION + " TEXT," +
                COL_PRICE + " REAL NOT NULL," +
                COL_STOCK + " INTEGER NOT NULL)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ID + " INTEGER NOT NULL," +
                COL_PRODUCT_ID + " INTEGER NOT NULL," +
                COL_QUANTITY + " INTEGER NOT NULL," +
                COL_DESCRIPTION + " TEXT," +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")," +
                "FOREIGN KEY (" + COL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "))";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ID + " INTEGER NOT NULL," +
                COL_PRODUCT_ID + " INTEGER NOT NULL," +
                COL_QUANTITY + " INTEGER NOT NULL," +
                COL_TOTAL_PRICE + " REAL NOT NULL," +
                COL_ORDER_DATE + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")," +
                "FOREIGN KEY (" + COL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "))";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ID + " INTEGER NOT NULL," +
                COL_MESSAGE + " TEXT NOT NULL," +
                COL_IS_READ + " INTEGER DEFAULT 0," +
                COL_CREATED_AT + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}