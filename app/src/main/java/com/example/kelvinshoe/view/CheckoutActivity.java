package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.CartItem;
import com.example.kelvinshoe.utils.DataManager;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutActivity extends AppCompatActivity {
    Button btnCheckout;
    TextView tvAmount, tvTransaction, tvTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        btnCheckout = findViewById(R.id.continue_button);
        tvAmount = findViewById(R.id.amount);
        tvTransaction = findViewById(R.id.transaction_id);
        tvTime = findViewById(R.id.date_time);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProductListActivity.class);
                startActivity(intent);
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int userId = Integer.parseInt(sharedPreferences.getString("userId", "-1"));

                DataManager dataManager = new DataManager(getBaseContext());
                List<CartItem> cartItemList = dataManager.getAllCartItems(userId);

                List<Integer> cartIds = cartItemList.stream()
                        .map(CartItem::getCartId)
                        .collect(Collectors.toList());
                for (int cartId : cartIds) {
                    dataManager.deleteCartItem(cartId);
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String price = bundle.getString("price", "0");
        tvAmount.setText(String.format("$%.2f", Double.valueOf(price)));
        tvTime.setText(String.valueOf(LocalDateTime.now()));
    }
}
