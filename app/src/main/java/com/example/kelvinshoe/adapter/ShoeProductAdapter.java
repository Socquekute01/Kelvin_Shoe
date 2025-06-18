package com.example.kelvinshoe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.Product;
import com.example.kelvinshoe.view.ProductDetailsActivity;

import java.util.List;

public class ShoeProductAdapter extends ArrayAdapter<Product> {

    public ShoeProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shoe_product, parent, false);
            holder = new ViewHolder();
            holder.cvProduct = convertView.findViewById(R.id.cv_product);
            holder.ivProductImage = convertView.findViewById(R.id.iv_product_image);
            holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
            holder.tvProductPrice = convertView.findViewById(R.id.tv_product_price);
            holder.tvProductStock = convertView.findViewById(R.id.tv_product_stock);
            holder.tvProductCategory = convertView.findViewById(R.id.tv_product_category);
            holder.ivFavorite = convertView.findViewById(R.id.iv_favorite);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product currentProduct = getItem(position);

        if (currentProduct != null) {
            // Set product name
            holder.tvProductName.setText(currentProduct.getName());

            // Set product price
            holder.tvProductPrice.setText(String.format("$%.2f", currentProduct.getPrice()));

            // Set stock status
            if (currentProduct.getStock() > 0) {
                holder.tvProductStock.setText("Còn " + currentProduct.getStock() + " đôi");
                holder.tvProductStock.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.tvProductStock.setText("Hết hàng");
                holder.tvProductStock.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
            }

            // Set category based on product name (you can modify this logic)
            String category = getCategoryFromProductName(currentProduct.getName());
            holder.tvProductCategory.setText(category);

            // Set product image (you can add image loading logic here)
            setProductImage(holder.ivProductImage, currentProduct.getName());

            // Handle favorite button click
            holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle favorite state
                    ImageView favoriteIcon = (ImageView) v;
                    // You can implement favorite logic here
                    // For now, just toggle the icon
                    toggleFavoriteIcon(favoriteIcon);
                }
            });

            holder.cvProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("productId", currentProduct.getProductId());
                    v.getContext().startActivity(intent);
                }
            });
        }

        return convertView;
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
        // This is a simple example - you should implement proper image loading
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
        // Simple toggle - you should implement proper favorite state management
        Object tag = favoriteIcon.getTag();
        if (tag != null && tag.equals("favorited")) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
            favoriteIcon.setTag("not_favorited");
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_filled);
            favoriteIcon.setTag("favorited");
        }
    }

    private static class ViewHolder {
        CardView cvProduct;
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductStock;
        TextView tvProductCategory;
        ImageView ivFavorite;
    }
}