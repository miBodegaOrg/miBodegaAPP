package com.mibodega.mystore.views.user;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.mibodega.mystore.models.Requests.RequestUpdatePassword;
import com.mibodega.mystore.models.Requests.RequestUpdateProfile;
import com.mibodega.mystore.models.Responses.Status;
import com.mibodega.mystore.models.Responses.UpdateProfileResponse;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText edt_lastpass,edt_newpass;
    private Button btn_savePass;

    private Utils utils = new Utils();
    private Config config = new Config();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edt_lastpass = findViewById(R.id.Edt_lastPassword_password);
        edt_newpass = findViewById(R.id.Edt_newPassword_password);
        btn_savePass = findViewById(R.id.Btn_savePassword_password);

        btn_savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(valiteFields(), "")){
                    savePassword();
                }else{
                    Dialog dialog = utils.getAlertCustom(ChangePasswordActivity.this,"danger","Error",valiteFields(),false);
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

    private void savePassword(){
        String lastpas = edt_lastpass.getText().toString();
        String newpas = edt_newpass.getText().toString();
        RequestUpdatePassword request = new RequestUpdatePassword(lastpas,newpas);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IUserServices service = retrofit.create(IUserServices.class);
        Call<Status> call = service.put_updatePassword(request, "Bearer " + config.getJwt());
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    Status data = response.body();
                    if(Objects.equals(data.getMsg(), "Password changed successfully")){
                        Utils utils = new Utils();
                        Dialog dialog = utils.getAlertCustom(ChangePasswordActivity.this,"success","Exitoso"," La contraseña se ha reestablecido correctamente",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            finish();
                            }
                        });
                        dialog.show();

                    }else{
                        Utils utils = new Utils();
                        Dialog dialog = utils.getAlertCustom(ChangePasswordActivity.this,"danger","Error","La contraseña actual es incorrecta",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
                        dialog.show();
                    }


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
                    Dialog dialog = utils.getAlertCustom(ChangePasswordActivity.this,"danger","Error","La contraseña actual es incorrecta",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(ChangePasswordActivity.this,"danger","Error","Error al actualizar contraseña",false);
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
        if(edt_lastpass.getText().toString().trim().length() == 0){
            message += "- Debe ingresar la antigua contraseña \n";
        }

        if(edt_newpass.getText().toString().trim().length() == 0){
            message += "- Debe ingresar la nueva contraseña \n";
        }

        if(edt_newpass.getText().toString().trim().length() != 0){
            int aux = edt_newpass.getText().toString().length();
            if(aux <6){
                message += "- Debe ingresar un contraseña con minimo 6 caracteres\n";
            }
        }


        return message;

    }


}