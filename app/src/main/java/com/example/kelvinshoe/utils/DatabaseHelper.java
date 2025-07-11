package com.example.kelvinshoe.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShoppingApp.db";
    private static final int DATABASE_VERSION = 2; // Increment version due to schema change

    // Table Users
    public static final String TABLE_USERS = "Users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";
    public static final String COL_FULL_NAME = "full_name";

    // Table Categories
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_CATEGORY_NAME = "category_name";
    public static final String COL_IMAGE_URL = "image_url";

    // Table Products
    public static final String TABLE_PRODUCTS = "Products";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PRICE = "price";
    public static final String COL_STOCK = "stock";
    public static final String IMAGE_URL = "image_url";

    // Table Cart
    public static final String TABLE_CART = "Cart";
    public static final String COL_CART_ID = "cart_id";
    public static final String COL_QUANTITY = "quantity";

    // Table Orders
    public static final String TABLE_ORDERS = "Orders";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_TOTAL_PRICE = "total_price";
    public static final String COL_ORDER_DATE = "order_date";

    // Table Notifications
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
        // Create Categories table
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CATEGORY_NAME + " TEXT NOT NULL UNIQUE)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT NOT NULL UNIQUE," +
                COL_PASSWORD + " TEXT NOT NULL," +
                COL_EMAIL + " TEXT NOT NULL UNIQUE," +
                COL_FULL_NAME + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Products table with foreign key to Categories
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT NOT NULL," +
                COL_DESCRIPTION + " TEXT," +
                COL_PRICE + " REAL NOT NULL," +
                COL_STOCK + " INTEGER NOT NULL," +
                COL_CATEGORY_ID + " INTEGER NOT NULL," +
                COL_IMAGE_URL + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COL_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + "))";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create Cart table
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ID + " INTEGER NOT NULL," +
                COL_PRODUCT_ID + " INTEGER NOT NULL," +
                COL_QUANTITY + " INTEGER NOT NULL," +
                COL_DESCRIPTION + " TEXT," +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")," +
                "FOREIGN KEY (" + COL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "))";
        db.execSQL(CREATE_CART_TABLE);

        // Create Orders table
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

        // Create Notifications table
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ID + " INTEGER NOT NULL," +
                COL_MESSAGE + " TEXT NOT NULL," +
                COL_IS_READ + " INTEGER DEFAULT 0," +
                COL_CREATED_AT + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        // Insert initial categories
        String[] categories = {"Giày Nam", "Giày Nữ", "Thể Thao", "Trẻ Em", "Sandal", "Bốt", "Dép", "Giày Cao Cổ"};
        for (String category : categories) {
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COL_CATEGORY_NAME + ") VALUES (?)", new String[]{category});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create Categories table
            String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_CATEGORY_NAME + " TEXT NOT NULL UNIQUE)";
            db.execSQL(CREATE_CATEGORIES_TABLE);

            // Add category_id column to Products table
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + COL_CATEGORY_ID + " INTEGER");

            // Insert initial categories
            String[] categories = {"Giày Nam", "Giày Nữ", "Thể Thao", "Trẻ Em", "Sandal", "Bốt", "Dép", "Giày Cao Cổ"};
            for (String category : categories) {
                db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COL_CATEGORY_NAME + ") VALUES (?)", new String[]{category});
            }

            // Update existing products to have a default category (e.g., assign to "Giày Nam" with category_id = 1)
            db.execSQL("UPDATE " + TABLE_PRODUCTS + " SET " + COL_CATEGORY_ID + " = 1");

            // Add foreign key constraint to Products table
            // Since SQLite doesn't support adding foreign keys via ALTER TABLE, we need to recreate the Products table
            db.execSQL("CREATE TABLE temp_products (" +
                    COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_NAME + " TEXT NOT NULL," +
                    COL_DESCRIPTION + " TEXT," +
                    COL_PRICE + " REAL NOT NULL," +
                    COL_STOCK + " INTEGER NOT NULL," +
                    COL_CATEGORY_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY (" + COL_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + "))");
            db.execSQL("INSERT INTO temp_products SELECT * FROM " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE " + TABLE_PRODUCTS);
            db.execSQL("ALTER TABLE temp_products RENAME TO " + TABLE_PRODUCTS);
        }

        // Drop all tables if further upgrades are needed
        if (newVersion > 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}