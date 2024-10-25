package com.mibodega.mystore.views.supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestSupplier;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseSupplierV2;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.models.common.ProductSupplier;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSupplier;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierRegisterActivity extends MainActivity {

    private TextInputEditText edt_name, edt_ruc, edt_phone, search;
    private RecyclerView rv_products, rv_products_supplier;
    private Button btn_cancel,btn_save, btn_update, btn_delete;
    private Utils utils = new Utils();
    private Config config =  new Config();


    private ArrayList<ProductResponseSupplierV2> arrayListProduct = new ArrayList<>();
    private ArrayList<ProductResponse> arraySearchListProduct = new ArrayList<>();
    private RecyclerViewAdapterProductSupplier listAdapter;

    private SupplierResponseV2 supplierResponse;
    private PagesProductResponse pagesSearchProductResponse;

    private String ruc="";
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_supplier_register);
        setContentLayout(R.layout.activity_supplier_register);


        edt_name = findViewById(R.id.Edt_nameSupplier_supplier);
        edt_ruc = findViewById(R.id.Edt_rucSupplier_supplier);
        edt_phone = findViewById(R.id.Edt_phoneSupplier_supplier);

        rv_products = findViewById(R.id.Rv_productList_supplier);
        rv_products_supplier = findViewById(R.id.Rv_productSupplierList_supplier);
        btn_cancel = findViewById(R.id.Btn_cancelRegister_supplier);
        btn_save = findViewById(R.id.Btn_acceptRegister_supplier);
        btn_update = findViewById(R.id.Btn_update_supplier);
        btn_delete = findViewById(R.id.Btn_delete_supplier);
        search = findViewById(R.id.Edt_searchProduct_supplier);
        ruc = getIntent().getExtras().getString("ruc");
        drawerLayout = findViewById(R.id.drawer_layout);
        chatFragmentContainer = findViewById(R.id.chat_fragment_container);

        // Configura el deslizable desde el lado derecho
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Mostrar el ChatFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.chat_fragment_container, new ChatBotGlobalFragment())
                        .commit();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Ocultar el ChatFragment
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.chat_fragment_container))
                        .commit();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // No es necesario hacer nada aquí
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Objects.equals(valiteFields(), "ok")){
                    registerSupplier();
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSupplier(ruc);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    updateSupplier(ruc);
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    rv_products.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    rv_products.setVisibility(View.VISIBLE);
                    searchProducts(editable.toString());
                } else {
                    rv_products.setVisibility(View.GONE);
                }
            }
        });


        if(!Objects.equals(ruc, "0")){
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Detalle de Proveedor");
            }
            arrayListProduct.clear();
            btn_update.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
            loadData(ruc);
        }else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Proveedor");
            }
            arrayListProduct.clear();
            btn_update.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
        }
    }
    public void registerSupplier(){
        String name =edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String ruc = edt_ruc.getText().toString();
        ArrayList<ProductSupplier> listProduct = new ArrayList<>();
        for (ProductResponseSupplierV2 item : arrayListProduct){
            double cost = Double.parseDouble(listAdapter.getMapEditCost().get(item.getCode()).getText().toString());
            listProduct.add(new ProductSupplier(item.getCode(),cost));
        }
        RequestSupplier requestSupplier = new RequestSupplier(name,phone,ruc,listProduct);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<SupplierResponseV2> call = service.createSupplier(requestSupplier,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponseV2>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponseV2> call, @NonNull Response<SupplierResponseV2> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    System.out.println("body: "+response.body());
                    edt_name.setText("");
                    edt_phone.setText("");
                    edt_ruc.setText("");
                    arrayListProduct.clear();
                    Dialog dialog = utils.getAlertCustom(SupplierRegisterActivity.this,"success","Registro"," Se creo exitosamente el proveedor",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                    dialog.show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<SupplierResponseV2> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });



    }

    public void searchProducts(String name){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<PagesProductResponse> call = service.getProductByName(name,20,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesProductResponse> call, @NonNull Response<PagesProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesSearchProductResponse = response.body();
                    if(pagesSearchProductResponse!=null){
                        System.out.println(pagesSearchProductResponse.getDocs().size());
                        arraySearchListProduct  = (ArrayList<ProductResponse>) pagesSearchProductResponse.getDocs();

                        //set arr products search list

                        RecyclerViewAdapterProductSearch listAdapter = new RecyclerViewAdapterProductSearch(getBaseContext(), arraySearchListProduct, new RecyclerViewAdapterProductSearch.OnItem() {
                            @Override
                            public void onClick(ProductResponse product) {
                                Toast.makeText(getBaseContext(),"Agregado",Toast.LENGTH_SHORT).show();

                                arrayListProduct.add(new ProductResponseSupplierV2( product.get_id(),
                                        product.getName(),
                                        0.0,
                                        product.getCode(),
                                        product.getPrice(),
                                        product.getStock(),
                                        product.getImage_url(),
                                        product.getSales(),
                                        false,
                                        product.getCategory().get_id(),
                                        product.getSubcategory().get_id(),
                                        product.getShop(),
                                        product.getCreatedAt(),
                                        product.getUpdatedAt(),"otros"));
                                updateDATA();
                            }
                        });

                        rv_products.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_products.setAdapter(listAdapter);
                    }
                    System.out.println("successfull request");
                }
            }
            @Override
            public void onFailure(@NonNull Call<PagesProductResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void loadData(String ruc){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<SupplierResponseV2> call = service.getSuppliersById(ruc,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponseV2>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponseV2> call, @NonNull Response<SupplierResponseV2> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    supplierResponse = response.body();
                    edt_name.setText(supplierResponse.getName());
                    edt_ruc.setText(supplierResponse.getRuc());
                    edt_phone.setText(supplierResponse.getPhone());

                    if(supplierResponse.getProducts()!=null){
                      int index = supplierResponse.getProducts().size();
                      arrayListProduct = supplierResponse.getProducts();
                      updateDATA();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<SupplierResponseV2> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });



    }

    public void updateSupplier(String ruc){
        String name =edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String _ruc = edt_ruc.getText().toString();
        ArrayList<ProductSupplier> listProduct = new ArrayList<>();
        for (ProductResponseSupplierV2 item : arrayListProduct){
            double cost = Double.parseDouble(listAdapter.getMapEditCost().get(item.getCode()).getText().toString());
            listProduct.add(new ProductSupplier(item.getCode(),cost));
        }
        RequestSupplier requestSupplier = new RequestSupplier(name,phone,_ruc,listProduct);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<SupplierResponseV2> call = service.updateSuppliersById(ruc,requestSupplier,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponseV2>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponseV2> call, @NonNull Response<SupplierResponseV2> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    supplierResponse = response.body();
                    if(supplierResponse!=null){
                        Toast.makeText(getBaseContext(),"ACTUALIZADO",Toast.LENGTH_SHORT).show();
                        edt_name.setText(supplierResponse.getName());
                        edt_phone.setText(supplierResponse.getPhone());
                        edt_ruc.setText(supplierResponse.getRuc());
                    }



                }
            }
            @Override
            public void onFailure(@NonNull Call<SupplierResponseV2> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void deleteSupplier(String ruc){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<SupplierResponseV2> call = service.deleteSupplier(ruc,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponseV2>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponseV2> call, @NonNull Response<SupplierResponseV2> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"cargado",Toast.LENGTH_SHORT).show();
                    supplierResponse = response.body();
                    if(supplierResponse!=null){
                        Toast.makeText(getBaseContext(),"Proveedor Eliminado",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<SupplierResponseV2> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void getProductByCode(String code,int index){


        if(index>0){
                System.out.println("busca producto");
            Retrofit retrofit = new Retrofit.
                    Builder().
                    baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                    build();

            IProductServices service = retrofit.create(IProductServices.class);
            Call<ProductResponse> call = service.getProductByCode(code,"Bearer "+config.getJwt());
            call.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                    System.out.println(response.toString());
                    if(response.isSuccessful()){
                        ProductResponse product = response.body();
                        if(product!=null){

                            ProductResponseSupplierV2 productResponse = new ProductResponseSupplierV2(product.get_id(),
                                            product.getName(),
                                            0.0,
                                            product.getCode(),
                                            product.getPrice(),
                                            product.getStock(),
                                            product.getImage_url(),
                                            product.getSales(),
                                            false,
                                            product.getCategory().get_id(),
                                            product.getSubcategory().get_id(),
                                            product.getShop(),
                                            product.getCreatedAt(),
                                            product.getUpdatedAt(),
                                    "otros");
                            arrayListProduct.add(productResponse);
                            if(index>0) {
                                getProductByCode(supplierResponse.getProducts().get(index-1).getCode(), index - 1);
                            }
                            updateDATA();
                        }
                        System.out.println("successfull request");
                    }else{
                        System.out.println("error");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                    System.out.println("errror "+t.getMessage());
                }
            });

        }else{
            updateDATA();
        }
    }
    public void updateDATA(){
        listAdapter = new RecyclerViewAdapterProductSupplier(1, getBaseContext(), arrayListProduct, new RecyclerViewAdapterProductSupplier.OnDeleteItem() {
            @Override
            public void onClick(ProductResponseSupplierV2 item) {
                arrayListProduct.remove(item);
                updateDATA();
            }
        });
        rv_products_supplier.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rv_products_supplier.setAdapter(listAdapter);
    }

    public String valiteFields(){
        String message = "ok";
        if(edt_name.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar nombre \n";
        }
        if(edt_ruc.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar apellidos \n";
        }
        if(edt_phone.getText().toString().trim().length() != 0){
            int aux = edt_phone.getText().toString().length();
            if(aux != 9){
                message += "😨 Debe ingresar un numero telefono con 9 digitos\n";
            }
        }
        if(edt_ruc.getText().toString().trim().length() != 0){
            int aux = edt_ruc.getText().toString().length();
            if(aux != 11){
                message += "😨 Debe ingresar un ruc con 11 digitos\n";
            }
        }

        edt_name = findViewById(R.id.Edt_nameSupplier_supplier);
        edt_ruc = findViewById(R.id.Edt_rucSupplier_supplier);
        edt_phone = findViewById(R.id.Edt_phoneSupplier_supplier);


        return message;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}