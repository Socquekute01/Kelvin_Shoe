package com.example.kelvinshoe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.CartItem;

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
            // Set product info
            setProductInfo(item);

            // Set quantity
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            // Set price
            double price = getProductPrice(item.getProductId());
            tvProductPrice.setText(formatCurrency(price));

            // Set product image based on product type
            setProductImage(item.getProductId());

            // Set click listeners
            setupClickListeners(item);
        }

        private void setProductInfo(CartItem item) {
            // Giả lập thông tin sản phẩm dựa trên productId
            switch (item.getProductId()) {
                case 1:
                    tvProductName.setText("Nike Air Max 270");
                    tvProductDescription.setText("Giày thể thao nam");
                    break;
                case 2:
                    tvProductName.setText("Adidas Ultraboost 22");
                    tvProductDescription.setText("Giày chạy bộ");
                    break;
                case 3:
                    tvProductName.setText("Converse Chuck Taylor");
                    tvProductDescription.setText("Giày sneaker classic");
                    break;
                default:
                    tvProductName.setText("Sản phẩm");
                    tvProductDescription.setText(item.getDescription());
                    break;
            }
        }

        private void setProductImage(int productId) {
            // Set image based on product type
            switch (productId) {
                case 1:
                    imgProduct.setImageResource(R.drawable.ic_sport_shoe);
                    break;
                case 2:
                    imgProduct.setImageResource(R.drawable.ic_boot);
                    break;
                case 3:
                    imgProduct.setImageResource(R.drawable.ic_sandal);
                    break;
                default:
                    imgProduct.setImageResource(R.drawable.ic_default_shoe);
                    break;
            }
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

        private double getProductPrice(int productId) {
            // Giả lập giá sản phẩm
            switch (productId) {
                case 1: return 2500000; // Nike Air Max 270
                case 2: return 3200000; // Adidas Ultraboost 22
                case 3: return 1800000; // Converse Chuck Taylor
                default: return 1000000;
            }
        }

        private String formatCurrency(double amount) {
            return NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(amount) + "đ";
        }
    }
}