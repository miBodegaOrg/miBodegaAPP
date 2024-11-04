package com.mibodega.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.topAppBarMain);
        setSupportActionBar(toolbar);
    }
    protected void setContentLayout(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, findViewById(R.id.content_frame));
    }
}