package com.mibodega.mystore.views.products;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.Display;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.ProductCreateRequest;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductEditActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST = 101;

    private Uri imageUri;
    private MultipartBody.Part imagePart;

    private ImageButton btnCamera ;
    private ImageButton btnGallery;

    private TextInputEditText txt_name_product;
    private TextInputEditText txt_price_product;
    private TextInputEditText txt_stock_product;
    private TextInputEditText txt_code_product;

    private Spinner sp_category_product;
    private TextView tv_fileName;
    private ImageButton btnScanProduct;
    private Spinner getSp_category_product;

    private Button btn_saveProduct;
    private Config config = new Config();

    private Dialog dialogShowProductImage;
    private Bitmap paymentBitmapImg;

    private ImageView imgProduct;
    private RequestBody finalImageBody;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(ProductEditActivity.this, "Escaneo cancelado", Toast.LENGTH_LONG).show();
                } else {
                    String barcodeValue = result.getContents();
                    // Procesa el valor del código de barras
                    txt_code_product.setText(barcodeValue);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        txt_name_product = findViewById(R.id.Edt_name_product);
        txt_price_product = findViewById(R.id.Edt_price_product);
        txt_stock_product = findViewById(R.id.Edt_stock_product);
        txt_code_product = findViewById(R.id.Edt_productCode_product);
        getSp_category_product = findViewById(R.id.Sp_selectProductCategory_product);
        btn_saveProduct = findViewById(R.id.Btn_saveProduct_product);
        tv_fileName = findViewById(R.id.Tv_productFileName_product);
        btnScanProduct = findViewById(R.id.Imgb_scanProductCode_product);


        tv_fileName.setText("");
        setDialogs(this);

        String[] categories = {"BEBIDAS", "SNACKS", "GOLOSINAS", "COMIDA"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getSp_category_product.setAdapter(adapter);

        setupButtons();
        btn_saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProductsData();
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


    }
    private void setupButtons() {
         btnCamera = findViewById(R.id.Imgb_takeProductPhoto_product);
         btnGallery = findViewById(R.id.Imgb_getproductPhoto_product);


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

    private void postProductsData() {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IProductServices service = retrofit.create(IProductServices.class);

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(getSp_category_product.getSelectedItem().toString());
        String code = txt_code_product.getText().toString();
        if(txt_code_product.getText().toString().equals("")){
            code = generateRandomNumber();
        }

        Map<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("name", RequestBody.create(MediaType.parse("text/plain"), txt_name_product.getText().toString()));
        requestMap.put("code", RequestBody.create(MediaType.parse("text/plain"), code));
        requestMap.put("price", RequestBody.create(MediaType.parse("text/plain"), txt_price_product.getText().toString()));
        requestMap.put("stock", RequestBody.create(MediaType.parse("text/plain"), txt_stock_product.getText().toString()));
        requestMap.put("category", RequestBody.create(MediaType.parse("text/plain"), categoryList.toString()));
        requestMap.put("image\"; filename=\"" + "image", finalImageBody);

        Call<ProductResponse> call = service.createProduct(requestMap,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){

                   // pagesProductResponse = response.body();
                    System.out.println("successfull request");
                    finish();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
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




}