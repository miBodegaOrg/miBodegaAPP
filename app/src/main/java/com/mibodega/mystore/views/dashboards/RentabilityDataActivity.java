package com.mibodega.mystore.views.dashboards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ProductRentabilityResponse;
import com.mibodega.mystore.models.Responses.SaleCategoryDataDashboardResponse;
import com.mibodega.mystore.services.IDashboardServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductRentability;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RentabilityDataActivity extends MainActivity {

    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private RecyclerView rv_dataListProduct;
    private ArrayList<ProductRentabilityResponse> productList = new ArrayList<>();
    private TextInputEditText edt_searchProduct;
    private Config config = new Config();
    private RecyclerViewAdapterProductRentability recyclerViewAdapterProductRentability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rentability_data);
        setContentLayout(R.layout.activity_rentability_data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rentabilidad");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        chatFragmentContainer = findViewById(R.id.chat_fragment_container);
        rv_dataListProduct = findViewById(R.id.Rv_productRentabiltyData_dashboard);
        edt_searchProduct = findViewById(R.id.Edt_searchProductRentability_dashboard);
        edt_searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterData(editable.toString());
            }
        });
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

        loadProductRentability();
    }
    private void filterData(String name){
        if(Objects.equals(name, "")){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
            rv_dataListProduct.setLayoutManager(gridLayoutManager);
            recyclerViewAdapterProductRentability = new RecyclerViewAdapterProductRentability(productList);
            rv_dataListProduct.setAdapter(recyclerViewAdapterProductRentability);
        }else{
            ArrayList<ProductRentabilityResponse> aux = new ArrayList<>();
            for (ProductRentabilityResponse item: productList){
                if(item.getName().toUpperCase().contains(name.toUpperCase())){
                    aux.add(item);
                }
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
            rv_dataListProduct.setLayoutManager(gridLayoutManager);
            recyclerViewAdapterProductRentability = new RecyclerViewAdapterProductRentability(aux);
            rv_dataListProduct.setAdapter(recyclerViewAdapterProductRentability);
        }

    }
    private  void loadProductRentability(){  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(config.getURL_API())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<ProductRentabilityResponse>> call = service.getDataProductRentability("Bearer " + config.getJwt());
        System.out.println(config.getJwt());

        call.enqueue(new Callback<List<ProductRentabilityResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductRentabilityResponse>> call, @NonNull Response<List<ProductRentabilityResponse>> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        productList  = (ArrayList<ProductRentabilityResponse>) response.body();
                        // Configurar el GridLayoutManager con 2 columnas
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 2);
                        rv_dataListProduct.setLayoutManager(gridLayoutManager);
                        // Crear y configurar el adaptador
                        recyclerViewAdapterProductRentability = new RecyclerViewAdapterProductRentability(productList);
                        rv_dataListProduct.setAdapter(recyclerViewAdapterProductRentability);
                    }
                    System.out.println("successfull request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductRentabilityResponse>> call, @NonNull Throwable t) {
                System.out.println("errror " + t.getMessage());
            }
        });

    }

}