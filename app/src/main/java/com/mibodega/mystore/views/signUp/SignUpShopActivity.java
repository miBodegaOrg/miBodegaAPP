package com.mibodega.mystore.views.signUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestEmployee;
import com.mibodega.mystore.models.Requests.RequestSignUp;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.models.Responses.SignUpResponse;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.services.IUserServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.views.employers.ManageEmployerActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpShopActivity extends AppCompatActivity {
    private TextInputEditText edt_nameSignUp_shop,edt_lastnameSignUp_shop,edt_phoneSignUp_shop,edt_rucSignUp_shop,edt_passwordSignUp_shop, edt_addressSignUp_shop;
    private Button btn_register;
    private TextView tv_moveSignIn;
    private Config config = new Config();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);
        edt_nameSignUp_shop = findViewById(R.id.Edt_nameSignUp);
        edt_lastnameSignUp_shop = findViewById(R.id.Edt_lastnameSignUp);
        edt_phoneSignUp_shop = findViewById(R.id.Edt_phoneSignUp);
        edt_rucSignUp_shop = findViewById(R.id.Edt_rucdniSignUp);
        edt_passwordSignUp_shop = findViewById(R.id.Edt_passwprdSignUp);
        edt_addressSignUp_shop = findViewById(R.id.Edt_addressSignUp);

        btn_register = findViewById(R.id.Btn_registerSignUp);
        tv_moveSignIn = findViewById(R.id.Tv_moveSignIn);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    createShop();
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_moveSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public String valiteFields(){
        String message = "ok";
        if(edt_nameSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar nombre \n";
        }
        if(edt_lastnameSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar apellidos \n";
        }
        if(edt_addressSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar dirección \n";
        }

        if(edt_phoneSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar correo \n";
        }
        if(edt_rucSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar RUC \n";
        }
        if(edt_passwordSignUp_shop.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar contraseña \n";
        }
        if(edt_phoneSignUp_shop.getText().toString().trim().length() != 0){
            int aux = edt_phoneSignUp_shop.getText().toString().length();
            if(aux != 9){
                message += "😨 Debe ingresar un numero telefono con 9 digitos\n";
            }
        }
        if(edt_rucSignUp_shop.getText().toString().trim().length() != 0){
            int aux = edt_rucSignUp_shop.getText().toString().length();
            if(aux != 11&&aux!=8){
                message += "😨 Debe ingresar un numero de ruc con minimo 8 digitos\n";
            }
        }
        if(edt_passwordSignUp_shop.getText().toString().trim().length() != 0){
            int aux = edt_passwordSignUp_shop.getText().toString().length();
            if(aux < 6){
                message += "😨 Debe ingresar una contraseña minimo 6 digitos\n";
            }
        }



        return message;

    }


    public void createShop(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IUserServices service = retrofit.create(IUserServices.class);
        String namefinal = edt_nameSignUp_shop.getText().toString()+" "+edt_lastnameSignUp_shop.getText().toString();
        RequestSignUp request =  new RequestSignUp(
                namefinal,
                edt_addressSignUp_shop.getText().toString(),
                edt_phoneSignUp_shop.getText().toString(),
                edt_rucSignUp_shop.getText().toString(),
                edt_passwordSignUp_shop.getText().toString());
        System.out.println(namefinal);
        System.out.println(edt_addressSignUp_shop.getText().toString());
        System.out.println(edt_phoneSignUp_shop.getText().toString());
        System.out.println(edt_rucSignUp_shop.getText().toString());
        System.out.println(edt_passwordSignUp_shop.getText().toString());


        Call<SignUpResponse> call = service.post_signup(request);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    SignUpResponse user = response.body();
                    if(user!=null){
                        edt_nameSignUp_shop.setText("");
                                edt_lastnameSignUp_shop.setText("");
                                edt_addressSignUp_shop.setText("");
                                edt_phoneSignUp_shop.setText("");
                                edt_rucSignUp_shop.setText("");
                                edt_passwordSignUp_shop.setText("");

                        Toast.makeText(getBaseContext(),"BODEGUERO CREADO",Toast.LENGTH_SHORT).show();
                        System.out.println("successfull request");
                        finish();
                        Utils utils = new Utils();
                        Dialog dialog = utils.getAlertCustom(SignUpShopActivity.this,"successs","Exitoso","Bodeguero creado",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                        dialog.show();
                    }

                }
            }
            @Override
            public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
}