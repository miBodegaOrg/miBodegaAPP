package com.mibodega.mystore.views.products;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;

import com.mibodega.mystore.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductEditActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;

    private Uri imageUri;
    private MultipartBody.Part imagePart;

    private ImageButton btnCamera ;
    private ImageButton btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        setupButtons();

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

    private void setupButtons() {
       btnCamera = findViewById(R.id.Imgb_takeProductPhoto_product);
       btnGallery = findViewById(R.id.Imgb_getproductPhoto_product);

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
    }
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

    }

    private void createImagePart() {
        // Obtén la ruta del archivo a partir de la URI de la imagen
        String filePath = getPathFromUri(imageUri);
        File file = new File(filePath);

        // Crea el MultipartBody.Part a partir del archivo
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
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



}