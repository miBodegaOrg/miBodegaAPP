package com.mibodega.mystore.views.sales;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;

public class DetailSaleActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail_sale);
        setContentLayout(R.layout.activity_detail_sale);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle de Venta");
        }
    }
}