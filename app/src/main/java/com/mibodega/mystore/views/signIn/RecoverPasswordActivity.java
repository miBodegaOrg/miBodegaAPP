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
import com.google.android.material.textfield.TextInputLayout;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestEmailCode;
import com.mibodega.mystore.models.Requests.RequestUpdateProfile;
import com.mibodega.mystore.models.Responses.EmailSendResponse;
import com.mibodega.mystore.models.Responses.UpdateProfileResponse;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.InputValidator;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.signUp.SignUpShopActivity;
import com.mibodega.mystore.views.user.EditProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecoverPasswordActivity extends AppCompatActivity {

    private TextInputEditText edt_email;
    private TextInputLayout tly_email;
    private Button btn_send;
    private Config config = new Config();
    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        edt_email = findViewById(R.id.Edt_email_recoverpassword);
        btn_send = findViewById(R.id.Btn_sendEmail_recoverpassword);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Objects.equals(valiteFields(), "")){
                    sendEmail();
                }else{
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(RecoverPasswordActivity.this,"danger","Error",valiteFields(),false);
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
    private void sendEmail(){
        String email = edt_email.getText().toString();
        RequestEmailCode request = new RequestEmailCode(email);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IUserServices service = retrofit.create(IUserServices.class);
        Call<EmailSendResponse> call = service.sendEmailCode(request);
        call.enqueue(new Callback<EmailSendResponse>() {
            @Override
            public void onResponse(Call<EmailSendResponse> call, Response<EmailSendResponse> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    EmailSendResponse data = response.body();
                    Intent intent = new Intent(RecoverPasswordActivity.this,ValidateEmailActivity.class);
                    intent.putExtra("email",edt_email.getText().toString());
                    startActivity(intent);

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
                    Dialog dialog = utils.getAlertCustom(RecoverPasswordActivity.this,"danger","Error","El correo electrónico ingresado no está asociado a ninguna cuenta",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<EmailSendResponse> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(RecoverPasswordActivity.this,"danger","Error","Error al actualizar perfil",false);
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
        if(edt_email.getText().toString().trim().length() == 0){
            message += "- Debe ingresar correo \n";
        }
        if(!edt_email.getText().toString().isEmpty()){
            InputValidator inputValidator = new InputValidator();
            if(!inputValidator.esEmailValido(edt_email.getText().toString())){
                message += "- Correo no válido \n";
            }
        }
        return message;

    }
}