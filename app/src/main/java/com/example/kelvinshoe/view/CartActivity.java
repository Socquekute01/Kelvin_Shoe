package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.adapter.CartAdapter;
import com.example.kelvinshoe.model.CartItem;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {

    private RecyclerView recyclerCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    private TextView tvSubtotal;
    private TextView tvShippingFee;
    private TextView tvDiscount;
    private TextView tvTotal;
    private EditText editPromoCode;
    private Button btnApplyPromo;
    private Button btnCheckout;
    private ImageView btnBack;

    private double subtotal = 0;
    private double shippingFee = 30000;
    private double discount = 0;
    private double total = 0;

    private NumberFormat currencyFormat;

    private DataManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbManager = new DataManager(this);

        initViews();
        setupRecyclerView();
        loadCartData();
        setupClickListeners();

        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    private void initViews() {
        recyclerCartItems = findViewById(R.id.recycler_cart_items);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvShippingFee = findViewById(R.id.tv_shipping_fee);
        tvDiscount = findViewById(R.id.tv_discount);
        tvTotal = findViewById(R.id.tv_total);
        editPromoCode = findViewById(R.id.edit_promo_code);
        btnApplyPromo = findViewById(R.id.btn_apply_promo);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems, this);
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerCartItems.setAdapter(cartAdapter);
    }

    private void loadCartData() {
        // Giả lập dữ liệu giỏ hàng
        // Trong thực tế, bạn sẽ load từ database hoặc SharedPreferences
        cartItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(sharedPreferences.getString("userId", "-1"));

        cartItems = dbManager.getAllCartItems(userId);
//        // Thêm một số sản phẩm mẫu
//        cartItems.add(new CartItem(1, 1, 1, 1, "Nike Air Max 270 - Size 42 - Đen"));
//        cartItems.add(new CartItem(2, 1, 2, 2, "Adidas Ultraboost 22 - Size 41 - Trắng"));
//        cartItems.add(new CartItem(3, 1, 3, 1, "Converse Chuck Taylor - Size 43 - Đỏ"));

        cartAdapter.notifyDataSetChanged();
        calculateTotal();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnApplyPromo.setOnClickListener(v -> {
            String promoCode = editPromoCode.getText().toString().trim();
            applyPromoCode(promoCode);
        });

        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đến màn hình thanh toán
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("total_amount", total);
            intent.putExtra("cart_items_count", cartItems.size());
            startActivity(intent);
        });
    }

    private void applyPromoCode(String promoCode) {
        if (promoCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả lập logic áp dụng mã giảm giá
        switch (promoCode.toUpperCase()) {
            case "WELCOME10":
                discount = subtotal * 0.1; // Giảm 10%
                Toast.makeText(this, "Áp dụng mã giảm giá thành công! Giảm 10%", Toast.LENGTH_SHORT).show();
                break;
            case "FREESHIP":
                discount = shippingFee; // Miễn phí ship
                Toast.makeText(this, "Áp dụng mã miễn phí vận chuyển thành công!", Toast.LENGTH_SHORT).show();
                break;
            case "SAVE50K":
                discount = Math.min(50000, subtotal * 0.05); // Giảm tối đa 50k
                Toast.makeText(this, "Áp dụng mã giảm 50k thành công!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
        }

        calculateTotal();
        editPromoCode.setText("");
    }

    private void calculateTotal() {
        subtotal = 0;

        // Tính tổng tiền sản phẩm (giả lập giá)
        for (CartItem item : cartItems) {
            double itemPrice = getProductPrice(item.getProductId());
            subtotal += itemPrice * item.getQuantity();
        }

        total = subtotal + shippingFee - discount;
        if (total < 0) total = 0;

        updatePriceDisplay();
    }

    private double getProductPrice(int productId) {
        // Giả lập giá sản phẩm
        switch (productId) {
            case 1: return 2500000; // Nike Air Max 270
            case 2: return 3200000; // Adidas Ultraboost 22
            case 3: return 1800000; // Converse Chuck Taylor
            default: return 1000000;
        }
    }

    private void updatePriceDisplay() {
        tvSubtotal.setText(formatCurrency(subtotal));
        tvShippingFee.setText(formatCurrency(shippingFee));
        tvDiscount.setText("-" + formatCurrency(discount));
        tvTotal.setText(formatCurrency(total));
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(amount) + "đ";
    }

    // Implement CartAdapter.OnCartItemListener
    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            cartItems.remove(item);
            cartAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
        } else {
            item.setQuantity(newQuantity);
        }
        calculateTotal();
    }

    @Override
    public void onItemRemoved(CartItem item) {
        cartItems.remove(item);
        cartAdapter.notifyDataSetChanged();
        calculateTotal();
        Toast.makeText(this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteToggled(CartItem item, boolean isFavorite) {
        // Xử lý thêm/bỏ yêu thích
        String message = isFavorite ? "Đã thêm vào yêu thích" : "Đã bỏ khỏi yêu thích";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cart data khi quay lại màn hình
        loadCartData();
    }
}