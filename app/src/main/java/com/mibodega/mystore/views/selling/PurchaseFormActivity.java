package com.mibodega.mystore.views.selling;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.models.common.ProductSaleV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

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
    private RecyclerView rv_recyclerSearchProductList;
    private Button btn_vender;
    private Config config =  new Config();
    private Utils utils = new Utils();

    private SaleTemporalList saleTemporalList =  new SaleTemporalList();
    private ArrayList<ProductResponse> arrayListProduct = new ArrayList<>();
    private ArrayList<ProductResponse> arraySearchListProduct = new ArrayList<>();
    private PagesProductResponse pagesSearchProductResponse;
    private TextInputEditText edt_dicount, edt_shipping;
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private Spinner sp_selectSupplier;
    private ArrayList<SupplierResponseV2> arrsupplierResponses = new ArrayList<>();
    private Map<String,SupplierResponseV2> mapsupplierResponses = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_form);

        searchProduct = findViewById(R.id.Sv_searchProduct_purchase);
        rv_recyclerSearchProductList = findViewById(R.id.Rv_listSearchProduct_purchase);

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
                    rv_recyclerSearchProductList.setVisibility(View.VISIBLE);
                    searchProducts(s);
                }else{
                    rv_recyclerSearchProductList.setVisibility(View.GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(Objects.equals(s, "")){
                    rv_recyclerSearchProductList.setVisibility(View.GONE);

                }
                return true;

            }
        });

        sp_selectSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),"SELECCIONADO",Toast.LENGTH_SHORT).show();
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
        arrayListProduct  = saleTemporalList.getArrayList();
        RecyclerViewAdapterProductSale listAdapter = new RecyclerViewAdapterProductSale(getBaseContext(),arrayListProduct);
        rv_recyclerProductList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        rv_recyclerProductList.setAdapter(listAdapter);
    }
    public void getProductByCode(String code){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<ProductResponseByCode> call = service.getProductByCode(code,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<ProductResponseByCode>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponseByCode> call, @NonNull Response<ProductResponseByCode> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    ProductResponseByCode product = response.body();
                    CategoryProduct categoryProduct = new CategoryProduct(product.getCategory(),"");
                    SubCategoryResponse subCategoryResponse = new SubCategoryResponse(product.getSubcategory(),"");
                    if(product!=null){
                        ProductResponse productResponse = new ProductResponse(
                                product.get_id(),
                                product.getName(),
                                product.getCode(), product.getPrice(),
                                product.getStock(),
                                product.getImage_url(),
                                product.getSales(),
                                false,
                                categoryProduct,
                                subCategoryResponse,
                                product.getShop(),
                                product.getCreatedAt(),
                                product.getUpdatedAt()
                        );
                        saleTemporalList.addProduct(productResponse,1);
                        loadData();
                    }
                    System.out.println("successfull request");
                }else{
                    System.out.println("error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ProductResponseByCode> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void searchProducts(String name){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<PagesProductResponse> call = service.getProductByName(name,20,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<PagesProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<PagesProductResponse> call, @NonNull Response<PagesProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    pagesSearchProductResponse = response.body();
                    if(pagesSearchProductResponse!=null){
                        System.out.println(pagesSearchProductResponse.getDocs().size());
                        arraySearchListProduct  = (ArrayList<ProductResponse>) pagesSearchProductResponse.getDocs();

                        //set arr products search list

                        RecyclerViewAdapterProductSearch listAdapter = new RecyclerViewAdapterProductSearch(getBaseContext(), arraySearchListProduct, new RecyclerViewAdapterProductSearch.OnItem() {
                            @Override
                            public void onClick(ProductResponse product) {
                                Toast.makeText(getBaseContext(),"Agregado",Toast.LENGTH_SHORT).show();
                                saleTemporalList.addProduct(product,1);
                                loadData();
                            }
                        });

                        rv_recyclerSearchProductList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_recyclerSearchProductList.setAdapter(listAdapter);
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
        ArrayList<ProductSaleV2> arraux = new ArrayList<>();
        for (ProductResponse product : saleTemporalList.getArrayList()){

            arraux.add(new ProductSaleV2(product.getCode(),1));
        }
        String ruc = "";
        Double discount = Double.valueOf(edt_dicount.getText().toString())/100;
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
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    saleTemporalList.cleanAll();
                    edt_dicount.setText("");
                    edt_shipping.setText("");
                    loadData();
                    Toast.makeText(getBaseContext(),"COMPRA CREADA",Toast.LENGTH_SHORT).show();
                } else {
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
                    Toast.makeText(getBaseContext(),"no Creado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PurchaseResponse> call, Throwable t) {

            }
        });
    }
}