package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapterSale extends RecyclerView.Adapter<RecyclerViewAdapterSale.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<SaleResponse> saleList = new ArrayList<>();

    private int viewType_;
    private Context context;
    private View viewToast;
    private View.OnClickListener listener;
    final RecyclerViewAdapterSale.OnDetailItem onDetailItem;
    final RecyclerViewAdapterSale.OnCancelItem onCancelItem;;
    final RecyclerViewAdapterSale.OnPayItem onPayItem;



    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(SaleResponse sale);
    }
    public interface OnCancelItem {
        void onClick(SaleResponse sale);
    }
    public interface OnPayItem {
        void onClick(SaleResponse sale);
    }

    public RecyclerViewAdapterSale(Context context, ArrayList<SaleResponse> saleList, RecyclerViewAdapterSale.OnDetailItem onDetailItem, OnCancelItem onSupplierItem,RecyclerViewAdapterSale.OnPayItem onPayItem) {
        this.context = context;
        this.saleList = saleList;


        this.onDetailItem = onDetailItem;
        this.onCancelItem = onSupplierItem;
        this.onPayItem = onPayItem;
    }

    public void setFilteredList(ArrayList<SaleResponse> filteredList) {
        this.saleList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterSale.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sale, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterSale.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSale.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SaleResponse sale = saleList.get(position);
        holder.date.setText(utils.convertDateToClearFormat(sale.getCreatedAt().toString()));
        holder.amountProduct.setText("Product: "+sale.getProducts().size());

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedIgv = decimalFormat.format(sale.getIgv());
        holder.igv.setText(formattedIgv);

        holder.state.setText(sale.getStatus());
        holder.idSale.setText(sale.get_id());

        DecimalFormat decimalFormat2 = new DecimalFormat("#0.00");
        String formattedtotal = decimalFormat2.format(sale.getTotal());
        holder.total.setText(formattedtotal);

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailItem.onClick(sale);
            }
        });
        holder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPayItem.onClick(sale);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelItem.onClick(sale);
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
        return saleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView amountProduct;
        TextView igv;
        TextView state;
        TextView idSale;
        TextView total;

        Button btn_pay;
        Button btn_cancel;
        Button btn_details;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Tv_dateSale_item_sale);
            amountProduct = itemView.findViewById(R.id.Tv_amuntSaleProduct_item_sale);
            igv = itemView.findViewById(R.id.Tv_igvSale_item_sale);
            state = itemView.findViewById(R.id.Tv_stateSale_item_sale);
            idSale = itemView.findViewById(R.id.Tv_idSale_item_sale);
            total = itemView.findViewById(R.id.Tv_totalSale_item_sale);
            btn_pay = itemView.findViewById(R.id.Btn_paySale_item_sale);
            btn_cancel = itemView.findViewById(R.id.Btn_cancelSale_item_sale);
            btn_details = itemView.findViewById(R.id.Btn_details_item_sale);


        }
    }

}