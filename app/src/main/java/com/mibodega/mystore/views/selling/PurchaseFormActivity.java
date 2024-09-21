package com.mibodega.mystore.views.selling;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.PurchaseRequest;
import com.mibodega.mystore.models.Responses.CategoryProduct;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseByCode;
import com.mibodega.mystore.models.Responses.ProductResponseSupplier;
import com.mibodega.mystore.models.Responses.ProductResponseSupplierV2;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.models.common.ProductPurchase;
import com.mibodega.mystore.models.common.ProductSaleV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.LoadingDialogAdapter;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSupplier;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;
import com.mibodega.mystore.views.products.ProductEditActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PurchaseFormActivity extends AppCompatActivity {

    private SearchView searchProduct;
    private RecyclerView rv_recyclerProductList;

    private LoadingDialogAdapter loadingDialog = new LoadingDialogAdapter();
    private Button btn_vender;
    private Config config =  new Config();
    private Utils utils = new Utils();

    private TextInputEditText edt_dicount, edt_shipping;
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private Spinner sp_selectSupplier;
    private ArrayList<SupplierResponseV2> arrsupplierResponses = new ArrayList<>();
    private ArrayList<ProductResponseSupplierV2> arrProducts = new ArrayList<>();
    private Map<String,SupplierResponseV2> mapsupplierResponses = new HashMap<>();
    private RecyclerViewAdapterProductSupplier listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_form);

        searchProduct = findViewById(R.id.Sv_searchProduct_purchase);
        rv_recyclerProductList = findViewById(R.id.Rv_productAtPurchaseList_purchase);
        btn_vender = findViewById(R.id.Btn_purchaseProducts_purchase);
        sp_selectSupplier = findViewById(R.id.Sp_selectSuppier_purchase);
        edt_dicount = findViewById(R.id.Edt_discount_purchase);
        edt_shipping = findViewById(R.id.Edt_shipping_purchase);
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


        btn_vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    createPurchase();
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadData();
        loadSuppliers();
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!Objects.equals(s, "")){

                }else{

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(Objects.equals(s, "")){

                }
                return true;

            }
        });

        sp_selectSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),"SELECCIONADO",Toast.LENGTH_SHORT).show();
                arrProducts.clear();
                arrProducts = mapsupplierResponses.get(sp_selectSupplier.getSelectedItem().toString()).getProducts();
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public String valiteFields(){
            String message = "ok";
            if(edt_shipping.getText().toString().trim().length() == 0){
                message += "😨 Debe ingresar precio envio \n";
            }
            if(edt_dicount.getText().toString().trim().length() == 0){
                message += "😨 Debe ingresar Nombre cliente \n";
            }

             if(edt_dicount.getText().toString().trim().length() != 0){
                 double aux = Double.parseDouble(edt_dicount.getText().toString());
                 if(aux>100){
                     message += "😨 Debe ingresar un numero menor a 100 en descuentos \n";
                 }
             }
            return message;

    }

    public void loadData(){
        listAdapter = new RecyclerViewAdapterProductSupplier(2, getBaseContext(), arrProducts, new RecyclerViewAdapterProductSupplier.OnDeleteItem() {
            @Override
            public void onClick(ProductResponseSupplierV2 item) {
                arrProducts.remove(item);
                loadData();
            }
        });
        rv_recyclerProductList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rv_recyclerProductList.setAdapter(listAdapter);
    }


    public void loadSuppliers(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISupplierServices service = retrofit.create(ISupplierServices.class);
        Call<List<SupplierResponseV2>> call = service.getSuppliers("Bearer "+config.getJwt());
        call.enqueue(new Callback<List<SupplierResponseV2>>() {
            @Override
            public void onResponse(@NonNull Call<List<SupplierResponseV2>> call, @NonNull Response<List<SupplierResponseV2>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    arrsupplierResponses = (ArrayList<SupplierResponseV2>) response.body();
                    if(arrsupplierResponses!=null){
                        ArrayList<String> suppliers = new ArrayList<>();
                        for (SupplierResponseV2 item:arrsupplierResponses) {
                            suppliers.add(item.getName());
                            mapsupplierResponses.put(item.getName(),item);
                        }
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, suppliers);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_selectSupplier.setAdapter(adapter2);
                        System.out.println("successfull request");
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SupplierResponseV2>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void createPurchase(){
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

        ArrayList<ProductPurchase> arraux = new ArrayList<>();
        for (ProductResponseSupplierV2 product : arrProducts){
            int amountint = Integer.parseInt(listAdapter.getMapEditAmount().get(product.getCode()).getText().toString());
            double amount = Double.parseDouble(listAdapter.getMapEditAmount().get(product.getCode()).getText().toString());
            if(!product.isWeight()){
                arraux.add(new ProductPurchase(amountint,product.getCode()));
            }else{
                arraux.add(new ProductPurchase(amount,product.getCode()));
            }

        }
        String ruc = mapsupplierResponses.get(sp_selectSupplier.getSelectedItem().toString()).getRuc();
        Double discount = Double.valueOf(edt_dicount.getText().toString());
        Double shipping = Double.valueOf(edt_shipping.getText().toString());
        PurchaseRequest request = new PurchaseRequest(ruc,discount,shipping,arraux);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IPurchasesService service = retrofit.create(IPurchasesService.class);
        Call<PurchaseResponse> call = service.createPurchase(request, "Bearer " + config.getJwt());
        call.enqueue(new Callback<PurchaseResponse>() {
            @Override
            public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                if (response.isSuccessful()) {
                    Dialog dialog = utils.getAlertCustom(PurchaseFormActivity.this, "success", "Creado", "Compra creada", false);
                    edt_dicount.setText("");
                    edt_shipping.setText("");
                    loadData();
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    });

                } else {
                    Dialog dialog = utils.getAlertCustom(PurchaseFormActivity.this, "warning", "No creado", "Asegurece de ingresar bien los datos", false);
                    dialog.show();
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PurchaseResponse> call, Throwable t) {

            }
        });
    }
}