package com.example.kelvinshoe.model;

public class CartItem {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private String description;

    // Constructor with all fields
    public CartItem(int cartId, int userId, int productId, int quantity, String description) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
    }

    // Constructor without cartId (for new items)
    public CartItem(int userId, int productId, int quantity, String description) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Fixed: Return String instead of int
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                '}';
    }
}