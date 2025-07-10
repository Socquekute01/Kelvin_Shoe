package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.adapter.BannerAdapter;
import com.example.kelvinshoe.adapter.BannerItem;
import com.example.kelvinshoe.adapter.ShoeProductRecyclerAdapter;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private DataManager dataManager;
    private RecyclerView lvProducts;
    private RecyclerView lvProductsNew;
    private RecyclerView lvBestSeller;
    private EditText etSearch;
    private ImageView ivCart, ivProfile;
    private LinearLayout categoryMen, categoryWomen, categorySport, categoryBaby;
    private TextView tvSeeAll;
    private int userId;

    private List<Product> allProducts;
    private List<Product> filteredProducts;
    private ShoeProductRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize views
        initViews();

        // Initialize DataManager
        dataManager = new DataManager(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = Integer.parseInt(sharedPreferences.getString("userId", "-1"));

        // Get userId from Intent
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

        // Setup banner
        setupBanner();
        startAutoSlide();
    }

    private void initViews() {
        lvProducts = findViewById(R.id.lv_products);
        lvProductsNew = findViewById(R.id.lv_products_new);
        lvBestSeller = findViewById(R.id.lv_products_bestsellers);
        etSearch = findViewById(R.id.et_search);
        ivCart = findViewById(R.id.iv_cart);
        ivProfile = findViewById(R.id.iv_profile);
        categoryMen = findViewById(R.id.category_men);
        categoryWomen = findViewById(R.id.category_women);
        categorySport = findViewById(R.id.category_sport);
        categoryBaby = findViewById(R.id.category_kids);
        tvSeeAll = findViewById(R.id.tv_see_all);
        vpBanner = findViewById(R.id.vp_banner);
        llBannerIndicators = findViewById(R.id.ll_banner_indicators);
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
        LinearLayoutManager layoutOutStandingManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutNewManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutSellerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lvProducts.setLayoutManager(layoutOutStandingManager);
        lvProductsNew.setLayoutManager(layoutNewManager);
        lvBestSeller.setLayoutManager(layoutSellerManager);
        // Setup adapter
        adapter = new ShoeProductRecyclerAdapter(this, filteredProducts);
        lvProducts.setAdapter(adapter);
        lvProductsNew.setAdapter(adapter);
        lvBestSeller.setAdapter(adapter);
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

        categoryBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, CategoryActivity.class);
                startActivity(intent);
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
    private ViewPager2 vpBanner;
    private LinearLayout llBannerIndicators;
    private BannerAdapter bannerAdapter;
    private List<BannerItem> bannerList;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;
    private void setupBanner() {
        // Create sample banner data
        bannerList = new ArrayList<>();
        bannerList.add(new BannerItem(
                "Flash Sale 70%",
                "Giảm giá cực sốc cho tất cả giày thể thao",
                R.drawable.banner1,
                "data:image/jpeg;base64,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"
        ));
        bannerList.add(new BannerItem(
                "Bộ sưu tập mới",
                "Khám phá xu hướng giày thời trang 2025",
                R.drawable.banner2,
                "https://static.vecteezy.com/system/resources/thumbnails/004/707/493/small/online-shopping-on-phone-buy-sell-business-digital-web-banner-application-money-advertising-payment-ecommerce-illustration-search-vector.jpg"
        ));
        bannerList.add(new BannerItem(
                "Miễn phí vận chuyển",
                "Đơn hàng từ 500k được giao hàng miễn phí",
                R.drawable.banner3,
                "data:image/jpeg;base64,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"
        ));

        // Setup adapter
        bannerAdapter = new BannerAdapter(this, bannerList);
        bannerAdapter.setOnBannerClickListener(banner -> {
            // Handle banner click
            // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getActionUrl()));
            // startActivity(intent);
        });

        vpBanner.setAdapter(bannerAdapter);

        // Setup indicators
        setupIndicators();

        // Setup page change callback
        vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateIndicators(position);
            }
        });
    }

    private void setupIndicators() {
        llBannerIndicators.removeAllViews();

        for (int i = 0; i < bannerList.size(); i++) {
            View indicator = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(8, 0, 8, 0);
            indicator.setLayoutParams(params);

            if (i == 0) {
                indicator.setBackgroundResource(R.drawable.indicator_active);
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive);
            }

            llBannerIndicators.addView(indicator);
        }
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < llBannerIndicators.getChildCount(); i++) {
            View indicator = llBannerIndicators.getChildAt(i);
            if (i == position) {
                indicator.setBackgroundResource(R.drawable.indicator_active);
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive);
            }
        }
    }

    private void startAutoSlide() {
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == bannerList.size() - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                vpBanner.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 3000); // 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataManager != null) {
            dataManager.close();
        }
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    protected void onPause() {
        super.onPause();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoSlide();
    }
}