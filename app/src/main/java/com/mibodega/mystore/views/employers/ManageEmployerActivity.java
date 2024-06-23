package com.mibodega.mystore.views.employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestEmployee;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterEmployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageEmployerActivity extends AppCompatActivity {
    private TextInputEditText edt_name, edt_lastname,edt_email,edt_dni,edt_phone,edt_password;
    private LinearLayout ly_permisesContainer, ly_permisesCheckList;
    private Button btn_savePermises;
    private Button btn_desactive,btn_delete,btn_active,btn_create,btn_back;
    private String id_employee="";
    private Config config = new Config();
    private EmployeeResponse employeeResponse=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employer);

        edt_name = findViewById(R.id.Edt_nameEmployee_employee);
        edt_lastname = findViewById(R.id.Edt_lastnameEmployee_employee);
        edt_email = findViewById(R.id.Edt_emailEmployee_employee);
        edt_dni = findViewById(R.id.Edt_dniEmployee_employee);
        edt_phone = findViewById(R.id.Edt_phoneEmployee_employee);
        edt_password = findViewById(R.id.Edt_passwordEmployee_employee);

        ly_permisesContainer = findViewById(R.id.Ly_permisesContainer_employee);
        ly_permisesCheckList = findViewById(R.id.Ly_permisesListCheck_employee);
        btn_savePermises = findViewById(R.id.Btn_savePrivilegiesEmployee_employee);
        btn_desactive = findViewById(R.id.Btn_desactiveEmployee_employee);
        btn_delete = findViewById(R.id.Btn_deleteEmployee_employee);
        btn_active = findViewById(R.id.Btn_activeEmployee_employee);
        btn_create = findViewById(R.id.Btn_createEmployee_employee);
        btn_back = findViewById(R.id.Btn_back_employee);

        id_employee = getIntent().getExtras().getString("employee_id");
        if(Objects.equals(id_employee, "0")){
            btn_active.setVisibility(View.GONE);
            btn_desactive.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
            ly_permisesContainer.setVisibility(View.GONE);
        }else {
            btn_active.setVisibility(View.VISIBLE);
            btn_desactive.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_create.setVisibility(View.GONE);
            ly_permisesContainer.setVisibility(View.VISIBLE);
            loadEmployee(id_employee);

        }
        btn_savePermises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_desactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    createEmployee();
                }else{
                    Toast.makeText(getBaseContext(),valiteFields(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void loadEmployee(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IEmployeeServices service = retrofit.create(IEmployeeServices.class);
        Call<EmployeeResponse> call = service.getEmployeeById(id,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeResponse> call, @NonNull Response<EmployeeResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    employeeResponse = response.body();
                    if(employeeResponse!=null){
                        edt_name.setText(employeeResponse.getName());
                        edt_lastname.setText(employeeResponse.getLastname());
                        edt_dni.setText(employeeResponse.getDni());
                        edt_phone.setText(employeeResponse.getPhone());
                        edt_email.setText(employeeResponse.getEmail());
                        edt_password.setText(employeeResponse.getPassword());
                        ly_permisesCheckList.removeAllViews();
                        for (String item : employeeResponse.getPermissions()) {
                            CheckBox checkBox = new CheckBox(getBaseContext());
                            checkBox.setText(item); // Establecer el texto del CheckBox con el valor del permiso
                            ly_permisesCheckList.addView(checkBox); // Agregar el CheckBox al LinearLayout
                        }
                        System.out.println("successfull request");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void createEmployee(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IEmployeeServices service = retrofit.create(IEmployeeServices.class);
        RequestEmployee requestEmployee =  new RequestEmployee(
                edt_name.getText().toString(),
                edt_lastname.getText().toString(),
                edt_email.getText().toString(),
                edt_dni.getText().toString(),
                edt_phone.getText().toString(),
                edt_password.getText().toString());
        Call<EmployeeResponse> call = service.createEmployee(requestEmployee,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeResponse> call, @NonNull Response<EmployeeResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    employeeResponse = response.body();
                    if(employeeResponse!=null){
                        edt_name.setText("");
                        edt_lastname.setText("");
                        edt_email.setText("");
                        edt_dni.setText("");
                        edt_phone.setText("");
                        edt_password.setText("");
                        Toast.makeText(getBaseContext(),"EMPLEADO CREADO",Toast.LENGTH_SHORT).show();
                        System.out.println("successfull request");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public String valiteFields(){
        String message = "ok";
        if(edt_name.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar nombre \n";
        }
        if(edt_lastname.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar apellidos \n";
        }
        if(edt_email.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar correo \n";
        }
        if(edt_dni.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar DNI \n";
        }
        if(edt_phone.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar telefono \n";
        }
        if(edt_password.getText().toString().trim().length() == 0){
            message += "😨 Debe ingresar contraseña \n";
        }
        if(edt_phone.getText().toString().trim().length() != 0){
            int aux = edt_phone.getText().toString().length();
            if(aux != 9){
                message += "😨 Debe ingresar un numero telefono con 8 digitos\n";
            }
        }
        if(edt_dni.getText().toString().trim().length() != 0){
            int aux = edt_dni.getText().toString().length();
            if(aux != 8){
                message += "😨 Debe ingresar un numero telefono con 8 digitos\n";
            }
        }
        if(edt_password.getText().toString().trim().length() != 0){
            int aux = edt_password.getText().toString().length();
            if(aux <6){
                message += "😨 Debe ingresar una contraseña con 6 digitos\n";
            }
        }


        return message;

    }

}