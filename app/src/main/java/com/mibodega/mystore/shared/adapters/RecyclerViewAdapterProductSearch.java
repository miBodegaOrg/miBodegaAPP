package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;

public class RecyclerViewAdapterProductSearch extends RecyclerView.Adapter<RecyclerViewAdapterProductSearch.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ProductResponse> productList = new ArrayList<>();

    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterProductSearch.OnItem onItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }
    public interface OnItem {
        void onClick(ProductResponse product);
    }

    public RecyclerViewAdapterProductSearch(Context context, ArrayList<ProductResponse> productList, RecyclerViewAdapterProductSearch.OnItem onItem) {
        this.context = context;
        this.productList = productList;
        this.onItem = onItem;
    }

    public void setFilteredList(ArrayList<ProductResponse> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerViewAdapterProductSearch.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_search, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterProductSearch.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterProductSearch.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductResponse product = productList.get(position);
        holder.tv_name.setText(product.getName().toString());
        holder.tv_stock.setText(String.valueOf(product.getStock()));
       holder.tv_price.setText("S/ " +utils.formatDecimal(product.getPrice()));
        holder.ly_item_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItem.onClick(product);
            }
        });

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_stock;
        TextView tv_price;
        LinearLayout ly_item_product;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.Tv_nameProduct_search_item);
            tv_stock = itemView.findViewById(R.id.Tv_stockProduct_search_item);
            tv_price = itemView.findViewById(R.id.Tv_priceProduct_search_item);
            ly_item_product = itemView.findViewById(R.id.Ly_itemSearchProduct_sale);

        }
    }


}
