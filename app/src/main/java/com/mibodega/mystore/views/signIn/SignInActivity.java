package com.mibodega.mystore.views.signIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.mibodega.mystore.MainNavigationActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestSignIn;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.models.Responses.SignInResponseToken;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBfunctionsTableData;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.LoadingDialogAdapter;
import com.mibodega.mystore.views.signUp.SignUpShopActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private TextView Tv_questionForgotPassword_login;
    private CheckBox cbx_rememberUser;
    private DBfunctionsTableData dBfunctionsTableData = new DBfunctionsTableData();
    private LoadingDialogAdapter loadingDialog = new LoadingDialogAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(this);
        edt_user = findViewById(R.id.Tedt_user);
        edt_password = findViewById(R.id.Tedt_password);
        btn_moveToHome = findViewById(R.id.Btn_moveToHome_login);
        Tv_questionForgotPassword_login = findViewById(R.id.Tv_questionForgotPassword_login);
        cbx_rememberUser = findViewById(R.id.Cbx_remenberUser_signin);
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
        Tv_questionForgotPassword_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg = new Intent(getBaseContext(), SignUpShopActivity.class);
                startActivity(mg);
            }
        });

        ImageView imageView = findViewById(R.id.Imv_profileImageSigIn);
        Glide.with(this)
                .load(R.drawable.logo_size2)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        if(!Objects.equals(dBfunctionsTableData.get_user_save(getBaseContext()), "")){
            System.out.println("remember "+dBfunctionsTableData.get_user_save(getBaseContext()));
            postsigninToken(dBfunctionsTableData.get_user_save(getBaseContext()));
        }


    }
    private void postData(String user, String pass) {
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUserServices usersService = retrofit.create(IUserServices.class);
        RequestSignIn modal = new RequestSignIn(user, pass, cbx_rememberUser.isChecked());

        Call<SignInResponse> call = usersService.post_signin(modal);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()) {
                    loadingDialog.dismissDialog();
                    SignInResponse data = response.body();
                    config.setJwt(data.getToken());
                    System.out.println("*"+data.getToken()+"*");
                    dBfunctionsTableData.insertUserRemember(getBaseContext(),data.getToken());
                    config.setUserData(data);
                    Intent moveHMA = new Intent(getApplicationContext(), MainNavigationActivity.class);
                    startActivity(moveHMA);

                }else{
                    loadingDialog.dismissDialog();
                    Dialog dialog = utils.getAlertCustom(SignInActivity.this, "danger", "Alerta", "Alerta  - DNI / RUC o Contraseña incorrecta", false);
                    dialog.show();
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                loadingDialog.dismissDialog();
                System.out.println(t.toString());
                System.out.println(t.getMessage());
                Dialog dialog = utils.getAlertCustom(SignInActivity.this, "danger", "Alerta", "Error en servicios", false);
                dialog.show();
            }
        });
    }

    private void postsigninToken(String token) {
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUserServices usersService = retrofit.create(IUserServices.class);

        Call<SignInResponseToken> call = usersService.post_signInToken("Bearer "+token);
        call.enqueue(new Callback<SignInResponseToken>() {
            @Override
            public void onResponse(Call<SignInResponseToken> call, Response<SignInResponseToken> response) {
                if (response.isSuccessful()) {
                    loadingDialog.dismissDialog();
                    SignInResponseToken data = response.body();
                    config.setJwt(token);
                    config.setUserData(new SignInResponse(data.getName(),data.getUsername(),data.getPhone(),data.getType(),token));
                    Intent moveHMA = new Intent(getApplicationContext(), MainNavigationActivity.class);
                    startActivity(moveHMA);

                }else{
                    loadingDialog.dismissDialog();
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
                   dBfunctionsTableData.cleanTokensSignIn(getBaseContext());
                    /*
                   Dialog dialog = utils.getAlertCustom(SignInActivity.this, "danger", "Alerta", "Token expirado", false);
                    dialog.show();
                    System.out.println("error");*/
                }
            }

            @Override
            public void onFailure(Call<SignInResponseToken> call, Throwable t) {
                loadingDialog.dismissDialog();
                System.out.println(t.toString());
                System.out.println(t.getMessage());
                Dialog dialog = utils.getAlertCustom(SignInActivity.this, "danger", "Alerta", "Error en servicios", false);
                dialog.show();
            }
        });
    }

}