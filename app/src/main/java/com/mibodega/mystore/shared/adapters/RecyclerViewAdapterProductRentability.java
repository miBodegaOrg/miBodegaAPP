package com.mibodega.mystore.shared.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductRentabilityResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;

import java.util.ArrayList;


public class RecyclerViewAdapterProductRentability  extends RecyclerView.Adapter<RecyclerViewAdapterProductRentability.ProductViewHolder> {

    private ArrayList<ProductRentabilityResponse> products;

    public RecyclerViewAdapterProductRentability(ArrayList<ProductRentabilityResponse> products) {
        this.products = products;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView profitabilityValue;

        public ProductViewHolder(View view) {
            super(view);
            productImage = view.findViewById(R.id.productImage);
            productName = view.findViewById(R.id.productName);
            profitabilityValue = view.findViewById(R.id.profitabilityValue);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product_rentability, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductRentabilityResponse product = products.get(position);
        holder.productName.setText(product.getName());
        holder.profitabilityValue.setText(String.format("$%.2f", product.getRentability()));
    }

    public void setFilteredList(ArrayList<ProductRentabilityResponse> filteredList) {
        this.products = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
}
