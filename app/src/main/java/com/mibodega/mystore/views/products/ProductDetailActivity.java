package com.mibodega.mystore.views.products;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.GenerateCodeResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseByCode;
import com.mibodega.mystore.models.Responses.ProductResponseSupplier;
import com.mibodega.mystore.models.Responses.ProductResponseSupplierV2;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;
import com.mibodega.mystore.services.IBarCodeService;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.services.ISupplierServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.LoadingDialogAdapter;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST = 101;

    private Uri imageUri;
    private Utils utils = new Utils();
    private MultipartBody.Part imagePart;

    private ImageButton btnCamera ;
    private ImageButton btnGallery;

    private TextInputEditText txt_name_product;
    private TextInputEditText txt_price_product;
    private TextInputEditText txt_stock_product;
    private TextInputEditText txt_code_product;
    private TextInputEditText txt_cost_product;

    private Spinner sp_category_product;
    private TextView tv_fileName;
    private Button btnScanProduct;
    private Button btnGenerateCode;
    private Spinner getSp_category_product;
    private Spinner getSp_subcategory_product;
    private Spinner getSp_proveedor, getSp_factor;



    private Button btn_saveProduct,btn_deleteProduct;
    private Config config = new Config();

    private Dialog dialogShowProductImage;
    private Bitmap paymentBitmapImg, barcodeBitmapImage;

    private ImageView imgProduct;
    private RequestBody finalImageBody, imageBodyBarCode;
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private String code="";
    ProductResponse productGeneral;
    private LoadingDialogAdapter loadingDialog = new LoadingDialogAdapter();

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(ProductDetailActivity.this, "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else {
                    String barcodeValue = result.getContents();
                    // Procesa el valor del código de barras
                    txt_code_product.setText(barcodeValue);
                }
            });

    private ArrayList<SupplierResponseV2> arrsupplierResponses = new ArrayList<>();
    private Map<String,SupplierResponseV2> mapsupplierResponses = new HashMap<>();
    private Map<String,CategoryResponse> mapcategoryResponses = new HashMap<>();
    private Map<String,SubCategoryResponse> mapsupcategoriesResponses = new HashMap<>();
    private ImageView img_barCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        img_barCode = findViewById(R.id.Imv_barsCode_detail_update);
        txt_name_product = findViewById(R.id.Edt_name_product_update);
        txt_price_product = findViewById(R.id.Edt_price_product_update);
        txt_stock_product = findViewById(R.id.Edt_stock_product_update);
        txt_code_product = findViewById(R.id.Edt_productCode_product_update);
        txt_cost_product = findViewById(R.id.Edt_saleprice_product_update);

        getSp_category_product = findViewById(R.id.Sp_selectProductCategory_product_update);
        getSp_subcategory_product = findViewById(R.id.Sp_selectProductSubCategory_product_update);
        getSp_proveedor = findViewById(R.id.Sp_selectProveedor_product_update);
        getSp_factor = findViewById(R.id.Sp_selectUnidad_product_update);

        btn_deleteProduct = findViewById(R.id.Btn_deleteProduct_detail);
        btn_saveProduct = findViewById(R.id.Btn_saveProduct_product_update);
        tv_fileName = findViewById(R.id.Tv_productFileName_product_update);
        btnScanProduct = findViewById(R.id.Btn_scanProductCode_product_update);
        btnGenerateCode = findViewById(R.id.Btn_generateProductCode_product_update);

        ArrayList<String> type = new ArrayList<>();
        type.add("UN");
        type.add("KG");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, type);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSp_factor.setAdapter(adapter4);
        getSp_factor.setSelection(0);

        code = getIntent().getExtras().getString("product_code");

        getProductByCode(code);
        drawerLayout = findViewById(R.id.drawer_layout);
        chatFragmentContainer = findViewById(R.id.chat_fragment_container);
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


        tv_fileName.setText("");
        setDialogs(this);
        mapcategoryResponses.clear();
        ArrayList<String> categories = new ArrayList<>();
        for (CategoryResponse item : config.getArrCategories()){
            categories.add(item.getName());
            mapcategoryResponses.put(item.getName(), item);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSp_category_product.setAdapter(adapter);
        getSp_category_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = getSp_category_product.getSelectedItem().toString();
                setSelectSubCategories(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getSp_category_product.setSelection(0);

        setupButtons();
        btn_saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_code_product.getText().toString().equals("")){
                    updateproductData(productGeneral.get_id());
                }else {
                    Toast.makeText(getBaseContext(),"Genera el codigo del producto",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(tv_fileName.getText().toString().equals(""))){
                    if(paymentBitmapImg!=null){
                        imgProduct.setImageBitmap(paymentBitmapImg);
                    }
                    dialogShowProductImage.show();
                }
            }
        });
        btnScanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBarcodeScanner();
            }
        });

        btnGenerateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCodeBars();
            }
        });
        btn_deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productGeneral!=null){
                    Dialog dialog = utils.getAlertCustom(ProductDetailActivity.this,"warning","¿Estas seguro de eliminar el producto?","Esta eliminación sera permanente",true);
                    Button btn_accept = dialog.findViewById(R.id.btn_accept);
                    Button btn_cancel= dialog.findViewById(R.id.btn_cancel);
                    btn_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteProductById(productGeneral.get_id());
                            dialog.dismiss();
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        loadBarCodeImage(code);
    }
    public void loadBarCodeImage(String _code){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IBarCodeService service = retrofit.create(IBarCodeService.class);
        Call<ResponseBody> call = service.generateCodeProduct(_code,"Bearer "+config.getJwt());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Convertir el ResponseBody a Bitmap
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    // Asignar el Bitmap al ImageView
                    img_barCode.setImageBitmap(bmp);
                }else{

                }

            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void getProductByCode(String code){
            System.out.println("busca producto");
            Retrofit retrofit = new Retrofit.
                    Builder().
                    baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                    build();

            IProductServices service = retrofit.create(IProductServices.class);
            Call<ProductResponse> call = service.getProductByCode(code,"Bearer "+config.getJwt());
            call.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                    System.out.println(response.toString());
                    if(response.isSuccessful()){
                        productGeneral = response.body();
                        if(productGeneral!=null){
                            txt_name_product.setText(productGeneral.getName());
                            txt_price_product.setText(String.valueOf(productGeneral.getPrice()));
                            txt_stock_product.setText(String.valueOf(productGeneral.getStock()));
                            txt_code_product.setText(String.valueOf(productGeneral.getCode()));

                            if (productGeneral.getCost() == null) {
                                txt_cost_product.setText("0");
                            } else {
                                txt_cost_product.setText(String.valueOf(productGeneral.getCost()));
                            }

                            if(!Objects.equals(productGeneral.getImage_url(), "")){

                                Glide.with(getBaseContext())
                                        .asBitmap()
                                        .load(productGeneral.getImage_url())
                                        .error(R.drawable.no_photo)
                                        .centerCrop()
                                        .transform(new RoundedCorners(2))
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imgProduct.setImageBitmap(resource);
                                                paymentBitmapImg = resource;
                                                tv_fileName.setText("image.jpg");

                                                // Convertir el Bitmap a un array de bytes sin compresión (manteniendo la calidad y resolución)
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // Usar PNG para mantener calidad
                                                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                                                finalImageBody = RequestBody.create(MediaType.parse("image/png"), imageBytes);


                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                // Este método se llama cuando la solicitud se ha cancelado
                                            }
                                        });
                            }

                            selectSpinnerItemByName(getSp_category_product, productGeneral.getCategory().getName());
                            selectSpinnerItemByName(getSp_subcategory_product, productGeneral.getSubcategory().getName());
                            String unidad ="";
                            if(productGeneral.isWeight()){
                                unidad = "KG";
                            }else{
                                unidad = "UN";
                            }
                            selectSpinnerItemByName(getSp_factor, unidad);
                            loadSuppliers();
                            System.out.println("successfull request");
                        }

                    }else{
                        System.out.println("error");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                    System.out.println("errror "+t.getMessage());
                }
            });
    }
    public void deleteProductById(String id){
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);
        Call<ProductResponseSupplierV2> call = service.deleteProductById(id,"Bearer "+config.getJwt());
        call.enqueue(new Callback<ProductResponseSupplierV2>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponseSupplierV2> call, @NonNull Response<ProductResponseSupplierV2> response) {
                if(response.isSuccessful()){
                    Dialog dialog = utils.getAlertCustom(ProductDetailActivity.this, "success", "Eliminado", "Producto eliminado", false);
                    ProductResponseSupplierV2 productResponseByCode = response.body();
                    if(productResponseByCode!=null){
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                loadingDialog.dismissDialog();
                                finish();
                            }
                        });
                    }
                    loadingDialog.dismissDialog();
                }else {
                    Dialog dialog = utils.getAlertCustom(ProductDetailActivity.this, "warning", "No eliminado", "Hubo un error", false);
                    dialog.show();
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            loadingDialog.dismissDialog();
                            finish();
                        }
                    });

                }
            }
            @Override
            public void onFailure(@NonNull Call<ProductResponseSupplierV2> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void selectSpinnerItemByName(Spinner spinner, String itemName) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).toString().equals(itemName)) {
                spinner.setSelection(position);
                return;
            }
        }
    }
    private void setupButtons() {
        btnCamera = findViewById(R.id.Imgb_takeProductPhoto_product_update);
        btnGallery= findViewById(R.id.Imgb_getproductPhoto_product_update);


        btnCamera.setOnClickListener(v -> {
            if (checkPermissions()) {
                openCamera();
            } else {
                requestPermissions();
            }
        });

        btnGallery.setOnClickListener(v -> {
            if (checkPermissions()) {
                openGallery();
            } else {
                requestPermissions();
            }
        });

    }
    public void setSelectSubCategories(String category){
        mapsupcategoriesResponses.clear();
        ArrayList<String> subcategories = new ArrayList<>();
        for(CategoryResponse item : config.getArrCategories()){
            if(Objects.equals(item.getName(), category)){
                for (SubCategoryResponse subcategory : item.getSubcategories()){
                    subcategories.add(subcategory.getName());
                    mapsupcategoriesResponses.put(subcategory.getName(), subcategory);
                }
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, subcategories);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSp_subcategory_product.setAdapter(adapter2);
        getSp_subcategory_product.setSelection(0);
    }
    public void setDialogs(Context context){
        dialogShowProductImage = new Dialog(context);
        dialogShowProductImage.setContentView(R.layout.dialog_show_product_photo);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogShowProductImage.getWindow().getAttributes());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogShowProductImage.getWindow().setBackgroundDrawableResource(R.drawable.backgroun_custom_rectangle);
        }
        imgProduct = dialogShowProductImage.findViewById(R.id.Imgv_showProductPhoto);
        ImageButton btn_close = dialogShowProductImage.findViewById(R.id.Imgb_closeShowProduct);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShowProductImage.dismiss();
            }
        });
        Display display;
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = context.getDisplay();
            display.getSize(size);
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                display = windowManager.getDefaultDisplay();
                display.getSize(size);
            }
        }
        int screenWidth = size.x;
        int dialogWidth = (int) (screenWidth * 0.9);
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        dialogShowProductImage.getWindow().setLayout(dialogWidth, dialogHeight);
        dialogShowProductImage.getWindow().getAttributes().windowAnimations = R.style.animation;

    }
    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000000) + 100000000;
        return String.valueOf(randomNumber);
    }
    private void updateproductData(String id) {
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(mapcategoryResponses.get(getSp_category_product.getSelectedItem().toString()).getName());

        ArrayList<String> subcategoryList = new ArrayList<>();
        subcategoryList.add(mapsupcategoriesResponses.get(getSp_subcategory_product.getSelectedItem().toString()).getName());

        String category = getSp_category_product.getSelectedItem().toString();
        String subcategory = getSp_subcategory_product.getSelectedItem().toString();

        String code = txt_code_product.getText().toString();

        String selectType = "";
        if(getSp_factor.getSelectedItem().toString().equals("UN")){selectType = "false";}else{selectType = "true";}
        SupplierResponseV2 selectSupplier = mapsupplierResponses.get(getSp_proveedor.getSelectedItem().toString());
        String selectsupplierid = "";
        if(selectSupplier!=null){
            selectsupplierid = selectSupplier.get_id();
        }

        System.out.println(txt_name_product.getText().toString());
        System.out.println(code);
        System.out.println(txt_price_product.getText().toString());
        System.out.println(txt_stock_product.getText().toString());
        System.out.println(category);
        System.out.println(subcategory);
        System.out.println(selectType);
        System.out.println("sup "+selectsupplierid);
        System.out.println(txt_cost_product.getText().toString());

        Map<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("name", RequestBody.create(MediaType.parse("text/plain"), txt_name_product.getText().toString()));
        requestMap.put("code", RequestBody.create(MediaType.parse("text/plain"), code));
        requestMap.put("price", RequestBody.create(MediaType.parse("text/plain"), txt_price_product.getText().toString()));
        requestMap.put("stock", RequestBody.create(MediaType.parse("text/plain"), txt_stock_product.getText().toString()));
        requestMap.put("category", RequestBody.create(MediaType.parse("text/plain"), category));
        requestMap.put("subcategory", RequestBody.create(MediaType.parse("text/plain"), subcategory));
        requestMap.put("weight", RequestBody.create(MediaType.parse("text/plain"), selectType));
        requestMap.put("image\"; filename=\"" + "image", finalImageBody);
        requestMap.put("supplier", RequestBody.create(MediaType.parse("text/plain"), selectsupplierid));
        requestMap.put("cost", RequestBody.create(MediaType.parse("text/plain"), txt_cost_product.getText().toString()));

        Call<ProductResponseSupplierV2> call = service.updateProduct(id,requestMap,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<ProductResponseSupplierV2>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponseSupplierV2> call, @NonNull Response<ProductResponseSupplierV2> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    System.out.println("actulizado");
                    Dialog dialog = utils.getAlertCustom(ProductDetailActivity.this, "success", "Actualizado", "Producto actualizado", false);
                    ProductResponseSupplierV2 productResponseByCode = response.body();
                    if(productResponseByCode!=null){
                        //Dialog dialog = utils.getAlertCustom(ProductEditActivity.this, "success", "Creado", "Producto creado", false);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                loadingDialog.dismissDialog();
                                finish();
                            }
                        });
                    }
                    loadingDialog.dismissDialog();
                }else {
                    Dialog dialog = utils.getAlertCustom(ProductDetailActivity.this, "warning", "No creado", "Asegurece de ingresar bien los datos", false);
                    dialog.show();
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            loadingDialog.dismissDialog();
                            finish();
                        }
                    });

                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductResponseSupplierV2> call, @NonNull Throwable t) {
                System.out.println("error de servidor");
                System.out.println("errror "+t.getMessage());


            }
        });
    }
    public void generateCodeBars(){

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);

        Call<GenerateCodeResponse> call = service.generateCodeProduct("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<GenerateCodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenerateCodeResponse> call, @NonNull Response<GenerateCodeResponse> response) {
                System.out.println(response.toString());

                if (response.isSuccessful()) {

                    GenerateCodeResponse code = response.body();
                    if(code!=null){
                        System.out.println(code.getCode());
                        txt_code_product.setText(code.getCode());
                    }
                    System.out.println("successfull request");

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(@NonNull Call<GenerateCodeResponse> call, @NonNull Throwable t) {
                System.out.println("errror " + t.getMessage());
            }
        });

    }
    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    saveImageFromCamera(imageBitmap);
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        imageUri = data.getData();
                        createImagePartFromGallery();
                    }
                }
            }
    );
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos, puedes acceder a la cámara y la galería
            } else {
                // Permisos denegados, muestra un mensaje al usuario o realiza alguna acción alternativa
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }
    private void saveImageFromCamera(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        paymentBitmapImg = bitmap;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        finalImageBody =requestFile;
        imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        System.out.println("datos");
        System.out.println(imagePart.body().toString());

        tv_fileName.setText("imageCamera.jpg");
    }
    private void createImagePartFromGallery() {
        String filePath = getPathFromUri(imageUri);
        if (filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            finalImageBody =requestFile;
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            System.out.println("datos");

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                paymentBitmapImg = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(imagePart.body().toString());
            tv_fileName.setText("imageGallery.jpg");
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        }
        return null;
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
                        String nameactualproveedor="";
                        for (SupplierResponseV2 item:arrsupplierResponses) {
                            suppliers.add(item.getName());
                            mapsupplierResponses.put(item.getName(),item);
                            if(Objects.equals(item.get_id(), productGeneral.getSupplier())){
                                nameactualproveedor = item.getName();
                            }
                        }
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, suppliers);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        getSp_proveedor.setAdapter(adapter2);

                        selectSpinnerItemByName(getSp_proveedor, nameactualproveedor);

                        System.out.println("obtuvo proveedores");
                        System.out.println("a "+arrsupplierResponses.size());


                    }
                }

            }
            @Override
            public void onFailure(@NonNull Call<List<SupplierResponseV2>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
}