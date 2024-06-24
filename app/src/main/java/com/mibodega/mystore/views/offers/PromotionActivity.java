package com.mibodega.mystore.views.offers;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.PromotionRequest;
import com.mibodega.mystore.models.Requests.PurchaseRequest;
import com.mibodega.mystore.models.Responses.CategoryProduct;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseByCode;
import com.mibodega.mystore.models.Responses.PromotionResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.models.common.ProductSaleV2;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.IPromotionService;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProductSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromotionActivity extends AppCompatActivity {

    private SearchView searchProduct;
    private TextView tv_codeScanned;
    private ImageButton btn_scanCodeBar;
    private RecyclerView rv_recyclerProductList;
    private RecyclerView rv_recyclerSearchProductList;
    private Button btn_vender;
    private Config config =  new Config();
    private Utils utils = new Utils();

    private SaleTemporalList saleTemporalList =  new SaleTemporalList();
    private ArrayList<ProductResponse> arrayListProduct = new ArrayList<>();
    private ArrayList<ProductResponse> arraySearchListProduct = new ArrayList<>();

    private PagesProductResponse pagesSearchProductResponse;
    private TextInputEditText edt_name, edt_dateInit, edt_dateEnd,edt_buy,edt_receiv;
    private Calendar startCalendar, endCalendar;
    private ImageButton btn_start,btn_end;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getBaseContext(), "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else {
                    String barcodeValue = result.getContents();
                    // Procesa el valor del código de barras
                    tv_codeScanned.setText(barcodeValue);
                    getProductByCode(barcodeValue);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        searchProduct = findViewById(R.id.Sv_searchProduct_promotion);
        rv_recyclerSearchProductList = findViewById(R.id.Rv_listSearchProduct_promotion);
        tv_codeScanned = findViewById(R.id.Tv_codeScannedBar_promotion);
        btn_scanCodeBar = findViewById(R.id.Imgb_scanCodeBarProduct_promotion);
        rv_recyclerProductList = findViewById(R.id.Rv_productAtPromotionList_promotion);
        btn_vender = findViewById(R.id.Btn_createPromotionProducts_promotion);
        edt_name = findViewById(R.id.Edt_namePromotion_promotion);
        edt_dateInit = findViewById(R.id.Edt_dateInit_promotion);
        edt_dateEnd = findViewById(R.id.Edt_dateEnd_promotion);
        edt_buy = findViewById(R.id.Edt_payAmount_promotion);
        edt_receiv = findViewById(R.id.Edt_receivAmount_promotion);
        btn_start = findViewById(R.id.Imgb_selectDateInit_promotion);
        btn_end = findViewById(R.id.Imgb_selectDateEnd_promotion);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(edt_dateInit,startCalendar);
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(edt_dateEnd,endCalendar);
            }
        });



        btn_scanCodeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBarcodeScanner();
            }
        });
        btn_vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    createPromotion();
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadData();
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
                                product.getCode(),
                                product.getPrice(),
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

    private void showDateTimePicker(TextInputEditText editText, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(year1, monthOfYear, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute1) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                String formattedDateTime = dateFormat.format(calendar.getTime());
                editText.setText(formattedDateTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);
        datePickerDialog.show();
    }

    public void createPromotion(){
        ArrayList<String> arraux = new ArrayList<>();
        for (ProductResponse product : saleTemporalList.getArrayList()){
            arraux.add(product.get_id());
        }
        String startDate = edt_dateInit.getText().toString();
        String endDate = edt_dateEnd.getText().toString();
        int pay = Integer.valueOf(edt_buy.getText().toString());
        int buy = Integer.valueOf(edt_receiv.getText().toString());

        PromotionRequest request = new PromotionRequest(
                edt_name.getText().toString(),
                startDate,
                endDate,
                buy,pay,true,arraux);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IPromotionService service = retrofit.create(IPromotionService.class);
        Call<PromotionResponse> call = service.createPromotion(request, "Bearer " + config.getJwt());
        call.enqueue(new Callback<PromotionResponse>() {
            @Override
            public void onResponse(Call<PromotionResponse> call, Response<PromotionResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    saleTemporalList.cleanAll();
                    edt_dateInit.setText("");
                    edt_dateEnd.setText("");
                    edt_name.setText("");
                    edt_buy.setText("");
                    edt_receiv.setText("");

                    loadData();
                    Toast.makeText(getBaseContext(),"PROMOCION CREADA",Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<PromotionResponse> call, Throwable t) {

            }
        });
    }


    private void startBarcodeScanner() {
        ScanOptions options = new ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                .setPrompt("Escanea un código de barras")
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(false);
        barcodeLauncher.launch(options);
    }
    public String valiteFields(){
        String message = "ok";
        if(edt_dateInit.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar fecha inicial \n";
        }
        if(edt_dateEnd.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar fecha final \n";
        }
        if(edt_buy.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar cantidad pago \n";
        }
        if(edt_receiv.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar cantidad recibir \n";
        }



        return message;

    }
}