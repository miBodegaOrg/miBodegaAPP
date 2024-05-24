package com.mibodega.mystore.views;

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

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;

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
    private Config  config = new Config();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_products, container, false);

        initProductsData();
        RecyclerViewAdapterProduct listAdapter = new RecyclerViewAdapterProduct(getResources(), getContext(), 1, productlist, new RecyclerViewAdapterProduct.OnDetailItem() {
            @Override
            public void onClick(ProductResponse product) {

            }
        }, new RecyclerViewAdapterProduct.OnSupplierItem() {
            @Override
            public void onClick(ProductResponse product) {

            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.Rv_productlist_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        glm = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(listAdapter);


        return root;
    }

    private void initProductsData() {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<List<ProductResponse>> call = service.getProducts(config.getJwt());
        call.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductResponse>> call, @NonNull Response<List<ProductResponse>> response) {
                if(response.isSuccessful()){
                    productlist = (ArrayList<ProductResponse>) response.body();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ProductResponse>> call, @NonNull Throwable t) {

            }
        });
    }
}