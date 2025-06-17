package com.example.kelvinshoe.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.utils.DatabaseHelper;

public class ProductDetailsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView tvProductName, tvProductDescription, tvProductPrice;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        dbHelper = new DatabaseHelper(this);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductDescription = findViewById(R.id.tv_product_description);
        tvProductPrice = findViewById(R.id.tv_product_price);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        // Ví dụ: Tải thông tin sản phẩm (cần truyền productId qua Intent)
        tvProductName.setText("Product 1");
        tvProductDescription.setText("Description of Product 1");
        tvProductPrice.setText("$10.00");

        btnAddToCart.setOnClickListener(v -> {
            // Logic thêm vào giỏ hàng (cần triển khai)
            Toast.makeText(this, "Added to Cart!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}