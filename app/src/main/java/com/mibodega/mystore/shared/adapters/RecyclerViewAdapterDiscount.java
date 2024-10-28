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
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;

public class RecyclerViewAdapterDiscount extends RecyclerView.Adapter<RecyclerViewAdapterDiscount.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<DiscountResponse> discountList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterDiscount.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(DiscountResponse item);
    }
    public RecyclerViewAdapterDiscount(Context context, ArrayList<DiscountResponse> discountList, RecyclerViewAdapterDiscount.OnDetailItem onDetailItem) {
        this.context = context;
        this.discountList = discountList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<DiscountResponse> filteredList) {
        this.discountList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterDiscount.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_discount, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterDiscount.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterDiscount.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DiscountResponse item = discountList.get(position);
        holder.tv_name.setText(item.getName());
        holder.tv_percentage.setText("Descuento: "+String.valueOf(item.getPercentage())+" %");
        holder.tv_value.setText("valor: S/ "+item.getValue());
        String active ="INACTIVO";
        if(item.isActive())active="ACTIVO";
        holder.tv_status.setText(active);
        holder.btn_manageDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailItem.onClick(item);
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
        return discountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_percentage;
        TextView tv_value;
        TextView tv_status;
        Button btn_manageDiscount;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.Tv_nameDiscount_discount_item);
            tv_percentage = itemView.findViewById(R.id.Tv_percentageDiscount_discount_item);
            tv_value = itemView.findViewById(R.id.Tv_valueDiscount_discount_item);
            tv_status = itemView.findViewById(R.id.Tv_statusDiscount_discount_item);
            btn_manageDiscount = itemView.findViewById(R.id.Btn_manageDiscount_discount_item);


        }
    }
}

