package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.DiscountResponse;
import com.mibodega.mystore.models.common.ProductSale;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapterSaleDetail extends RecyclerView.Adapter<RecyclerViewAdapterSaleDetail.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ProductSale> productList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterSaleDetail.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(ProductSale item);
    }
    public RecyclerViewAdapterSaleDetail(Context context, ArrayList<ProductSale> productList, RecyclerViewAdapterSaleDetail.OnDetailItem onDetailItem) {
        this.context = context;
        this.productList = productList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<ProductSale> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterSaleDetail.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sale_product_detail, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterSaleDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSaleDetail.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductSale item = productList.get(position);

        holder.tv_name.setText(item.getName());
        holder.tv_code.setText(item.getCode().toString());

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formatteq = decimalFormat.format(item.getQuantity());
        holder.tv_quantity.setText(formatteq);

        DecimalFormat decimalFormaet = new DecimalFormat("#0.00");
        String formatteqr = decimalFormaet.format(item.getPrice());
        holder.tv_price.setText(formatteqr);

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
        TextView tv_code;
        TextView tv_quantity;
        TextView tv_price;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_sale_detail);
            tv_code = itemView.findViewById(R.id.tv_code_sale_detail);
            tv_quantity = itemView.findViewById(R.id.tv_quantity_sale_detail);
            tv_price = itemView.findViewById(R.id.tv_price_sale_detail);



        }
    }
}
