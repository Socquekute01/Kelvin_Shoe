package com.example.kelvinshoe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.CartItem;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.utils.DataManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
        void onFavoriteToggled(CartItem item, boolean isFavorite);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName;
        private TextView tvProductDescription;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private ImageView btnDecrease;
        private ImageView btnIncrease;
        private ImageView btnFavorite;
        private ImageView btnRemove;

        private boolean isFavorite = false;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductDescription = itemView.findViewById(R.id.tv_product_description);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(CartItem item) {
            setProductInfo(item);

            // Set quantity
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            setupClickListeners(item);
        }

        private void setProductInfo(CartItem item) {
            // Giả lập thông tin sản phẩm dựa trên productId
            DataManager dataManager = new DataManager(context);
            Product product = dataManager.getProductById(item.getProductId());
            tvProductName.setText(product.getName());
            tvProductDescription.setText(product.getDescription());
            Glide.with(context).load(product.getImageUrl()).into(imgProduct);
            tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
        }
        private void setupClickListeners(CartItem item) {
            // Decrease quantity
            btnDecrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    item.setQuantity(newQuantity);
                    tvQuantity.setText(String.valueOf(newQuantity));
                    if (listener != null) {
                        listener.onQuantityChanged(item, newQuantity);
                    }
                } else {
                    // Remove item if quantity becomes 0
                    if (listener != null) {
                        listener.onQuantityChanged(item, 0);
                    }
                }
            });

            // Increase quantity
            btnIncrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                int newQuantity = currentQuantity + 1;
                item.setQuantity(newQuantity);
                tvQuantity.setText(String.valueOf(newQuantity));
                if (listener != null) {
                    listener.onQuantityChanged(item, newQuantity);
                }
            });

            // Toggle favorite
            btnFavorite.setOnClickListener(v -> {
                isFavorite = !isFavorite;
                if (isFavorite) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_border);
                }
                if (listener != null) {
                    listener.onFavoriteToggled(item, isFavorite);
                }
            });

            // Remove item
            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemoved(item);
                }
            });
        }
    }
}