package com.mibodega.mystore.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Conference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mibodega.mystore.MainNavigationActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;
import com.mibodega.mystore.views.products.ProductEditActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductsFragment extends Fragment {


    private GridLayoutManager glm;
    private ArrayList<ProductResponse> productlist = new ArrayList<>();
    private PagesProductResponse pagesProductResponse;
    private Config  config = new Config();
    private RecyclerView recyclerView;
    private Button btnMoveToAddProduct;

    private View _root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        _root =root;
        recyclerView = root.findViewById(R.id.Rv_productlist_product);
        initProductsData(root);

        btnMoveToAddProduct = root.findViewById(R.id.Btn_addNewProduct_product);

        btnMoveToAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHMA = new Intent(getContext(), ProductEditActivity.class);
                startActivity(moveHMA);
            }
        });
        return root;
    }

    private void initProductsData(View root) {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<PagesProductResponse> call = service.getProducts("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesProductResponse> call, @NonNull Response<PagesProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesProductResponse = response.body();
                    if(pagesProductResponse!=null){
                        System.out.println(pagesProductResponse.getDocs().size());
                        productlist  = (ArrayList<ProductResponse>) pagesProductResponse.getDocs();
                        recyclerView.removeAllViews();
                        RecyclerViewAdapterProduct listAdapter = new RecyclerViewAdapterProduct(getContext(), 1, productlist, new RecyclerViewAdapterProduct.OnDetailItem() {
                            @Override
                            public void onClick(ProductResponse product) {

                            }
                        }, new RecyclerViewAdapterProduct.OnSupplierItem() {
                            @Override
                            public void onClick(ProductResponse product) {

                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(listAdapter);
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


    @Override
    public void onResume() {
        super.onResume();
        initProductsData(_root);
    }
}