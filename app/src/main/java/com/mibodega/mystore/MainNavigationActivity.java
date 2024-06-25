package com.mibodega.mystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.mibodega.mystore.databinding.ActivityMainNavigationBinding;

import com.mibodega.mystore.views.DashboardsFragment;
import com.mibodega.mystore.views.HomeFragment;
import com.mibodega.mystore.views.ProductsFragment;
import com.mibodega.mystore.views.ProfileFragment;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;
import com.mibodega.mystore.views.chatbot.ChatListActivity;
import com.mibodega.mystore.views.products.ProductEditActivity;
import com.mibodega.mystore.views.sales.SaleProductsActivity;


public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;
    private FloatingActionButton btn_moveSale;
    private FloatingActionButton btn_moveChat;

    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity HomeMenuActivity = this;

        //dbconfig = new DBConfig(getApplicationContext());

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setTitle("Hola, Bodeguero");

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

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


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == binding.bottomNavigationView.getMenu().findItem(R.id.home).getItemId()) {
                    replaceFragment(new HomeFragment());
                } else if (itemId == binding.bottomNavigationView.getMenu().findItem(R.id.dashboard).getItemId()) {
                    replaceFragment(new DashboardsFragment());
                } else if (itemId == binding.bottomNavigationView.getMenu().findItem(R.id.products).getItemId()) {
                    replaceFragment(new ProductsFragment());
                } else if (itemId == binding.bottomNavigationView.getMenu().findItem(R.id.profile).getItemId()) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });


        binding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                switch (item.getItemId()){
                    case R.id.singoff:
                        View dialogView = getLayoutInflater().from(HomeActivity.this).inflate(R.layout.progress_dialog, null);
                        loadingDialog.startLoadingDialog(HomeActivity.this,dialogView,"close session");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                utils.cerrarSesionYSalir(HomeMenuActivity);
                            }
                        },1000);
                        break;
                }*/
                return true;
            }
        });

        binding.FabtnMoveSaleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHMA = new Intent(getBaseContext(), SaleProductsActivity.class);
                startActivity(moveHMA);
            }
        });

        binding.FabtnMoveChatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHMA = new Intent(getBaseContext(), ChatListActivity.class);
                startActivity(moveHMA);
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}