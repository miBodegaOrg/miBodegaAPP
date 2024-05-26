package com.mibodega.mystore.views.products;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainNavigationActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.ProductCreateRequest;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        tv_fileName.setText("");

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

    }
    private void setupButtons() {
         btnCamera = findViewById(R.id.Imgb_takeProductPhoto_product);
         btnGallery = findViewById(R.id.Imgb_getproductPhoto_product);
         btnScanProduct = findViewById(R.id.Imgb_scanProductCode_product);

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
        btnScanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        ProductCreateRequest request = new ProductCreateRequest(
                txt_name_product.getText().toString(),
                generateRandomNumber(),
                Double.valueOf(txt_price_product.getText().toString()),
                Integer.valueOf(txt_stock_product.getText().toString()),
                categoryList,
                imagePart
                );

        Call<ProductResponse> call = service.createProduct(request,"Bearer "+config.getJwt());
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

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
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
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            System.out.println("datos");

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





/*
    private void setupButtons() {
        btnCamera = findViewById(R.id.Imgb_takeProductPhoto_product);
        btnGallery = findViewById(R.id.Imgb_getproductPhoto_product);
        btnScanProduct = findViewById(R.id.Imgb_scanProductCode_product);


        btnCamera.setOnClickListener(v -> {
            // Abre la cámara para tomar una foto
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(takePictureIntent);
            }
        });

        btnGallery.setOnClickListener(v -> {
            // Abre la galería para seleccionar una imagen
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(galleryIntent);
        });

        btnScanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });






    }
    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Obtén la imagen de la cámara
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // Guarda la imagen en un archivo y crea el MultipartBody.Part
                    try {
                        saveBitmapToFile(imageBitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Obtén la URI de la imagen seleccionada de la galería
                    Intent data = result.getData();
                    if (data != null) {
                        imageUri = data.getData();
                        // Crea el MultipartBody.Part a partir de la URI de la imagen
                        createImagePart();
                    }
                }
            }
    );


    private void saveBitmapToFile(Bitmap bitmap) throws IOException {
        // Guarda el Bitmap en un archivo
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        // Crea el MultipartBody.Part a partir del archivo
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        tv_fileName.setText("imagecamera.jpg");

    }

    private void createImagePart() {
        // Obtén la ruta del archivo a partir de la URI de la imagen
        String filePath = getPathFromUri(imageUri);
        File file = new File(filePath);

        // Crea el MultipartBody.Part a partir del archivo
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        tv_fileName.setText("imagegallery.jpg");
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;
        if (uri != null) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
        return filePath;
    }
*/


}