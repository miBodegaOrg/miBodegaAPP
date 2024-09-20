package com.mibodega.mystore.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.CategoryResponseWithProducts;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;
import com.mibodega.mystore.models.Responses.PermissionResponse;
import com.mibodega.mystore.models.Responses.TodayDataResponse;
import com.mibodega.mystore.models.common.RecomendationMessage;
import com.mibodega.mystore.services.ICategoryServices;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.services.IDashboardServices;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBfunctionsTableData;
import com.mibodega.mystore.shared.SharedPreferencesHelper;
import com.mibodega.mystore.shared.TextFormaterMarkdown;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.employers.EmployerActivity;
import com.mibodega.mystore.views.offers.OffersActivity;
import com.mibodega.mystore.views.selling.SellingActivity;
import com.mibodega.mystore.views.supplier.SupplierActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private Config config = new Config();
    private Utils utils= new Utils();
    private DBfunctionsTableData dBfunctionsTableData= new DBfunctionsTableData();
    private MaterialCardView btn_employe,btn_supplier, btn_buying, btn_discountPromotion;
    private SharedPreferencesHelper preferencesHelper;
    private TextView tv_recomendation, tv_cantSale,tv_amountSale;
    //por rango tiempo minutos
    private static final String KEY_LAST_SEND_TIMESTAMP = "lastSendTimestamp";
    private static final int INTERVAL_MINUTES = 2; // Intervalo de 30 minutos
    private TextFormaterMarkdown textFormaterMarkdown = new TextFormaterMarkdown();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        btn_employe = root.findViewById(R.id.MBtn_manageEmployes_home);
        btn_supplier = root.findViewById(R.id.MBtn_manageSupplier_home);
        btn_buying = root.findViewById(R.id.MBtn_managePurchases_home);
        btn_discountPromotion = root.findViewById(R.id.MBtn_manageDiscountsOferts_home);
        tv_recomendation = root.findViewById(R.id.Tv_recomendationGPT_home);
        tv_cantSale = root.findViewById(R.id.Tv_cantSaleTotal_home);
        tv_amountSale = root.findViewById(R.id.Tv_amountSaleTotal_home);
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
        String recomendation = dBfunctionsTableData.get_recomendation_save(getContext());

        tv_recomendation.setText(textFormaterMarkdown.formatText(getContext(),recomendation));
        if (preferencesHelper.hasIntervalPassedInMinutes(KEY_LAST_SEND_TIMESTAMP, INTERVAL_MINUTES)) {
            getRecomendationData();
            // Guardar el timestamp actual
            preferencesHelper.putCurrentTimestamp(KEY_LAST_SEND_TIMESTAMP);
        }
        getTodayData();
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

    private void getRecomendationData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de conexión
                        .readTimeout(30, TimeUnit.SECONDS) // Tiempo de lectura
                        .build())
                .build();
        IChatServices service = retrofit.create(IChatServices.class);
        Call<MessageResponseGpt> call = service.createRecomendation("Bearer "+config.getJwt());
        call.enqueue(new Callback<MessageResponseGpt>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponseGpt> call, @NonNull Response<MessageResponseGpt> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    MessageResponseGpt responseGpt = response.body();
                    if(responseGpt!=null){
                        tv_recomendation.setText(textFormaterMarkdown.formatText(getContext(),responseGpt.getResponse()));
                        dBfunctionsTableData.insert_recomendation_sqlite(getContext(),new RecomendationMessage(1,responseGpt.getResponse(),utils.getDateTimeDDMMYYYYHHMMSS()));
                    }
                    System.out.println("successfull request");
                }else{
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
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponseGpt> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    private void getTodayData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<TodayDataResponse> call = service.getTodayData("Bearer "+config.getJwt());
        call.enqueue(new Callback<TodayDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<TodayDataResponse> call, @NonNull Response<TodayDataResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    TodayDataResponse data = response.body();
                    if(data!=null){
                        tv_cantSale.setText(data.getSales().toString());
                        tv_amountSale.setText(data.getTotal().toString());
                    }
                    System.out.println("successfull request");
                }else{
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
                }
            }

            @Override
            public void onFailure(@NonNull Call<TodayDataResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getTodayData();
        String recomendation = dBfunctionsTableData.get_recomendation_save(getContext());
        tv_recomendation.setText(textFormaterMarkdown.formatText(getContext(),recomendation));
        if (preferencesHelper.hasIntervalPassedInMinutes(KEY_LAST_SEND_TIMESTAMP, INTERVAL_MINUTES)) {
            getRecomendationData();
            // Guardar el timestamp actual
            preferencesHelper.putCurrentTimestamp(KEY_LAST_SEND_TIMESTAMP);
        }
    }
}