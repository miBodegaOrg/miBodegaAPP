package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;

public class RecyclerViewAdapterSupplier extends RecyclerView.Adapter<RecyclerViewAdapterSupplier.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<SupplierResponseV2> supplierList = new ArrayList<>();

    private int viewType_;
    private Context context;
    private View viewToast;
    private View.OnClickListener listener;
    final RecyclerViewAdapterSupplier.OnDetailItem onDetailItem;
    final RecyclerViewAdapterSupplier.OnManageItem onManageItem;


    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(SupplierResponseV2 item);
    }
    public interface OnManageItem {
        void onClick(SupplierResponseV2 item);
    }

    public RecyclerViewAdapterSupplier(Context context, ArrayList<SupplierResponseV2> supplierList, RecyclerViewAdapterSupplier.OnDetailItem onDetailItem, RecyclerViewAdapterSupplier.OnManageItem onManageItem) {
        this.context = context;
        this.supplierList = supplierList;
        this.onDetailItem = onDetailItem;
        this.onManageItem = onManageItem;
    }

    public void setFilteredList(ArrayList<SupplierResponseV2> filteredList) {
        this.supplierList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterSupplier.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_supplier, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterSupplier.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSupplier.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SupplierResponseV2 supplier = supplierList.get(position);
/*
        Glide.with(context)
                .asBitmap()
                .load(supplier.getImage_url())
                .error(R.drawable.no_photo)
                .override(convertDpToPixel(150, context), convertDpToPixel(100, context))
                .centerCrop()
                .transform(new RoundedCorners(20))
                .into(holder.image);*/

        holder.name.setText(supplier.getName());

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailItem.onClick(supplier);
            }
        });
        holder.btn_gestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onManageItem.onClick(supplier);
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
        return supplierList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        Button btn_details;
        Button btn_gestion;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Imgv_iconSupplier_supplier_item);
            image.setScaleType(ImageView.ScaleType.FIT_XY);

            name = itemView.findViewById(R.id.Tv_nameSupplier_supplier_item);

            btn_details = itemView.findViewById(R.id.Btn_showDetails_supplier_item);
            btn_gestion = itemView.findViewById(R.id.Btn_manageSupplier_supplier_item);


        }
    }


}
