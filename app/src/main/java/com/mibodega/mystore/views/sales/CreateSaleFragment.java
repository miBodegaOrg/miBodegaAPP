package com.mibodega.mystore.views.sales;

import android.content.Intent;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.models.common.ProductSaleV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISaleServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateSaleFragment extends Fragment {

    private SearchView searchProduct;
    private TextView tv_codeScanned;
    private ImageButton btn_scanCodeBar;
    private RecyclerView rv_recyclerProductList;
    private RecyclerView rv_recyclerSearchProductList;
    private Button btn_vender;
    private Config config =  new Config();
    private Utils utils = new Utils();

    private SaleTemporalList saleTemporalList =  new SaleTemporalList();
    private ArrayList<ProductResponse> arrayListProduct = new ArrayList<>();
    private ArrayList<ProductResponse> arraySearchListProduct = new ArrayList<>();
    private RecyclerViewAdapterProductSale listAdapter;

    private TextView tv_subTotal;

    private PagesProductResponse pagesSearchProductResponse;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else {
                    String barcodeValue = result.getContents();
                    // Procesa el valor del código de barras
                    tv_codeScanned.setText(barcodeValue);
                    getProductByCode(barcodeValue);
                }
            });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_create_sale, container, false);
        searchProduct = root.findViewById(R.id.Sv_searchProduct_sale);
        rv_recyclerSearchProductList = root.findViewById(R.id.Rv_listSearchProduct_sale);
        tv_codeScanned = root.findViewById(R.id.Tv_codeScannedBar_sale);
        btn_scanCodeBar = root.findViewById(R.id.Imgb_scanCodeBarProduct_sale);
        rv_recyclerProductList = root.findViewById(R.id.Rv_productAtSaleList_sale);
        btn_vender = root.findViewById(R.id.Btn_saleProducts_sale);
        tv_subTotal = root.findViewById(R.id.Tv_subTotal_sale);

        btn_scanCodeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBarcodeScanner();
            }
        });
        btn_vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSale();
            }
        });

        loadData();
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!Objects.equals(s, "")){
                    rv_recyclerSearchProductList.setVisibility(View.VISIBLE);
                    searchProducts(s);
                }else{
                    rv_recyclerSearchProductList.setVisibility(View.GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(Objects.equals(s, "")){
                    rv_recyclerSearchProductList.setVisibility(View.GONE);

                }
                return true;

            }
        });
        return root;
    }

    public void loadData(){
        arrayListProduct  = saleTemporalList.getArrayList();
        listAdapter = new RecyclerViewAdapterProductSale(getContext(), arrayListProduct, new RecyclerViewAdapterProductSale.OnEdit() {
            @Override
            public void onClick(ProductResponse product) {
                tv_subTotal.setText("S/. "+saleTemporalList.getTotalPrice().toString());
            }
        }, new RecyclerViewAdapterProductSale.OnDelete() {
            @Override
            public void onClick(ProductResponse product) {
                tv_subTotal.setText("S/. "+saleTemporalList.getTotalPrice().toString());
            }
        });
        rv_recyclerProductList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_recyclerProductList.setAdapter(listAdapter);
    }
    public void getProductByCode(String code){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<ProductResponse> call = service.getProductByCode(code,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    ProductResponse product = response.body();
                    if(product!=null){
                        ProductResponse productResponse = new ProductResponse(
                                product.get_id(),
                                product.getName(),
                                product.getCode(), product.getPrice(),
                                product.getStock(),
                                product.getImage_url(),
                                product.getSales(),
                                product.isWeight(),
                                product.getCategory(),
                                product.getSubcategory(),
                                product.getShop(),
                                product.getCreatedAt(),
                                product.getUpdatedAt(),
                                product.getCost(),
                                product.getSupplier()
                                );
                        saleTemporalList.addProduct(productResponse,1);
                        loadData();
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

                        RecyclerViewAdapterProductSearch listAdapter = new RecyclerViewAdapterProductSearch(getContext(), arraySearchListProduct, new RecyclerViewAdapterProductSearch.OnItem() {
                            @Override
                            public void onClick(ProductResponse product) {
                                Toast.makeText(getContext(),"Agregado",Toast.LENGTH_SHORT).show();
                                saleTemporalList.addProduct(product,1);
                                tv_subTotal.setText("S/. "+saleTemporalList.getTotalPrice().toString());
                                utils.getAlertDialog(getContext(),"Producto","Se agrego "+product.getName()+"a la lista","verde");
                                loadData();
                            }
                        });

                        rv_recyclerSearchProductList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        rv_recyclerSearchProductList.setAdapter(listAdapter);
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


    public void createSale(){
        ArrayList<ProductSaleV2> arraux = new ArrayList<>();
        for (ProductResponse product : saleTemporalList.getArrayList()){
            int amountint = Integer.parseInt(listAdapter.getMapEditAmount().get(product.getCode()).getText().toString());
            double amount = Double.parseDouble(listAdapter.getMapEditAmount().get(product.getCode()).getText().toString());
            if(!product.isWeight()){
                double cost =0;
                double rentability = product.getPrice()-cost;
                if(rentability<0){
                    rentability=0;
                }
                arraux.add(new ProductSaleV2(product.getCode(),amountint));
            }else{
                double cost =0;
                double rentability = product.getPrice()-cost;
                if(rentability<0){
                    rentability=0;
                }
                arraux.add(new ProductSaleV2(product.getCode(),amount));

            }
        }
        RequestCreateSale requestCreateSale = new RequestCreateSale(arraux);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<SaleResponse> call = service.createSales(requestCreateSale, "Bearer " + config.getJwt());
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(Call<SaleResponse> call, Response<SaleResponse> response) {
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(),"Creado",Toast.LENGTH_SHORT).show();
                    saleTemporalList.cleanAll();
                    loadData();
                    SaleResponse saleResponse = response.body();
                    saleTemporalList.setSaleCurrent(saleResponse);
                    Intent moveHMA = new Intent(getContext(), ValidateSaleActivity.class);
                    startActivity(moveHMA);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(),"no Creado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleResponse> call, Throwable t) {

            }
        });
    }


    private void startBarcodeScanner() {
        ScanOptions options = new ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                .setPrompt("Escanea un código de barras")
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(false);
        barcodeLauncher.launch(options);
    }


    @Override
    public void onResume() {
        super.onResume();

        tv_subTotal.setText("S/. "+saleTemporalList.getTotalPrice().toString());

    }
}