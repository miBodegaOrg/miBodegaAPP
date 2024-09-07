package com.mibodega.mystore.views.offers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.DiscountResponse;
import com.mibodega.mystore.models.Responses.PromotionResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.services.IDiscountsService;
import com.mibodega.mystore.services.IPromotionService;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterDiscount;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterPromotion;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterPurchase;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffersActivity extends AppCompatActivity {
    private Button btn_newDiscount,btn_newPromotion;
    private RecyclerView rv_discountList,rv_promotionList;
    private ArrayList<DiscountResponse> arrDiscount = new ArrayList<>();
    private ArrayList<PromotionResponse> arrPromotion = new ArrayList<>();
    private RecyclerViewAdapterDiscount recyclerViewAdapterDiscount;
    private RecyclerViewAdapterPromotion recyclerViewAdapterPromotion;
    private Config config = new Config();
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        btn_newDiscount = findViewById(R.id.Btn_newDiscount_discount);
        btn_newPromotion = findViewById(R.id.Btn_newPromotion_promotion);
        rv_discountList = findViewById(R.id.Rv_discountList_discount);
        rv_promotionList = findViewById(R.id.Rv_promotionList_promotion);

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
        btn_newPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg = new Intent(getBaseContext(), PromotionActivity.class);
                startActivity(mg);
            }
        });
        btn_newDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg = new Intent(getBaseContext(), DiscountsActivity.class);
                startActivity(mg);
            }
        });

        loadDiscount();
        loadPromotion();
    }


    public void loadDiscount(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IDiscountsService service = retrofit.create(IDiscountsService.class);
        Call<List<DiscountResponse>> call = service.getDiscounts("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<DiscountResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<DiscountResponse>> call, @NonNull Response<List<DiscountResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    arrDiscount= (ArrayList<DiscountResponse>) response.body();
                    if(arrDiscount!=null){
                        rv_discountList.removeAllViews();
                         recyclerViewAdapterDiscount= new RecyclerViewAdapterDiscount(getBaseContext(), arrDiscount, new RecyclerViewAdapterDiscount.OnDetailItem() {
                            @Override
                            public void onClick(DiscountResponse item) {

                            }
                        });
                        rv_discountList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_discountList.setAdapter(recyclerViewAdapterDiscount);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<DiscountResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void loadPromotion(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IPromotionService service = retrofit.create(IPromotionService.class);
        Call<List<PromotionResponse>> call = service.getPromotion("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<PromotionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<PromotionResponse>> call, @NonNull Response<List<PromotionResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    arrPromotion= (ArrayList<PromotionResponse>) response.body();
                    if(arrPromotion!=null){
                        rv_promotionList.removeAllViews();
                        recyclerViewAdapterPromotion= new RecyclerViewAdapterPromotion(getBaseContext(), arrPromotion, new RecyclerViewAdapterPromotion.OnDetailItem() {
                            @Override
                            public void onClick(PromotionResponse item) {

                            }
                        });
                        rv_promotionList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_promotionList.setAdapter(recyclerViewAdapterPromotion);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<PromotionResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDiscount();
        loadPromotion();
    }
}