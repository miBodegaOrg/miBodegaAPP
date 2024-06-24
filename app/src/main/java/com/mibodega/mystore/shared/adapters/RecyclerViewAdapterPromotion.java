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
import com.mibodega.mystore.models.Responses.PromotionResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapterPromotion extends RecyclerView.Adapter<RecyclerViewAdapterPromotion.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<PromotionResponse> promotionList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterPromotion.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(PromotionResponse item);
    }
    public RecyclerViewAdapterPromotion(Context context, ArrayList<PromotionResponse> promotionList, RecyclerViewAdapterPromotion.OnDetailItem onDetailItem) {
        this.context = context;
        this.promotionList = promotionList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<PromotionResponse> filteredList) {
        this.promotionList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterPromotion.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promotion, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterPromotion.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPromotion.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PromotionResponse item = promotionList.get(position);
        holder.tv_name.setText(item.getName());
        String formattedDate = utils.convertDateToClearFormat(item.getStartDate().toString());
        String formattedDate2 = utils.convertDateToClearFormat(item.getEndDate().toString());
        holder.tv_dateStart.setText("fecha I.: "+formattedDate);
        holder.tv_dateEnd.setText("fecha F.: "+formattedDate2);
        String active ="INACTIVO";
        if(item.isActive())active="ACTIVO";
        holder.tv_status.setText(active);
        holder.tv_amountPay.setText("Compra: "+item.getPay());
        holder.tv_amountReceiv.setText("Recibe: "+item.getBuy());
        holder.btn_managePromotion.setOnClickListener(new View.OnClickListener() {
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
        return promotionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_dateStart;
        TextView tv_dateEnd;
        TextView tv_amountPay;
        TextView tv_amountReceiv;
        TextView tv_status;
        Button btn_managePromotion;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.Tv_namePromotion_promotion_item);
            tv_dateStart = itemView.findViewById(R.id.Tv_dateStartPromotion_promotion_item);
            tv_dateEnd = itemView.findViewById(R.id.Tv_dateEndPromotion_promotion_item);
            tv_amountPay = itemView.findViewById(R.id.Tv_buyAmount_promotion);
            tv_amountReceiv = itemView.findViewById(R.id.Tv_receiveAmount_promotion);
            tv_status = itemView.findViewById(R.id.Tv_statusPromotion_promotion_item);
            btn_managePromotion = itemView.findViewById(R.id.Btn_managePromotion_promotion_item);


        }
    }
}

