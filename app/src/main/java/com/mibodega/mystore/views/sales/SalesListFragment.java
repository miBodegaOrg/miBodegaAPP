package com.mibodega.mystore.views.sales;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.PagesSaleResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.models.common.ProductSaleV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISaleServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterSale;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SalesListFragment extends Fragment {



    private Config config = new Config();
    private RecyclerView rv_saleList;

    private PagesSaleResponse pagesSaleResponse;
    private ArrayList<SaleResponse> arrSaleList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sales_list, container, false);
        rv_saleList = root.findViewById(R.id.Rv_saleListCreated_sale);
        loadData();
        return root;
    }

    public void loadData(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<PagesSaleResponse> call = service.getSales("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesSaleResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesSaleResponse> call, @NonNull Response<PagesSaleResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesSaleResponse = response.body();
                    if(pagesSaleResponse!=null){
                        System.out.println(pagesSaleResponse.getDocs().size());
                        arrSaleList  = (ArrayList<SaleResponse>) pagesSaleResponse.getDocs();

                        //set arr products search list

                        RecyclerViewAdapterSale listAdapter = new RecyclerViewAdapterSale(getContext(), arrSaleList, new RecyclerViewAdapterSale.OnDetailItem() {
                            @Override
                            public void onClick(SaleResponse sale) {
                                Intent moveHMA = new Intent(getContext(), DetailSaleActivity.class);
                                startActivity(moveHMA);
                            }
                        }, new RecyclerViewAdapterSale.OnCancelItem() {
                            @Override
                            public void onClick(SaleResponse sale) {
                                cancelSale(sale.get_id());
                            }
                        }, new RecyclerViewAdapterSale.OnPayItem() {
                            @Override
                            public void onClick(SaleResponse sale) {
                                paySale(sale.get_id());
                            }
                        });

                        rv_saleList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        rv_saleList.setAdapter(listAdapter);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<PagesSaleResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void cancelSale(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<SaleResponse> call = service.cancelSale(id, "Bearer " + config.getJwt());
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(Call<SaleResponse> call, Response<SaleResponse> response) {
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(),"Actualizado",Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(getContext(),"no Actualizado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleResponse> call, Throwable t) {

            }
        });
    }

    public void paySale(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<SaleResponse> call = service.paidSale(id, "Bearer " + config.getJwt());
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(Call<SaleResponse> call, Response<SaleResponse> response) {
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(),"Actualizado",Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(getContext(),"no Actualizado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleResponse> call, Throwable t) {

            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}