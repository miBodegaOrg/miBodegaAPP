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
import com.mibodega.mystore.models.Requests.RequestEmailCode;
import com.mibodega.mystore.models.Requests.RequestValidCodeEmail;
import com.mibodega.mystore.models.Responses.EmailSendResponse;
import com.mibodega.mystore.models.Responses.ValidCodeEmailResponse;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.InputValidator;
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

public class ValidateEmailActivity extends AppCompatActivity {

    private TextInputEditText edt_code;
    private Button btn_send;
    private Config config = new Config();
    private Utils utils = new Utils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_email);
        edt_code = findViewById(R.id.Edt_code_recoverpassword);
        btn_send = findViewById(R.id.Btn_sendCode_recoverpassword);

        String email = getIntent().getStringExtra("email");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Objects.equals(valiteFields(), "")){
                    sendCode(email);
                }else{
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(ValidateEmailActivity.this,"danger","Error",valiteFields(),false);
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
    private void sendCode(String email){
        String code = edt_code.getText().toString();
        RequestValidCodeEmail request = new RequestValidCodeEmail(code,email);

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IUserServices service = retrofit.create(IUserServices.class);
        Call<ValidCodeEmailResponse> call = service.validateCode(request);
        call.enqueue(new Callback<ValidCodeEmailResponse>() {
            @Override
            public void onResponse(Call<ValidCodeEmailResponse> call, Response<ValidCodeEmailResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    ValidCodeEmailResponse data = response.body();
                    Intent intent = new Intent(ValidateEmailActivity.this, SetNewPasswordActivity.class);
                    intent.putExtra("email",email);
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
                    Dialog dialog = utils.getAlertCustom(ValidateEmailActivity.this,"danger","Error","El código es invalido",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ValidCodeEmailResponse> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(ValidateEmailActivity.this,"danger","Error","Error al actualizar perfil",false);
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
        if(edt_code.getText().toString().trim().length() == 0){
            message += "- Debe ingresar código \n";
        }
        return message;

    }
}