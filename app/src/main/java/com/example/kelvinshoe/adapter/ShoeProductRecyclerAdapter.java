package com.example.kelvinshoe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.view.ProductDetailsActivity;

import java.util.List;

public class ShoeProductRecyclerAdapter extends RecyclerView.Adapter<ShoeProductRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;

    public ShoeProductRecyclerAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoe_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentProduct = products.get(position);

        if (currentProduct != null) {
            // Set product name
            holder.tvProductName.setText(currentProduct.getName());

            // Set product price
            holder.tvProductPrice.setText(String.format("$%.2f", currentProduct.getPrice()));

            // Set stock status
            if (currentProduct.getStock() > 0) {
                holder.tvProductStock.setText("Còn " + currentProduct.getStock() + " đôi");
                holder.tvProductStock.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.tvProductStock.setText("Hết hàng");
                holder.tvProductStock.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }

            // Set category based on product name
            String category = getCategoryFromProductName(currentProduct.getName());
            holder.tvProductCategory.setText(category);

            // Set product image
            setProductImage(holder.ivProductImage, currentProduct.getName());

            // Handle favorite button click
            holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle favorite state
                    ImageView favoriteIcon = (ImageView) v;
                    toggleFavoriteIcon(favoriteIcon);
                }
            });

            // Handle card click to open product details
            holder.cvProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("productId", currentProduct.getProductId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    private String getCategoryFromProductName(String productName) {
        String name = productName.toLowerCase();
        if (name.contains("nike") || name.contains("adidas") || name.contains("running") || name.contains("sport")) {
            return "Thể thao";
        } else if (name.contains("boot") || name.contains("leather") || name.contains("oxford")) {
            return "Công sở";
        } else if (name.contains("sandal") || name.contains("flip")) {
            return "Dép";
        } else if (name.contains("high heel") || name.contains("pump")) {
            return "Cao gót";
        }
        return "Giày thường";
    }

    private void setProductImage(ImageView imageView, String productName) {
        String name = productName.toLowerCase();
        if (name.contains("nike") || name.contains("running") || name.contains("sport")) {
            imageView.setImageResource(R.drawable.ic_sport_shoe);
        } else if (name.contains("boot") || name.contains("leather")) {
            imageView.setImageResource(R.drawable.ic_boot);
        } else if (name.contains("sandal")) {
            imageView.setImageResource(R.drawable.ic_sandal);
        } else if (name.contains("high heel")) {
            imageView.setImageResource(R.drawable.ic_high_heel);
        } else {
            imageView.setImageResource(R.drawable.ic_default_shoe);
        }
    }

    private void toggleFavoriteIcon(ImageView favoriteIcon) {
        Object tag = favoriteIcon.getTag();
        if (tag != null && tag.equals("favorited")) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
            favoriteIcon.setTag("not_favorited");
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_filled);
            favoriteIcon.setTag("favorited");
        }
    }

    // Method to update data
    public void updateData(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    // Method to add single product
    public void addProduct(Product product) {
        if (products != null) {
            products.add(product);
            notifyItemInserted(products.size() - 1);
        }
    }

    // Method to remove product
    public void removeProduct(int position) {
        if (products != null && position >= 0 && position < products.size()) {
            products.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvProduct;
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;
        TextView tvProductCategory;
        ImageView ivFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvProduct = itemView.findViewById(R.id.cv_product);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductStock = itemView.findViewById(R.id.tv_product_stock);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
        }
    }
}