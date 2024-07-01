package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.DiscountResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseSupplier;
import com.mibodega.mystore.models.Responses.ProductResponseSupplierV2;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapterProductSupplier extends RecyclerView.Adapter<RecyclerViewAdapterProductSupplier.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ProductResponseSupplierV2> productList = new ArrayList<>();
    private SaleTemporalList saleTemporalList  = new SaleTemporalList();
    private Map<String,EditText> mapEditCost = new HashMap<>();
    private Map<String,EditText> mapEditAmount = new HashMap<>();

    private Context context;
    private View.OnClickListener listener;
    private int type;
    final RecyclerViewAdapterProductSupplier.OnDeleteItem onDeleteItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDeleteItem {
        void onClick(ProductResponseSupplierV2 item);
    }
    public RecyclerViewAdapterProductSupplier(int type,Context context, ArrayList<ProductResponseSupplierV2> productList, RecyclerViewAdapterProductSupplier.OnDeleteItem onDeleteItem) {
        this.context = context;
        this.productList = productList;
        this.onDeleteItem = onDeleteItem;
        this.type = type;

    }

    public void setFilteredList(ArrayList<ProductResponseSupplierV2> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterProductSupplier.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_supplier, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterProductSupplier.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterProductSupplier.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductResponseSupplierV2 item = productList.get(position);
        holder.tv_name.setText(item.getName());


        Glide.with(context)
                .asBitmap()
                .load(item.getImage_url())
                .error(R.drawable.no_photo)
                .override(convertDpToPixel(150, context), convertDpToPixel(100, context))
                .centerCrop()
                .transform(new RoundedCorners(20))
                .into(holder.imageProduct);

        holder.tv_stock.setText(String.valueOf(item.getStock()));
        holder.edt_amount.setText("1");
        holder.edt_priceCost.setText(String.valueOf(item.getCost()));
        holder.tv_priceVenta.setText("s/ " +String.valueOf(item.getPrice()));
        double total_price = item.getCost()*Integer.parseInt(holder.edt_amount.getText().toString());
        holder.tv_totalPrice.setText("s/. "+total_price);
        holder.tv_cost.setText("s/ "+item.getCost());


        mapEditCost.put(item.getCode(),holder.edt_priceCost);
        mapEditAmount.put(item.getCode(),holder.edt_amount);
        if(type==1){
            holder.ly_totalContainer.setVisibility(View.GONE);
            holder.ly_amountContainer.setVisibility(View.GONE);
        }else if(type==2){
            holder.ly_totalContainer.setVisibility(View.VISIBLE);
            holder.ly_amountContainer.setVisibility(View.VISIBLE);
            holder.ly_priceContainer.setVisibility(View.GONE);
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        productList.remove(item);
                        notifyDataSetChanged();
                onDeleteItem.onClick(item);
            }
        });
        holder.edt_priceCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    double total_price = item.getCost() * Integer.parseInt(holder.edt_amount.getText().toString());
                    holder.tv_totalPrice.setText("s/. " + total_price);
                }
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
    public Map<String,EditText> getMapEditCost(){
        return mapEditCost;
    }
    public Map<String,EditText> getMapEditAmount(){
        return mapEditAmount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageButton btn_delete;
        ImageView imageProduct;
        TextView tv_stock;
        TextView tv_priceVenta,tv_cost;
        TextView tv_totalPrice;
        LinearLayout ly_totalContainer, ly_inputContainer, ly_priceContainer,ly_amountContainer;
        EditText edt_priceCost, edt_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.Tv_productName_product_supplier);
            btn_delete = itemView.findViewById(R.id.Imgb_deleteProduct_product_supplier);
            imageProduct = itemView.findViewById(R.id.Imgv_productImage_product_supplier);
            tv_stock = itemView.findViewById(R.id.Tv_stockProduct_product_supplier);
            tv_priceVenta = itemView.findViewById(R.id.Tv_priceProduct_product_supplier);
            tv_totalPrice = itemView.findViewById(R.id.Tv_totalPrice_product_supplier);
            ly_totalContainer = itemView.findViewById(R.id.Ly_totalContainer_product_supplier);
            ly_priceContainer = itemView.findViewById(R.id.Ly_containerPrice_product_supplier);
            ly_amountContainer = itemView.findViewById(R.id.Ly_containerAmount_product_supplier);
            ly_inputContainer = itemView.findViewById(R.id.Ly_containerInputs_supplier);
            tv_cost = itemView.findViewById(R.id.Tv_cost_product_supplier);
            edt_amount = itemView.findViewById(R.id.Edt_amountProduct_supplier);
            edt_priceCost = itemView.findViewById(R.id.edt_pricetProduct_supplier);

        }
    }
}
