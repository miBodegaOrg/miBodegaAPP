package com.mibodega.mystore.views.products;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;

public class ProductSupplierActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_supplier);
        setContentLayout(R.layout.activity_product_supplier);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Proveedor de Producto");
        }
    }
}