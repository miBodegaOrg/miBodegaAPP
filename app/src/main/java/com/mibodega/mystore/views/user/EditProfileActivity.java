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
import com.mibodega.mystore.models.Requests.PromotionRequest;
import com.mibodega.mystore.models.Requests.RequestUpdateProfile;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.PromotionResponse;
import com.mibodega.mystore.models.Responses.UpdateProfileResponse;
import com.mibodega.mystore.services.IPromotionService;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.offers.PromotionActivity;
import com.mibodega.mystore.views.signUp.SignUpShopActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText edt_name,edt_lastname,edt_address,edt_phone,edt_ruc,edt_password;
    private Button btn_update;
    private Utils utils = new Utils();
    private Config config = new Config();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edt_name = findViewById(R.id.Edt_nameProfile_profile);
        edt_lastname = findViewById(R.id.Edt_lastnameProfile_profile);
        edt_address = findViewById(R.id.Edt_addressProfile_profile);
        edt_phone = findViewById(R.id.Edt_phoneProfile_profile);
        edt_ruc =findViewById(R.id.Edt_rucdniProfile_profile);
        edt_password =  findViewById(R.id.Edt_passwordProfile_profile);
        btn_update = findViewById(R.id.Btn_registerProfile_profile);

        String[] nombres= obtenerPrimeraYSegundaPalabra(config.getUserData().getName());
        edt_name.setText(config.getUserData().getName());
        //edt_lastname.setText(nombres[1]);
        edt_address.setText(config.getUserData().getEmail());
        edt_phone.setText(config.getUserData().getPhone());
        edt_ruc.setText(config.getUserData().getUsername());
        edt_password.setText("123456");



        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(valiteFields(), "")){
                    updateUser();
                }else{
                    Dialog dialog = utils.getAlertCustom(EditProfileActivity.this,"danger","Error",valiteFields(),false);
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

    private void updateUser(){
        String name = edt_name.getText().toString();
        RequestUpdateProfile request = new RequestUpdateProfile(name,edt_phone.getText().toString(),edt_address.getText().toString());

        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        IUserServices service = retrofit.create(IUserServices.class);
        Call<UpdateProfileResponse> call = service.put_updateProfile(request, "Bearer " + config.getJwt());
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    UpdateProfileResponse data = response.body();
                    edt_name.setText(data.getName());
                    edt_phone.setText(data.getPhone());
                    edt_address.setText(data.getAddress());
                    config.getUserData().setName(data.getName());
                    config.getUserData().setPhone(data.getPhone());
                    config.getUserData().setEmail(data.getAddress());
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(EditProfileActivity.this,"success","Exitoso","Datos actualizados correctamente",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
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
                    Dialog dialog = utils.getAlertCustom(EditProfileActivity.this,"danger","Error","Error al actualizar perfil",false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Utils utils = new Utils();
                Dialog dialog = utils.getAlertCustom(EditProfileActivity.this,"danger","Error","Error al actualizar perfil",false);
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
        if(edt_name.getText().toString().trim().length() == 0){
            message += "- El nombre y apellido no debe estar vacio \n";
        }
        /*
        if(edt_lastname.getText().toString().trim().length() == 0){
            message += "- Debe ingresar apellidos \n";
        }*/
        if(edt_address.getText().toString().trim().length() == 0){
            message += "- Debe ingresar correo \n";
        }

        if(edt_phone.getText().toString().trim().length() == 0){
            message += "- Debe ingresar telefono \n";
        }
        if(edt_ruc.getText().toString().trim().length() == 0){
            message += "- Debe ingresar RUC \n";
        }
        /*
        if(edt_password.getText().toString().trim().length() == 0){
            message += "- Debe ingresar contraseña \n";
        }*/

        if(edt_phone.getText().toString().trim().length() != 0){
            int aux = edt_phone.getText().toString().length();
            if(aux != 9){
                message += "- Debe ingresar un numero telefono con 9 digitos\n";
            }
        }
/*
        if(edt_password.getText().toString().trim().length() != 0){
            int aux = edt_password.getText().toString().length();
            if(aux < 6){
                message += "- Debe ingresar una contraseña minimo 6 digitos\n";
            }
        }
*/


        return message;

    }
    public String[] obtenerPrimeraYSegundaPalabra(String texto) {
        // Dividir el texto en palabras usando espacio como delimitador
        String[] palabras = texto.trim().split("\\s+");

        // Definir las variables para almacenar las palabras
        String primeraPalabra = palabras.length > 0 ? palabras[0] : "";
        String segundaPalabra = palabras.length > 1 ? palabras[1] : "";

        return new String[] {primeraPalabra, segundaPalabra};
    }

}