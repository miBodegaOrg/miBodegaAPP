package com.mibodega.mystore.views.signIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainNavigationActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestSignIn;
import com.mibodega.mystore.models.Requests.RequestSignUp;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private Button btn_moveToHome;
    private TextInputEditText edt_user;
    private TextInputEditText edt_password;
    private Config config = new Config();
    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edt_user = findViewById(R.id.Tedt_user);
        edt_password = findViewById(R.id.Tedt_password);
        btn_moveToHome = findViewById(R.id.Btn_moveToHome_login);
        btn_moveToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_user.getText().toString().length()>5){
                    String user = edt_user.getText().toString();
                    String pass = edt_password.getText().toString();
                    postData(user,pass);
                }
            }
        });
    }
    private void postData(String user, String pass) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUserServices usersService = retrofit.create(IUserServices.class);
        RequestSignIn modal = new RequestSignIn(user, pass);
        System.out.println("user "+user);
        System.out.println("password "+pass);


        Call<SignInResponse> call = usersService.post_signin(modal);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()) {
                    SignInResponse data = response.body();
                        config.setJwt(data.getToken());
                        System.out.println(config.getJwt());
                    Intent moveHMA = new Intent(getApplicationContext(), MainNavigationActivity.class);
                    startActivity(moveHMA);

                }else{
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                System.out.println(t.toString());
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en el Servidor Off", Toast.LENGTH_LONG).show();
            }
        });
    }

}