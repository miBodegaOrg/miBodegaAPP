package com.mibodega.mystore.views.employers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;

public class ProfileEmployeeActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_employee);
        setContentLayout(R.layout.activity_profile_employee);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalles de Empleados");
        }
    }
}