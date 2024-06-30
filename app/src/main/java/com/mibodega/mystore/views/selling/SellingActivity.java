package com.mibodega.mystore.views.selling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterChat;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterPurchase;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;
import com.mibodega.mystore.views.chatbot.ChatbotActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellingActivity extends AppCompatActivity {
    private FloatingActionButton btn_newPurchase;
    private RecyclerView rv_purchaseList;
    private RecyclerViewAdapterPurchase recyclerViewAdapterPurchase;

    private Config config = new Config();
    private ArrayList<PurchaseResponse> purchaseList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        btn_newPurchase = findViewById(R.id.Btn_addNewPurchase_purchases);
        rv_purchaseList = findViewById(R.id.Rv_purchaseList_purchases);

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

        btn_newPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mb = new Intent(getBaseContext(), PurchaseFormActivity.class);
                startActivity(mb);
            }
        });
        loadPurchases();

    }

    public void loadPurchases(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IPurchasesService service = retrofit.create(IPurchasesService.class);
        Call<List<PurchaseResponse>> call = service.getPurchases("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<PurchaseResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PurchaseResponse>> call, @NonNull Response<List<PurchaseResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    purchaseList = (ArrayList<PurchaseResponse>) response.body();
                    if(purchaseList!=null){
                        rv_purchaseList.removeAllViews();
                        recyclerViewAdapterPurchase = new RecyclerViewAdapterPurchase(getBaseContext(), purchaseList, new RecyclerViewAdapterPurchase.OnDetailItem() {
                            @Override
                            public void onClick(PurchaseResponse item) {
                                validatePurchase(item);
                            }
                        });
                        rv_purchaseList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_purchaseList.setAdapter(recyclerViewAdapterPurchase);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<PurchaseResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void validatePurchase(PurchaseResponse purchase){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IPurchasesService service = retrofit.create(IPurchasesService.class);
        Call<PurchaseResponse> call = service.receivedPurchase(purchase.get_id(),"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PurchaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponse> call, @NonNull Response<PurchaseResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                     PurchaseResponse purchaseResponse =  response.body();
                    if(purchaseResponse!=null){
                        System.out.println("successfull request");
                        loadPurchases();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPurchases();
    }
}