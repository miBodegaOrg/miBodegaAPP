package com.mibodega.mystore.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.CategoryResponseWithProducts;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.PermissionResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.ICategoryServices;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SharedPreferencesHelper;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;
import com.mibodega.mystore.views.employers.EmployerActivity;
import com.mibodega.mystore.views.offers.OffersActivity;
import com.mibodega.mystore.views.selling.SellingActivity;
import com.mibodega.mystore.views.supplier.SupplierActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private Config config = new Config();
    private MaterialCardView btn_employe,btn_supplier, btn_buying, btn_discountPromotion;
    private SharedPreferencesHelper preferencesHelper;

    //por rango tiempo minutos
    private static final String KEY_LAST_SEND_TIMESTAMP = "lastSendTimestamp";
    private static final int INTERVAL_MINUTES = 2; // Intervalo de 30 minutos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        btn_employe = root.findViewById(R.id.MBtn_manageEmployes_home);
        btn_supplier = root.findViewById(R.id.MBtn_manageSupplier_home);
        btn_buying = root.findViewById(R.id.MBtn_managePurchases_home);
        btn_discountPromotion = root.findViewById(R.id.MBtn_manageDiscountsOferts_home);

        preferencesHelper = new SharedPreferencesHelper(getContext());

        btn_employe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent me = new Intent(getContext(),EmployerActivity.class);
                startActivity(me);
            }
        });
        btn_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent su = new Intent(getContext(), SupplierActivity.class);
                startActivity(su);
            }
        });
        btn_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent su = new Intent(getContext(), SellingActivity.class);
                startActivity(su);
            }
        });
        btn_discountPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent su = new Intent(getContext(), OffersActivity.class);
                startActivity(su);
            }
        });
        initProductsData(root);
        initPermises();
        initCategoryData(root);
        if (preferencesHelper.hasIntervalPassedInMinutes(KEY_LAST_SEND_TIMESTAMP, INTERVAL_MINUTES)) {
            sendData();
            // Guardar el timestamp actual
            preferencesHelper.putCurrentTimestamp(KEY_LAST_SEND_TIMESTAMP);
        }
        return root;
    }
    private void initProductsData(View root) {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ICategoryServices service = retrofit.create(ICategoryServices.class);
        Call<List<CategoryResponse>> call = service.getCategories("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponse>> call, @NonNull Response<List<CategoryResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    ArrayList<CategoryResponse> arr = (ArrayList<CategoryResponse>) response.body();
                    if(arr!=null){
                        config.setArrCategories(arr);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    private void initPermises(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IEmployeeServices service = retrofit.create(IEmployeeServices.class);
        Call<PermissionResponse> call = service.getPermises("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PermissionResponse>() {
            @Override
            public void onResponse(@NonNull Call<PermissionResponse> call, @NonNull Response<PermissionResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    PermissionResponse permissionResponse = response.body();
                    if(permissionResponse!=null){
                        config.setArrPermises(permissionResponse.getPermissions());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<PermissionResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    private void initCategoryData(View root) {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ICategoryServices service = retrofit.create(ICategoryServices.class);
        Call<List<CategoryResponseWithProducts>> call = service.getCategoriesWithProducts("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<CategoryResponseWithProducts>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponseWithProducts>> call, @NonNull Response<List<CategoryResponseWithProducts>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    ArrayList<CategoryResponseWithProducts> arr = (ArrayList<CategoryResponseWithProducts>) response.body();
                    if(arr!=null){
                        config.setArrCategoriesWithProducts(arr);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponseWithProducts>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    private void sendData() {
        // Aquí puedes agregar tu lógica para enviar datos
        Toast.makeText(getContext(),"RECOMENDACION",Toast.LENGTH_SHORT).show();
    }

}