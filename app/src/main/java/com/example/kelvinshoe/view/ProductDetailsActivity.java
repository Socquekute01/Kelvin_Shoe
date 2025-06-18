package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.CartItem;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;
import com.example.kelvinshoe.utils.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    private DataManager databaseHelper;
    private Product currentProduct;
    private int productId;
    private int userId;
    private int quantity = 1;
    private String selectedSize = "39"; // Default size
    private boolean isFavorite = false;

    // UI Components
    private ImageButton btnBack, btnFavorite, btnDecrease, btnIncrease;
    private ImageView imgProduct;
    private TextView tvProductName, tvCategory, tvPrice, tvDescription;
    private TextView tvRating, tvStockBadge, tvQuantity;
    private TextView tvSize38, tvSize39, tvSize40, tvSize41;
    private RatingBar ratingBar;
    private Button btnAddToCart, btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize database helper
        databaseHelper = new DataManager(this);

        // Get product ID from intent
        productId = getIntent().getIntExtra("productId", -1);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        userId = Integer.parseInt(sharedPreferences.getString("userId", "-1"));

        if (productId == -1) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupEventListeners();
        loadProductData();
    }

    private void initializeViews() {
        // Header buttons
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);

        // Product info
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvRating = findViewById(R.id.tvRating);
        tvStockBadge = findViewById(R.id.tvStockBadge);
        ratingBar = findViewById(R.id.ratingBar);

        // Size selection
        tvSize38 = findViewById(R.id.tvSize38);
        tvSize39 = findViewById(R.id.tvSize39);
        tvSize40 = findViewById(R.id.tvSize40);
        tvSize41 = findViewById(R.id.tvSize41);

        // Quantity controls
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        tvQuantity = findViewById(R.id.tvQuantity);

        // Action buttons
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void setupEventListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Favorite button
        btnFavorite.setOnClickListener(v -> toggleFavorite());

        // Size selection
        tvSize38.setOnClickListener(v -> selectSize("38", tvSize38));
        tvSize39.setOnClickListener(v -> selectSize("39", tvSize39));
        tvSize40.setOnClickListener(v -> selectSize("40", tvSize40));
        tvSize41.setOnClickListener(v -> selectSize("41", tvSize41));

        // Quantity controls
        btnDecrease.setOnClickListener(v -> decreaseQuantity());
        btnIncrease.setOnClickListener(v -> increaseQuantity());

        // Action buttons
        btnAddToCart.setOnClickListener(v -> addToCart());
        btnBuyNow.setOnClickListener(v -> buyNow());

        // Set default selected size
        selectSize("39", tvSize39);
    }

    private void loadProductData() {
        // Load product from database using productId
        currentProduct = databaseHelper.getProductById(productId);

        if (currentProduct == null) {
            // Create sample data if not found in database
            createSampleProduct();
        }

        displayProductInfo();
        checkFavoriteStatus();
    }

    private void createSampleProduct() {
        // Create sample product data based on productId
        switch (productId) {
            case 1:
                currentProduct = new Product(1, "Nike Air Max 270", "Premium quality sports shoes with advanced cushioning technology. Perfect for running and everyday wear.", 129.99, 15);
                break;
            case 2:
                currentProduct = new Product(2, "Adidas Ultraboost", "High-performance running shoes with responsive cushioning and energy return.", 179.99, 8);
                break;
            case 3:
                currentProduct = new Product(3, "Converse All Star", "Classic canvas sneakers perfect for casual wear. Timeless design meets modern comfort.", 65.99, 25);
                break;
            case 4:
                currentProduct = new Product(4, "Leather Boots", "Durable leather boots for outdoor adventures. Waterproof and comfortable for all-day wear.", 199.99, 12);
                break;
            case 5:
                currentProduct = new Product(5, "Summer Sandals", "Comfortable sandals perfect for beach days and summer walks. Lightweight and breathable.", 45.99, 30);
                break;
            default:
                currentProduct = new Product(productId, "Fashion Shoe", "Stylish footwear for modern lifestyle. Comfortable and trendy design.", 89.99, 20);
                break;
        }
    }

    private void displayProductInfo() {
        tvProductName.setText(currentProduct.getName());
        tvDescription.setText(currentProduct.getDescription());

        // Format price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        tvPrice.setText(formatter.format(currentProduct.getPrice()));

        // Set category based on product type
        String category = getCategoryByProductId(productId);
        tvCategory.setText(category);

        // Set product image based on category
        setProductImage(category);

        // Stock status
        if (currentProduct.getStock() > 0) {
            tvStockBadge.setText("In Stock (" + currentProduct.getStock() + ")");
            tvStockBadge.setBackgroundResource(R.drawable.premium_highlight);
        } else {
            tvStockBadge.setText("Out of Stock");
            tvStockBadge.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            btnAddToCart.setEnabled(false);
            btnBuyNow.setEnabled(false);
        }

        // Sample rating
        float rating = 4.0f + (productId % 10) * 0.1f;
        ratingBar.setRating(rating);
        int reviewCount = 50 + (productId * 15) % 200;
        tvRating.setText(String.format("%.1f (%d reviews)", rating, reviewCount));
    }

    private String getCategoryByProductId(int id) {
        switch (id % 5) {
            case 1: return "Sport Shoes";
            case 2: return "Boots";
            case 3: return "Sandals";
            case 4: return "High Heels";
            default: return "Casual Shoes";
        }
    }

    private void setProductImage(String category) {
        switch (category) {
            case "Sport Shoes":
                imgProduct.setImageResource(R.drawable.ic_sport_shoe);
                break;
            case "Boots":
                imgProduct.setImageResource(R.drawable.ic_boot);
                break;
            case "Sandals":
                imgProduct.setImageResource(R.drawable.ic_sandal);
                break;
            case "High Heels":
                imgProduct.setImageResource(R.drawable.ic_high_heel);
                break;
            default:
                imgProduct.setImageResource(R.drawable.ic_default_shoe);
                break;
        }
    }

    private void selectSize(String size, TextView sizeView) {
        // Reset all size views
        resetSizeSelection();

        // Highlight selected size
        selectedSize = size;
        sizeView.setBackgroundResource(R.drawable.header_gradient);
        sizeView.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void resetSizeSelection() {
        tvSize38.setBackgroundResource(R.drawable.category_tag_background);
        tvSize39.setBackgroundResource(R.drawable.category_tag_background);
        tvSize40.setBackgroundResource(R.drawable.category_tag_background);
        tvSize41.setBackgroundResource(R.drawable.category_tag_background);

        tvSize38.setTextColor(getResources().getColor(R.color.black));
        tvSize39.setTextColor(getResources().getColor(R.color.black));
        tvSize40.setTextColor(getResources().getColor(R.color.black));
        tvSize41.setTextColor(getResources().getColor(R.color.black));
    }

    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            tvQuantity.setText(String.valueOf(quantity));
        }
    }

    private void increaseQuantity() {
        if (quantity < currentProduct.getStock()) {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        } else {
            Toast.makeText(this, "Maximum stock reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFavorite() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFavoriteStatus() {
        // Check if product is in favorites (implement based on your favorites logic)
        // For now, set default state
        isFavorite = false;
        btnFavorite.setImageResource(R.drawable.ic_favorite_border);
    }

    private void addToCart() {
        if (currentProduct.getStock() < quantity) {
            Toast.makeText(this, "Not enough stock available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add to cart logic here
        String message = String.format("Added %d x %s (Size %s) to cart",
                quantity, currentProduct.getName(), selectedSize);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        CartItem newItems = new CartItem(userId, productId, quantity, selectedSize);
        databaseHelper.addToCart(newItems);

        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    private void buyNow() {
        if (currentProduct.getStock() < quantity) {
            Toast.makeText(this, "Not enough stock available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to checkout activity
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("SIZE", selectedSize);
        intent.putExtra("PRICE", currentProduct.getPrice() * quantity);

        Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
         startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}