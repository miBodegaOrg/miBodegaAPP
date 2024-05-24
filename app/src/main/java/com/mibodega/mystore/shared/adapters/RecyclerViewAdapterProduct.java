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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mibodega.mystore.R;
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

/*
public class RecyclerViewAdapterProduct extends RecyclerView.Adapter<RecyclerViewAdapterProduct.ViewHolder> implements View.OnClickListener {
   private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<Producto> productList = new ArrayList<>();
    private ArrayList<Precios> preciosList = new ArrayList<>();
    private ArrayList<Unidades> unidadesList = new ArrayList<>();

    private Map<String, Boolean> mapProductAdded = new HashMap<>();
    private List<Integer> openDialogPositions = new ArrayList<>();
    private List<Integer> openDialogPromotionPositions = new ArrayList<>();
    private int viewType_;
    private ErrorServer ServerErrorHandler = new ErrorServer();
    private Context context;
    private Resources resources;
    private View viewToast;
    private DecimalFormat formatoDecimal = new DecimalFormat("0.00");
    private View.OnClickListener listener;
    final RecyclerViewAdapterProduct.OnAddItem onAddItem;
    private int radio_idselected;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnAddItem {
        void onClick(Producto product);
    }

    public RecyclerViewAdapterProduct(View view, Resources resources, Context context, int viewType, ArrayList<Producto> productList, ArrayList<Precios> preciosList,
                               ArrayList<Unidades> unidadesList, RecyclerViewAdapter.OnAddItem onAddItem) {
        this.context = context;
        this.viewType_ = viewType;
        this.productList = productList;

        for (int i = 0; i < productList.size(); ++i) {
            mapProductAdded.put(productList.get(i).getCodigo(), false);
        }

        this.preciosList = preciosList;
        this.unidadesList = unidadesList;
        this.resources = resources;
        this.viewToast = view;
        this.onAddItem = onAddItem;
    }

    public void setFilteredList(ArrayList<Producto> filteredList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_v2, parent, false);
                return new ViewHolder(view);
            }
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Producto product = productList.get(position);
        holder.productCodigo.setText("codigo: " + product.getCodigo());
        holder.productDescription.setText(product.getDescripcion());

        Glide.with(context).
                asBitmap().
                load(product.getImagen()).
                error(R.drawable.no_photo).
                override(convertDpToPixel(150, context), convertDpToPixel(100, context)).
                into(holder.image);


        holder.btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Boolean.TRUE.equals(mapProductAdded.get(product.getCodigo()))) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                    builder.setTitle("Añadir Producto")
                            .setMessage("¿Desea agregar nuevamente el producto?")
                            .setPositiveButton("SI", (dialog, which) -> {
                                addProductToCart(holder, product, arrayListUnidades);
                                dialog.dismiss();

                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    addProductToCart(holder, product, arrayListUnidades);
                }

            }
        });

        holder.radioGroup.clearCheck();
        holder.radioGroup.removeAllViews();
        holder.linearLayoutCant.removeAllViews();

        for (Unidades unidad : arrayListUnidades) {

            ArrayList<Precios> arrayListPrecios = new ArrayList<>();
            for (Precios precios : preciosList) {
                if (precios.getIDUnidad() == unidad.getID()) {
                    //Log.e("PRECIOS:",precios.toString());

                    if (!arrayListPrecios.contains(precios)) {
                        arrayListPrecios.add(precios);
                    } else {
                        System.out.println("El valor " + precios.toString() + " ya existe en el ArrayList.");
                    }

                }
            }

            Log.e("array:", arrayListPrecios.toString());

            int Stock = product.getStock() / unidad.getFactor();
            double Precio = arrayListPrecios.size() > 0 ? arrayListPrecios.get(0).getPrecio() : 0.0;

            if (Precio > 0){
                RadioButton radioButton = new RadioButton(context.getApplicationContext());
                radioButton.setText(unidad.getDescripcion() + "  |  " + Stock + "  |  " + "S/ " + Precio);
                radioButton.setTextSize(10);
                radioButton.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                radioButton.setId(unidad.getID());

                radioButton.setBackgroundResource(R.drawable.radio_button_selector);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                radioButton.setLayoutParams(params);

                // Agrega el RadioButton al RadioGroup
                holder.radioGroup.addView(radioButton);


                EditText editTextCant = new EditText(context.getApplicationContext());
                editTextCant.setText("0");
                editTextCant.setTextSize(10);
                editTextCant.setId(5000 + unidad.getID());
                ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
                editTextCant.setLayoutParams(params1);

                editTextCant.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                editTextCant.setGravity(Gravity.CENTER_HORIZONTAL);
                editTextCant.setEnabled(false);
                editTextCant.setTextColor(context.getResources().getColor(R.color.black));
                editTextCant.setInputType(InputType.TYPE_CLASS_NUMBER);
                //editTextCant.setBackgroundResource(R.drawable.borde);
                // Agrega el Texto al LinearLayout
                holder.linearLayoutCant.addView(editTextCant);


                RadioButton radioButtonDefault = (RadioButton) holder.radioGroup.getChildAt(0);
                // Marcar el primer RadioButton como seleccionado
                radioButtonDefault.setChecked(true);
                radioButtonDefault.setTextColor(context.getResources().getColor(R.color.primary));
                radioButtonDefault.setTypeface(null, Typeface.BOLD);
                radioButtonDefault.setTextSize(11);

                EditText textCantidadDefaul = holder.itemView.findViewById(5000 + radioButtonDefault.getId());
                textCantidadDefaul.setEnabled(true);
                textCantidadDefaul.setTextSize(11);
                textCantidadDefaul.setTypeface(null, Typeface.BOLD);
                textCantidadDefaul.setTextColor(context.getResources().getColor(R.color.primary));
                textCantidadDefaul.setText("1");
            }
        }


        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    EditText textCantidad = holder.itemView.findViewById(5000 + radioButton.getId());

                    if (radioButton.getId() == checkedId) {
                        textCantidad.setTextColor(context.getResources().getColor(R.color.primary));
                        textCantidad.setTypeface(null, Typeface.BOLD);
                        textCantidad.setTextSize(11);
                        textCantidad.setEnabled(true);
                        textCantidad.setText("1");

                        radioButton.setTextColor(context.getResources().getColor(R.color.primary));
                        radioButton.setTypeface(null, Typeface.BOLD);
                        radioButton.setTextSize(11);
                    } else {
                        textCantidad.setTextColor(context.getResources().getColor(R.color.black));
                        textCantidad.setTypeface(null, Typeface.NORMAL);
                        textCantidad.setTextSize(10);
                        textCantidad.setEnabled(false);
                        textCantidad.setText("0");

                        radioButton.setTextColor(context.getResources().getColor(R.color.black));
                        radioButton.setTypeface(null, Typeface.NORMAL);
                        radioButton.setTextSize(10);
                    }
                }
            }

        });

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private ArrayList<Integer> convertArrayInteger(String numeros) {
        ArrayList<Integer> arr = new ArrayList<>();
        if (numeros.length() > 0) {
            String aux = "";
            for (int i = 0; i < numeros.length(); ++i) {
                if (numeros.charAt(i) == ',') {
                    arr.add(Integer.valueOf(aux));
                    aux = "";
                } else {
                    aux = aux + String.valueOf(numeros.charAt(i));
                }
            }
            arr.add(Integer.valueOf(aux));
        }
        return arr;
    }




    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView productCodigo;
        TextView productDescription;
        MaterialButton btn_addToCart;
        RadioGroup radioGroup;
        private OnAddItem onAddItem;
        LinearLayout linearLayoutCant;
        TextView txt_promociones;
        TextView txt_portafolio;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.Imgv_productImage_card);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            productDescription = itemView.findViewById(R.id.Tv_productName_card);
            productCodigo = itemView.findViewById(R.id.Tv_productSmallDescription_card);
            btn_addToCart = itemView.findViewById(R.id.Btn_addProductToCart);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            linearLayoutCant = itemView.findViewById(R.id.LinearLayoutCant);
            txt_promociones = itemView.findViewById(R.id.tv_showPromotionproduct_item);
            txt_portafolio = itemView.findViewById(R.id.Tv_portfolio_product);
        }
    }


}
*/
