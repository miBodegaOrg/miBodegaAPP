package com.mibodega.mystore.views.supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestSupplier;
import com.mibodega.mystore.models.Responses.CategoryProduct;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseByCode;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.common.ProductSupplier;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SupplierProductList;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierRegisterActivity extends AppCompatActivity {

    private TextInputEditText edt_name, edt_ruc, edt_phone, search;
    private RecyclerView rv_products, rv_products_supplier;
    private Button btn_cancel,btn_save;
    private Utils utils = new Utils();
    private Config config =  new Config();

    private SupplierProductList productTemporalList =  new SupplierProductList();
    private ArrayList<ProductResponse> arrayListProduct = new ArrayList<>();
    private ArrayList<ProductResponse> arraySearchListProduct = new ArrayList<>();

    private SupplierResponse supplierResponse;
    private PagesProductResponse pagesSearchProductResponse;

    private String ruc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_register);
        edt_name = findViewById(R.id.Edt_nameSupplier_supplier);
        edt_ruc = findViewById(R.id.Edt_rucSupplier_supplier);
        edt_phone = findViewById(R.id.Edt_phoneSupplier_supplier);

        rv_products = findViewById(R.id.Rv_productList_supplier);
        rv_products_supplier = findViewById(R.id.Rv_productSupplierList_supplier);
        btn_cancel = findViewById(R.id.Btn_cancelRegister_supplier);
        btn_save = findViewById(R.id.Btn_acceptRegister_supplier);
        search = findViewById(R.id.Edt_searchProduct_supplier);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSupplier();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        ruc = getIntent().getExtras().getString("ruc");
        if(ruc!="0"){
            productTemporalList.cleanAll();
            loadData(ruc);
        }else {
            productTemporalList.cleanAll();
        }
    }
    public void registerSupplier(){
        String name =edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String ruc = edt_ruc.getText().toString();
        ArrayList<ProductSupplier> listProduct = new ArrayList<>();
        for (ProductResponse item : productTemporalList.getArrayList()){
            listProduct.add(new ProductSupplier(item.getCode(),item.getPrice()));
        }
        RequestSupplier requestSupplier = new RequestSupplier(name,phone,ruc,listProduct);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<SupplierResponse> call = service.createSupplier(requestSupplier,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponse>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponse> call, @NonNull Response<SupplierResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    System.out.println("body: "+response.body());
                    Toast.makeText(getBaseContext(),"Creado",Toast.LENGTH_SHORT).show();

                    edt_name.setText("");
                    edt_phone.setText("");
                    edt_ruc.setText("");
                    productTemporalList.cleanAll();


                }
            }
            @Override
            public void onFailure(@NonNull Call<SupplierResponse> call, @NonNull Throwable t) {
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
                                productTemporalList.addProduct(product);
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
        Call<SupplierResponse> call = service.getSuppliersById(ruc,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<SupplierResponse>() {
            @Override
            public void onResponse(@NonNull Call<SupplierResponse> call, @NonNull Response<SupplierResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"cargado",Toast.LENGTH_SHORT).show();
                    supplierResponse = response.body();
                    if(supplierResponse.getProducts().size()>0){
                        edt_name.setText(supplierResponse.getName());
                        edt_ruc.setText(supplierResponse.getRuc());
                        edt_phone.setText(supplierResponse.getPhone());
                        int index = supplierResponse.getProducts().size();
                        getProductByCode(supplierResponse.getProducts().get(index-1).getCode(),index);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<SupplierResponse> call, @NonNull Throwable t) {
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
            Call<ProductResponseByCode> call = service.getProductByCode(code,"Bearer "+config.getJwt());
            call.enqueue(new Callback<ProductResponseByCode>() {
                @Override
                public void onResponse(@NonNull Call<ProductResponseByCode> call, @NonNull Response<ProductResponseByCode> response) {
                    System.out.println(response.toString());
                    if(response.isSuccessful()){
                        ProductResponseByCode product = response.body();
                        if(product!=null){
                            CategoryProduct categoryProduct = new CategoryProduct(product.getCategory(),"");
                            SubCategoryResponse subCategoryResponse = new SubCategoryResponse(product.getSubcategory(),"");

                            ProductResponse productResponse = new ProductResponse(
                                    product.get_id(),
                                    product.getName(),
                                    product.getCode(),
                                    product.getPrice(),
                                    product.getStock(),
                                    product.getImage_url(),
                                    product.getSales(),
                                    false,
                                    categoryProduct,
                                    subCategoryResponse,
                                    product.getShop(),
                                    product.getCreatedAt(),
                                    product.getUpdatedAt()
                            );
                            productTemporalList.addProduct(productResponse);
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
                public void onFailure(@NonNull Call<ProductResponseByCode> call, @NonNull Throwable t) {
                    System.out.println("errror "+t.getMessage());
                }
            });

        }else{
            updateDATA();
        }
    }
    public void updateDATA(){
        arrayListProduct  = productTemporalList.getArrayList();
        RecyclerViewAdapterProductSale listAdapter = new RecyclerViewAdapterProductSale(getBaseContext(),arrayListProduct);
        rv_products_supplier.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rv_products_supplier.setAdapter(listAdapter);
    }



}