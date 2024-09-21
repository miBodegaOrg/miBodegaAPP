package com.mibodega.mystore.views.dashboards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;

public class PredictionDataActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_prediction_data);
        setContentLayout(R.layout.activity_prediction_data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Datos Predicciones");
        }
    }
}