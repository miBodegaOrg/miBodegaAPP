package com.mibodega.mystore.views.signIn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestSetNewPassword;
import com.mibodega.mystore.models.Requests.RequestValidCodeEmail;
import com.mibodega.mystore.models.Responses.SetNewPasswordResponse;
import com.mibodega.mystore.models.Responses.ValidCodeEmailResponse;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.user.ChangePasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetNewPasswordActivity extends AppCompatActivity {
    private TextInputEditText edt_password;
    private Button btn_send;
    private Config config = new Config();
    private Utils utils = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        edt_password = findViewById(R.id.Edt_password_recoverpassword);
        btn_send = findViewById(R.id.Btn_sendNewPassword_recoverpassword);
        String email = getIntent().getStringExtra("email");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(valiteFields(), "")){
                    sendPassword(email);
                }else{
                    Dialog dialog = utils.getAlertCustom(SetNewPasswordActivity.this,"danger","Error",valiteFields(),false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }

            }
        });
    }
    private void sendPassword(String email){
        String code = edt_password.getText().toString();
        RequestSetNewPassword request = new RequestSetNewPassword(email,code);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IUserServices service = retrofit.create(IUserServices.class);
        Call<SetNewPasswordResponse> call = service.changePasswordEmail(request);
        call.enqueue(new Callback<SetNewPasswordResponse>() {
            @Override
            public void onResponse(Call<SetNewPasswordResponse> call, Response<SetNewPasswordResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    SetNewPasswordResponse data = response.body();
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(SetNewPasswordActivity.this,"success","Exitoso","La contraseña se ha reestablecido correctamente",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Intent intent = new Intent(SetNewPasswordActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();



                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        //Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(SetNewPasswordActivity.this,"danger","Error","Error al actualizar perfil",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<SetNewPasswordResponse> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(SetNewPasswordActivity.this,"danger","Error","Error al actualizar perfil",false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                dialog.show();
            }
        });
    }
    public String valiteFields(){
        String message = "";

        if(edt_password.getText().toString().trim().length() == 0){
            message += "- Debe ingresar la nueva contraseña \n";
        }

        if(edt_password.getText().toString().trim().length() != 0){
            int aux = edt_password.getText().toString().length();
            if(aux <6){
                message += "- Debe ingresar un contraseña con minimo 6 caracteres\n";
            }
        }


        return message;

    }
}