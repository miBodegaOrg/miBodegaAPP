package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapterProductSale extends RecyclerView.Adapter<RecyclerViewAdapterProductSale.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ProductResponse> productList = new ArrayList<>();
    private Context context;
    private SaleTemporalList saleTemporalList  = new SaleTemporalList();
    private Map<String,Boolean> arrIsEdit = new HashMap<>();
    private Map<String,EditText> arrEdtAmount = new HashMap<>();
    private View.OnClickListener listener;
    final RecyclerViewAdapterProductSale.OnEdit onEdit;
    final RecyclerViewAdapterProductSale.OnDelete onDelete;




    @Override
    public void onClick(View v) {
            if (listener != null) {
            listener.onClick(v);
            }
    }

    public interface OnDelete {
        void onClick(ProductResponse product);
    }
    public interface OnEdit {
        void onClick(ProductResponse product);
    }


        public RecyclerViewAdapterProductSale(Context context, ArrayList<ProductResponse> productList, OnEdit onEdit, OnDelete onDelete) {
            this.context = context;
            this.productList = productList;
            this.onEdit = onEdit;
            this.onDelete = onDelete;
            for (ProductResponse product:productList){
                arrIsEdit.put(product.getCode(),false);
            }

        }

        public void setFilteredList(ArrayList<ProductResponse> filteredList) {
            this.productList = filteredList;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public RecyclerViewAdapterProductSale.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sale_product, parent, false);
            view.setOnClickListener(this);
            return new RecyclerViewAdapterProductSale.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapterProductSale.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ProductResponse product = productList.get(position);


            Glide.with(context)
                    .asBitmap()
                    .load(product.getImage_url())
                    .error(R.drawable.no_photo)
                    .override(convertDpToPixel(150, context), convertDpToPixel(100, context))
                    .centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(holder.image);
            arrEdtAmount.put(product.getCode(),holder.edt_amount);
            holder.productDescription.setText(product.getName());
            holder.productStock.setText(String.valueOf(product.getStock()));
            holder.buyPrice.setText("s/ " +String.valueOf(product.getPrice()));
            if(product.isWeight()){ holder.edt_amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);}else{holder.edt_amount.setInputType(InputType.TYPE_CLASS_NUMBER); }
            Number amountNum = saleTemporalList.getMapAmountProduct().get(product.getCode());


            double _total_price=0.0;
            if(product.isWeight()){
                holder.edt_amount.setText(String.valueOf(amountNum.doubleValue()));
                _total_price = product.getPrice() * Double.valueOf(holder.edt_amount.getText().toString());

            }else{
                holder.edt_amount.setText(String.valueOf(amountNum.intValue()));
                _total_price = product.getPrice() * Integer.parseInt(holder.edt_amount.getText().toString());

            }

            holder.total_price.setText("s/. "+utils.formatDecimal(_total_price));

            holder.edt_amount.setEnabled(false);


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
                        double total_price =0.0;
                        if(product.isWeight()){
                            total_price = product.getPrice() * Double.valueOf(holder.edt_amount.getText().toString());

                        }else{
                            total_price = product.getPrice() * Integer.parseInt(holder.edt_amount.getText().toString());

                        }
                       holder.total_price.setText("s/. " + utils.formatDecimal(total_price));
                    }
                }
            });

            holder.btn_edit_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Boolean.TRUE.equals(arrIsEdit.get(product.getCode()))){
                        Double amount = 0.0;

                        if(product.isWeight()){
                            amount = Double.valueOf(holder.edt_amount.getText().toString());

                        }else{
                            amount = 1.0*Integer.parseInt(holder.edt_amount.getText().toString());

                        }
                        if(amount.doubleValue()>0){
                            holder.edt_amount.setEnabled(false);

                            saleTemporalList.updateAmountProduct(product.getCode(),amount);

                            holder.btn_edit_save.setImageResource(R.drawable.baseline_edit_24);
                            arrIsEdit.put(product.getCode(),false);
                            //notifyDataSetChanged();
                        }
                    }else {
                        holder.edt_amount.setEnabled(true);
                        holder.btn_edit_save.setImageResource(R.drawable.baseline_save_24);
                        arrIsEdit.put(product.getCode(),true);
                    }
                    onEdit.onClick(product);
                }
            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saleTemporalList.removeProduct(product);
                    productList.remove(product);
                    onDelete.onClick(product);
                    notifyDataSetChanged();
                }
            });



        }
        public Map<String,EditText> getMapEditAmount(){
            return arrEdtAmount;
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
        TextView productDescription;
        TextView productStock;
        TextView buyPrice;
        EditText edt_amount;
        TextView total_price;


        ImageButton btn_delete;
        ImageButton btn_edit_save;
        ImageButton btn_cancel;



        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Imgv_productImage_sale_item);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            productDescription = itemView.findViewById(R.id.Tv_productName_sale);

            btn_delete = itemView.findViewById(R.id.Imgb_deleteProduct_sale_item);
            btn_edit_save = itemView.findViewById(R.id.Imgb_editSaveProduct_sale);
            btn_cancel = itemView.findViewById(R.id.Imgb_cancelEdit_sale);

            productStock = itemView.findViewById(R.id.Tv_stockProduct_item_sale);
            buyPrice     = itemView.findViewById(R.id.Tv_priceProduct_item_sale);

            total_price = itemView.findViewById(R.id.Tv_totalPrice_item_sale);
            edt_amount = itemView.findViewById(R.id.Edt_amountProduct_sale);



        }
    }


}
