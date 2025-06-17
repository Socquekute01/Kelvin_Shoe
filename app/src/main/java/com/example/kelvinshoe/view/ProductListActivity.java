package com.example.kelvinshoe.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;
import com.example.kelvinshoe.view.adapters.ShoeProductAdapter;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private DataManager dataManager;
    private ListView lvProducts;
    private EditText etSearch;
    private ImageView ivCart, ivProfile;
    private LinearLayout categoryMen, categoryWomen, categorySport;
    private TextView tvSeeAll;
    private int userId;

    private List<Product> allProducts;
    private List<Product> filteredProducts;
    private ShoeProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize views
        initViews();

        // Initialize DataManager
        dataManager = new DataManager(this);

        // Get userId from Intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Log.e(TAG, "Invalid userId received");
            Toast.makeText(this, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        Log.d(TAG, "User ID: " + userId);

        // Load and display products
        loadProducts();

        // Setup listeners
        setupListeners();
    }

    private void initViews() {
        lvProducts = findViewById(R.id.lv_products);
        etSearch = findViewById(R.id.et_search);
        ivCart = findViewById(R.id.iv_cart);
        ivProfile = findViewById(R.id.iv_profile);
        categoryMen = findViewById(R.id.category_men);
        categoryWomen = findViewById(R.id.category_women);
        categorySport = findViewById(R.id.category_sport);
        tvSeeAll = findViewById(R.id.tv_see_all);
    }

    private void loadProducts() {
        // Get all products from DataManager
        allProducts = dataManager.getAllProducts();
        // Check and add sample shoe products if list is empty
        if (allProducts == null || allProducts.isEmpty()) {
            Toast.makeText(this, "Đang tải sản phẩm mẫu...", Toast.LENGTH_SHORT).show();
            addSampleShoeProducts();
            allProducts = dataManager.getAllProducts();
            if (allProducts == null || allProducts.isEmpty()) {
                Toast.makeText(this, "Không thể tải sản phẩm.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Initialize filtered products
        filteredProducts = new ArrayList<>(allProducts);

        // Setup adapter
        adapter = new ShoeProductAdapter(this, filteredProducts);
        lvProducts.setAdapter(adapter);
    }

    private void addSampleShoeProducts() {
        try {
            // Men's shoes
            dataManager.addProduct(new Product("Nike Air Max 270", "Giày thể thao nam cao cấp với đệm khí tối ưu", 129.99, 15));
            dataManager.addProduct(new Product("Adidas Ultraboost 22", "Giày chạy bộ nam với công nghệ Boost", 180.00, 12));
            dataManager.addProduct(new Product("Oxford Leather Dress Shoes", "Giày da công sở nam sang trọng", 89.99, 8));
            dataManager.addProduct(new Product("Converse Chuck Taylor All Star", "Giày sneaker cổ điển unisex", 55.00, 25));

            // Women's shoes
            dataManager.addProduct(new Product("Christian Louboutin High Heels", "Giày cao gót nữ thời trang", 695.00, 5));
            dataManager.addProduct(new Product("Nike Air Force 1 Women", "Giày thể thao nữ trắng cổ điển", 90.00, 20));
            dataManager.addProduct(new Product("UGG Classic Short Boots", "Boots nữ ấm áp cho mùa đông", 150.00, 10));
            dataManager.addProduct(new Product("Balenciaga Triple S Sneakers", "Giày sneaker nữ chunky đẳng cấp", 850.00, 3));

            // Sport shoes
            dataManager.addProduct(new Product("Puma RS-X Running Shoes", "Giày chạy bộ retro futuristic", 110.00, 18));
            dataManager.addProduct(new Product("New Balance 990v6", "Giày thể thao premium made in USA", 185.00, 7));
            dataManager.addProduct(new Product("Vans Old Skool Skateboard", "Giày skateboard cổ điển", 65.00, 22));
            dataManager.addProduct(new Product("Jordan Air Jordan 1 Retro", "Giày bóng rổ huyền thoại", 170.00, 9));

            Log.d(TAG, "Sample shoe products added successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error adding sample shoe products: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khi thêm sản phẩm mẫu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListeners() {
        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Product item click
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = filteredProducts.get(position);
                Intent intent = new Intent(ProductListActivity.this, ProductDetailsActivity.class);
                intent.putExtra("productId", selectedProduct.getProductId());
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // Category clicks
        categoryMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("nam");
                Toast.makeText(ProductListActivity.this, "Hiển thị giày nam", Toast.LENGTH_SHORT).show();
            }
        });

        categoryWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("nữ");
                Toast.makeText(ProductListActivity.this, "Hiển thị giày nữ", Toast.LENGTH_SHORT).show();
            }
        });

        categorySport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByCategory("thể thao");
                Toast.makeText(ProductListActivity.this, "Hiển thị giày thể thao", Toast.LENGTH_SHORT).show();
            }
        });

        // Cart click
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to cart activity
                Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // Profile click
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to profile activity
                Intent intent = new Intent(ProductListActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // See all click
        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show all products
                filteredProducts.clear();
                filteredProducts.addAll(allProducts);
                adapter.notifyDataSetChanged();
                Toast.makeText(ProductListActivity.this, "Hiển thị tất cả sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String query) {
        filteredProducts.clear();

        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery) ||
                        product.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    filteredProducts.add(product);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredProducts.isEmpty() && !query.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sản phẩm phù hợp", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterByCategory(String category) {
        filteredProducts.clear();
        String lowerCaseCategory = category.toLowerCase();

        for (Product product : allProducts) {
            String productName = product.getName().toLowerCase();
            String productDesc = product.getDescription().toLowerCase();

            boolean matches = false;
            switch (lowerCaseCategory) {
                case "nam":
                    matches = productName.contains("men") || productName.contains("nam") ||
                            productDesc.contains("nam") || productName.contains("oxford") ||
                            productName.contains("jordan") || productName.contains("air max");
                    break;
                case "nữ":
                    matches = productName.contains("women") || productName.contains("nữ") ||
                            productDesc.contains("nữ") || productName.contains("high heel") ||
                            productName.contains("louboutin") || productName.contains("ugg") ||
                            productName.contains("balenciaga");
                    break;
                case "thể thao":
                    matches = productName.contains("nike") || productName.contains("adidas") ||
                            productName.contains("puma") || productName.contains("running") ||
                            productName.contains("sport") || productName.contains("sneaker") ||
                            productName.contains("air") || productName.contains("boost");
                    break;
            }

            if (matches) {
                filteredProducts.add(product);
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredProducts.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm trong danh mục này", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataManager != null) {
            dataManager.close();
        }
    }
}