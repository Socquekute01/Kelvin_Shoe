package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.adapter.ShoeProductRecyclerAdapter;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "CategoryActivity";
    private DataManager dataManager;
    private RecyclerView lvProducts;
    private RecyclerView lvProductsNew;
    private RecyclerView lvBestSeller;
    private EditText etSearch;
    private ImageView ivCart, ivProfile;
    private int userId;
    private String cateType;
    private List<Product> allProducts;
    private List<Product> filteredProducts;
    private ShoeProductRecyclerAdapter adapter;
    TextView tv_cate_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        // Initialize DataManager
        dataManager = new DataManager(this);
        tv_cate_title = findViewById(R.id.tv_Cate_Title);
        // Initialize views
        initViews();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        cateType = bundle.getString("cate_type", "all");

        // Load and display products
        loadProducts();

        // Setup listeners
        setupListeners();
    }

    private void loadProducts() {
        // Get all products from DataManager
        allProducts = dataManager.getAllProducts();
        // Check and add sample shoe products if list is empty
        if (allProducts == null || allProducts.isEmpty()) {
            Toast.makeText(this, "Đang tải sản phẩm mẫu...", Toast.LENGTH_SHORT).show();
            allProducts = dataManager.getAllProducts();
            if (allProducts == null || allProducts.isEmpty()) {
                Toast.makeText(this, "Không thể tải sản phẩm.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (cateType.equals("all") || cateType.equals("best_sellers") || cateType.equals("news")) {
            tv_cate_title.setText("Tất cả sản phẩm");
        }
        else {
            tv_cate_title.setText("Danh mục " + cateType);
            allProducts = allProducts.stream().filter(product -> product.getCategoryId() == dataManager.getCategoryId(cateType)).collect(Collectors.toList());
        }

        // Initialize filtered products
        filteredProducts = new ArrayList<>(allProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        LinearLayoutManager layoutOutStandingManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvProducts.setLayoutManager(layoutManager);
        // Setup adapter
        adapter = new ShoeProductRecyclerAdapter(this, filteredProducts);
        lvProducts.setAdapter(adapter);
    }

    private void initViews() {
        lvProducts = findViewById(R.id.lv_vertical_products);
        etSearch = findViewById(R.id.et_search);
        ivCart = findViewById(R.id.iv_cart);
        ivProfile = findViewById(R.id.iv_profile);
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

        // Cart click
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Profile click
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                startActivity(intent);
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
}
