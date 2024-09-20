package com.mibodega.mystore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mibodega.mystore.databinding.ActivityMainNavigationBinding;

import com.mibodega.mystore.models.Requests.RequestMessage;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBConfig;
import com.mibodega.mystore.shared.SharedPreferencesHelper;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.DashboardsFragment;
import com.mibodega.mystore.views.HomeFragment;
import com.mibodega.mystore.views.ProductsFragment;
import com.mibodega.mystore.views.ProfileFragment;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;
import com.mibodega.mystore.views.chatbot.ChatListActivity;
import com.mibodega.mystore.views.products.ProductEditActivity;
import com.mibodega.mystore.views.sales.SaleProductsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;
    private FloatingActionButton btn_moveSale;
    private FloatingActionButton btn_moveChat;

    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private Utils utils = new Utils();
    private Config config = new Config();

    //sharepreferences action once per day
    private SharedPreferencesHelper preferencesHelper;
    private static final String KEY_LAST_DIALOG_DATE = "lastDialogDate";

    //por rango tiempo minutos
    private static final String KEY_LAST_SEND_TIMESTAMP = "lastSendTimestamp";
    private static final int INTERVAL_MINUTES = 3; // Intervalo de 30 minutos

    private static DBConfig dbconfig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Obtén el token de registro FCM
                        String token = task.getResult();
                        // Envíalo a tu servidor o guárdalo localmente
                        System.out.println("* "+token);
                    }
                });

        Activity HomeMenuActivity = this;
        dbconfig = new DBConfig(getApplicationContext());
        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utils.getNotificacionPush(this,"Bienvenido de nuevo","Mi Bodega",null);
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

        preferencesHelper = new SharedPreferencesHelper(this);
        /*
        // Verificar si ha pasado 1 día desde la última vez que se mostró el diálogo
        if (preferencesHelper.hasIntervalPassed(KEY_LAST_DIALOG_DATE, 1)) {
            loadCreateChat();
            // Guardar la fecha actual
            preferencesHelper.putCurrentDate(KEY_LAST_DIALOG_DATE);
        }*/

        //si la fecha cambio, entonces crea un nuevo chat
        if (preferencesHelper.isNewDay(KEY_LAST_DIALOG_DATE)) {
            //showDialog();
            loadCreateChat();
            // Guardar la fecha actual en la zona horaria de Perú
            preferencesHelper.putCurrentDate(KEY_LAST_DIALOG_DATE);
        }


    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bienvenido");
        builder.setMessage("El Chabot ya conoce todos datos de tu bodega hasta este momento. Carga una vez cada día.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void loadCreateChat(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de conexión
                        .readTimeout(30, TimeUnit.SECONDS) // Tiempo de lectura
                        .build())
                .build();

        IChatServices service = retrofit.create(IChatServices.class);
        RequestMessage message = new RequestMessage("soy un bodeguero, quiero hacer consultas sobre gestion y deseo que  respondas de forma concreta");
        Call<MessageResponseGpt> call = service.createContextCreateChat(message,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<MessageResponseGpt>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponseGpt> call, @NonNull Response<MessageResponseGpt> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    MessageResponseGpt messageRptContext =response.body();
                    if(messageRptContext!=null){
                        showDialog();
                    }
                    System.out.println("successfull request");
                }else{
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
                }
            }
            @Override
            public void onFailure(@NonNull Call<MessageResponseGpt> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }


}