package com.mibodega.mystore.views.supplier;

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
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterSupplier;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierActivity extends AppCompatActivity {

    private TextInputEditText edt_searchSupplier;
    private FloatingActionButton btn_newSupplier;
    private RecyclerView rv_supplierList;
    private Config config = new Config();
    private ArrayList<SupplierResponseV2> arrayListSupplier = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        edt_searchSupplier = findViewById(R.id.Edt_searchSuplier_supplier);
        btn_newSupplier = findViewById(R.id.Btn_addNewSupplier_supplier);
        rv_supplierList = findViewById(R.id.Rv_supplierList_supplier);
        drawerLayout = findViewById(R.id.drawer_layout_supplier);
        chatFragmentContainer = findViewById(R.id.chat_fragment_container_supplier);
        laodSupplier();


        btn_newSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ruc","0");
                Intent mg = new Intent(getBaseContext(), SupplierRegisterActivity.class);
                mg.putExtras(bundle);
                startActivity(mg);
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
                        .replace(R.id.chat_fragment_container_supplier, new ChatBotGlobalFragment())
                        .commit();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Ocultar el ChatFragment
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.chat_fragment_container_supplier))
                        .commit();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // No es necesario hacer nada aquí
            }
        });
    }
    public void laodSupplier(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<List<SupplierResponseV2>> call = service.getSuppliers("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<SupplierResponseV2>>() {
            @Override
            public void onResponse(@NonNull Call<List<SupplierResponseV2>> call, @NonNull Response<List<SupplierResponseV2>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    arrayListSupplier = (ArrayList<SupplierResponseV2>) response.body();
                    if(arrayListSupplier!=null){
                        System.out.println(arrayListSupplier.size());
                        rv_supplierList.removeAllViews();
                        RecyclerViewAdapterSupplier listAdapter = new RecyclerViewAdapterSupplier(getBaseContext(), arrayListSupplier, new RecyclerViewAdapterSupplier.OnDetailItem() {
                            @Override
                            public void onClick(SupplierResponseV2 item) {
                                Intent mg = new Intent(getBaseContext(), SupplierDetailActivity.class);
                                startActivity(mg);
                            }
                        }, new RecyclerViewAdapterSupplier.OnManageItem() {
                            @Override
                            public void onClick(SupplierResponseV2 item) {
                                Bundle bundle = new Bundle();
                                bundle.putString("ruc",item.getRuc());
                                System.out.println("ruc "+item.getRuc());
                                Intent mg = new Intent(getBaseContext(), SupplierRegisterActivity.class);
                                mg.putExtras(bundle);
                                startActivity(mg);
                            }
                        });

                        rv_supplierList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_supplierList.setAdapter(listAdapter);
                        System.out.println("successfull request");
                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SupplierResponseV2>> call, @NonNull Throwable t) {
                System.out.println("error "+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        laodSupplier();
    }
}