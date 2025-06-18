package com.example.kelvinshoe.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kelvinshoe.model.CartItem;
import com.example.kelvinshoe.model.Notification;
import com.example.kelvinshoe.model.Order;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final DatabaseHelper dbHelper;
    private final SQLiteDatabase database;

    public DataManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // User Operations
    public User addUser(User user) {
        if (isUserExists(user.getUsername(), user.getEmail())) {
            return null; // Trả về null nếu người dùng đã tồn tại
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        long newRowId = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        if (newRowId != -1) {
            user.setUserId((int) newRowId);
        }
        return user;
    }

    private boolean isUserExists(String username, String email) {
        String[] columns = {DatabaseHelper.COL_USER_ID};
        String selection = DatabaseHelper.COL_USERNAME + "=? OR " + DatabaseHelper.COL_EMAIL + "=?";
        String[] selectionArgs = {username, email};
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public User login(String username, String password) {
        String[] columns = {DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_USERNAME, DatabaseHelper.COL_PASSWORD,
                DatabaseHelper.COL_EMAIL, DatabaseHelper.COL_FULL_NAME};
        String selection = DatabaseHelper.COL_USERNAME + "=? AND " + DatabaseHelper.COL_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME))
            );
        }
        cursor.close();
        return user;
    }

    public User getUserById(int userId) {
        String[] columns = {DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_USERNAME, DatabaseHelper.COL_PASSWORD,
                DatabaseHelper.COL_EMAIL, DatabaseHelper.COL_FULL_NAME};
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, columns,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME))
            );
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String[] columns = {DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_USERNAME, DatabaseHelper.COL_PASSWORD,
                DatabaseHelper.COL_EMAIL, DatabaseHelper.COL_FULL_NAME};
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME))
                );
                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return userList;
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        return database.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(user.getUserId())});
    }

    public int deleteUser(int userId) {
        return database.delete(DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    // Product Operations
    public Product addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, product.getName());
        values.put(DatabaseHelper.COL_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COL_PRICE, product.getPrice());
        values.put(DatabaseHelper.COL_STOCK, product.getStock());
        long newRowId = database.insert(DatabaseHelper.TABLE_PRODUCTS, null, values);
        if (newRowId != -1) {
            product.setProductId((int) newRowId);
        }
        return product;
    }

    public Product getProductById(int productId) {
        String[] columns = {DatabaseHelper.COL_PRODUCT_ID, DatabaseHelper.COL_NAME, DatabaseHelper.COL_DESCRIPTION,
                DatabaseHelper.COL_PRICE, DatabaseHelper.COL_STOCK};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS, columns,
                DatabaseHelper.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STOCK))
            );
            cursor.close();
            return product;
        }
        cursor.close();
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String[] columns = {DatabaseHelper.COL_PRODUCT_ID, DatabaseHelper.COL_NAME, DatabaseHelper.COL_DESCRIPTION,
                DatabaseHelper.COL_PRICE, DatabaseHelper.COL_STOCK};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STOCK))
                );
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return productList;
    }

    public int updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, product.getName());
        values.put(DatabaseHelper.COL_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COL_PRICE, product.getPrice());
        values.put(DatabaseHelper.COL_STOCK, product.getStock());
        return database.update(DatabaseHelper.TABLE_PRODUCTS, values,
                DatabaseHelper.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(product.getProductId())});
    }

    public int deleteProduct(int productId) {
        return database.delete(DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
    }

    // CartItem Operations
    public CartItem addToCart(CartItem cartItem) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, cartItem.getUserId());
        values.put(DatabaseHelper.COL_PRODUCT_ID, cartItem.getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, cartItem.getQuantity());
        long newRowId = database.insert(DatabaseHelper.TABLE_CART, null, values);
        if (newRowId != -1) {
            cartItem.setCartId((int) newRowId);
        }
        return cartItem;
    }

    public CartItem getCartItemById(int cartId) {
        String[] columns = {DatabaseHelper.COL_CART_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_QUANTITY};
        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, columns,
                DatabaseHelper.COL_CART_ID + "=?", new String[]{String.valueOf(cartId)}, null, null, null);
        if (cursor.moveToFirst()) {
            CartItem cartItem = new CartItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
            );
            cursor.close();
            return cartItem;
        }
        cursor.close();
        return null;
    }

    public List<CartItem> getAllCartItems(int userId) {
        List<CartItem> cartList = new ArrayList<>();
        String[] columns = {DatabaseHelper.COL_CART_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_QUANTITY, DatabaseHelper.COL_DESCRIPTION};
        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, columns,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
                );
                cartList.add(cartItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return cartList;
    }

    public int updateCartItem(CartItem cartItem) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, cartItem.getUserId());
        values.put(DatabaseHelper.COL_PRODUCT_ID, cartItem.getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, cartItem.getQuantity());
        return database.update(DatabaseHelper.TABLE_CART, values,
                DatabaseHelper.COL_CART_ID + "=?", new String[]{String.valueOf(cartItem.getCartId())});
    }

    public int deleteCartItem(int cartId) {
        return database.delete(DatabaseHelper.TABLE_CART,
                DatabaseHelper.COL_CART_ID + "=?", new String[]{String.valueOf(cartId)});
    }

    // Order Operations
    public Order addOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, order.getUserId());
        values.put(DatabaseHelper.COL_PRODUCT_ID, order.getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, order.getQuantity());
        values.put(DatabaseHelper.COL_TOTAL_PRICE, order.getTotalPrice());
        values.put(DatabaseHelper.COL_ORDER_DATE, order.getOrderDate());
        long newRowId = database.insert(DatabaseHelper.TABLE_ORDERS, null, values);
        if (newRowId != -1) {
            order.setOrderId((int) newRowId);
        }
        return order;
    }

    public Order getOrderById(int orderId) {
        String[] columns = {DatabaseHelper.COL_ORDER_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_QUANTITY, DatabaseHelper.COL_TOTAL_PRICE, DatabaseHelper.COL_ORDER_DATE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERS, columns,
                DatabaseHelper.COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Order order = new Order(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE))
            );
            cursor.close();
            return order;
        }
        cursor.close();
        return null;
    }

    public List<Order> getAllOrders(int userId) {
        List<Order> orderList = new ArrayList<>();
        String[] columns = {DatabaseHelper.COL_ORDER_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_QUANTITY, DatabaseHelper.COL_TOTAL_PRICE, DatabaseHelper.COL_ORDER_DATE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERS, columns,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return orderList;
    }

    public int updateOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, order.getUserId());
        values.put(DatabaseHelper.COL_PRODUCT_ID, order.getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, order.getQuantity());
        values.put(DatabaseHelper.COL_TOTAL_PRICE, order.getTotalPrice());
        values.put(DatabaseHelper.COL_ORDER_DATE, order.getOrderDate());
        return database.update(DatabaseHelper.TABLE_ORDERS, values,
                DatabaseHelper.COL_ORDER_ID + "=?", new String[]{String.valueOf(order.getOrderId())});
    }

    public int deleteOrder(int orderId) {
        return database.delete(DatabaseHelper.TABLE_ORDERS,
                DatabaseHelper.COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    // Notification Operations
    public Notification addNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, notification.getUserId());
        values.put(DatabaseHelper.COL_MESSAGE, notification.getMessage());
        values.put(DatabaseHelper.COL_IS_READ, notification.isRead() ? 1 : 0);
        values.put(DatabaseHelper.COL_CREATED_AT, notification.getCreatedAt());
        long newRowId = database.insert(DatabaseHelper.TABLE_NOTIFICATIONS, null, values);
        if (newRowId != -1) {
            notification.setNotificationId((int) newRowId);
        }
        return notification;
    }

    public Notification getNotificationById(int notificationId) {
        String[] columns = {DatabaseHelper.COL_NOTIFICATION_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_MESSAGE,
                DatabaseHelper.COL_IS_READ, DatabaseHelper.COL_CREATED_AT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NOTIFICATIONS, columns,
                DatabaseHelper.COL_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Notification notification = new Notification(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MESSAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IS_READ)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT))
            );
            cursor.close();
            return notification;
        }
        cursor.close();
        return null;
    }

    public List<Notification> getAllNotifications(int userId) {
        List<Notification> notificationList = new ArrayList<>();
        String[] columns = {DatabaseHelper.COL_NOTIFICATION_ID, DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_MESSAGE,
                DatabaseHelper.COL_IS_READ, DatabaseHelper.COL_CREATED_AT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NOTIFICATIONS, columns,
                DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MESSAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IS_READ)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT))
                );
                notificationList.add(notification);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return notificationList;
    }

    public int updateNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_ID, notification.getUserId());
        values.put(DatabaseHelper.COL_MESSAGE, notification.getMessage());
        values.put(DatabaseHelper.COL_IS_READ, notification.isRead() ? 1 : 0);
        values.put(DatabaseHelper.COL_CREATED_AT, notification.getCreatedAt());
        return database.update(DatabaseHelper.TABLE_NOTIFICATIONS, values,
                DatabaseHelper.COL_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notification.getNotificationId())});
    }

    public int deleteNotification(int notificationId) {
        return database.delete(DatabaseHelper.TABLE_NOTIFICATIONS,
                DatabaseHelper.COL_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});
    }

    public void close() {
        if (database != null) database.close();
        if (dbHelper != null) dbHelper.close();
    }
}