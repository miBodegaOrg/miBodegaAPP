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
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerViewAdapterPurchase extends RecyclerView.Adapter<RecyclerViewAdapterPurchase.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<PurchaseResponse> purchaseList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterPurchase.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(PurchaseResponse item);
    }
    public RecyclerViewAdapterPurchase(Context context, ArrayList<PurchaseResponse> purchaseList, RecyclerViewAdapterPurchase.OnDetailItem onDetailItem) {
        this.context = context;
        this.purchaseList = purchaseList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<PurchaseResponse> filteredList) {
        this.purchaseList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterPurchase.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_purchase, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterPurchase.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPurchase.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PurchaseResponse item = purchaseList.get(position);
        String formattedDate = utils.convertDateToClearFormat(item.getCreatedAt().toString());
        holder.date.setText(formattedDate);
        if(Objects.equals(item.getStatus(), "received")){
            holder.status.setText("Recibido");
        }else{
            holder.status.setText("En progreso");
        }
        holder.amount.setText("Cantidad: "+item.getProducts().size());
        holder.total.setText("Total: S/ "+utils.formatDecimal(item.getTotal()));
        holder.btn_validatePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailItem.onClick(item);
            }
        });
        if(Objects.equals(item.getStatus(), "received")){
            holder.btn_validatePurchase.setVisibility(View.GONE);
        }

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView amount;
        TextView total;
        TextView status;
        Button btn_validatePurchase;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Tv_datePurchase_purchase_item);
            amount = itemView.findViewById(R.id.Tv_amountPurchase_purchase_item);
            total = itemView.findViewById(R.id.Tv_totalPurchase_purchase_item);
            status = itemView.findViewById(R.id.Tv_statusPurchase_purchase_item);
            btn_validatePurchase = itemView.findViewById(R.id.Btn_validatePurchase_purchase_item);


        }
    }
}
