package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecyclerViewAdapterProduct extends RecyclerView.Adapter<RecyclerViewAdapterProduct.ViewHolder> implements View.OnClickListener {
   private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ProductResponse> productList = new ArrayList<>();

    private int viewType_;
    private Context context;
    private Resources resources;
    private View viewToast;
    private View.OnClickListener listener;
    final RecyclerViewAdapterProduct.OnDetailItem onDetailItem;
    final RecyclerViewAdapterProduct.OnSupplierItem onSupplierItem;


    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(ProductResponse product);
    }
    public interface OnSupplierItem {
        void onClick(ProductResponse product);
    }

    public RecyclerViewAdapterProduct(Resources resources, Context context, int viewType, ArrayList<ProductResponse> productList, RecyclerViewAdapterProduct.OnDetailItem onDetailItem, RecyclerViewAdapterProduct.OnSupplierItem onSupplierItem) {
        this.context = context;
        this.viewType_ = viewType;
        this.productList = productList;

        this.resources = resources;
        this.onDetailItem = onDetailItem;
        this.onSupplierItem = onSupplierItem;
    }

    public void setFilteredList(ArrayList<ProductResponse> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType_) {
            case 1: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
                return new ViewHolder(view);
            }
            case 2: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
                return new ViewHolder(view);
            }
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductResponse product = productList.get(position);
        holder.productCodigo.setText("codigo: " + product.getCode());
        holder.productDescription.setText(product.getName());
        holder.productStock.setText(product.getStock());
        holder.productLevel.setText("ALTO");
        holder.productState.setText("ACTIVO");
        holder.buyPrice.setText("s/ " +String.valueOf(product.getPrice()));
        holder.sellPrice.setText("s/ " +String.valueOf(product.getPrice()));

        Glide.with(context).
                asBitmap().
                load(product.getImage_url()).
                error(R.drawable.no_photo).
                override(convertDpToPixel(150, context), convertDpToPixel(100, context)).
                into(holder.image);


        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailItem.onClick(product);
            }
        });
        holder.btn_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupplierItem.onClick(product);
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
        ImageView image;
        TextView productCodigo;
        TextView productDescription;
        TextView productStock;
        TextView productState;
        TextView productLevel;
        TextView buyPrice;
        TextView sellPrice;

        Button btn_details;
        Button btn_supplier;

        private OnSupplierItem onSupplierItem;
        private OnDetailItem onDetailItem;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Imv_productImage_product);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            productDescription = itemView.findViewById(R.id.Tv_productName_product);
            productCodigo = itemView.findViewById(R.id.Tv_productCode_product);

            btn_details = itemView.findViewById(R.id.Btn_detailProduct_product);
            btn_supplier = itemView.findViewById(R.id.Btn_supplierProduct_product);
            productStock = itemView.findViewById(R.id.Tv_productStock_product);
            productState = itemView.findViewById(R.id.Tv_productState_product);
            productLevel = itemView.findViewById(R.id.Tv_productLevel_product);
            buyPrice     = itemView.findViewById(R.id.Tv_buyPriceProduct_product);
            sellPrice    = itemView.findViewById(R.id.Tv_sellPriceProduct_product);

        }
    }


}

