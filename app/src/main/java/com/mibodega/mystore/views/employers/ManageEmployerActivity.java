package com.mibodega.mystore.views.employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestEmployee;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageEmployerActivity extends AppCompatActivity {
    private TextInputEditText edt_name, edt_lastname,edt_email,edt_dni,edt_phone,edt_password;
    private LinearLayout ly_permisesContainer, ly_permisesCheckList;
    private Button btn_saveChangesEmployee;
    private Button btn_update,btn_delete,btn_create,btn_back;
    private String id_employee="";
    private Config config = new Config();
    private EmployeeResponse employeeResponse=null;
    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private Map<String,CheckBox> mapEsPermisses = new HashMap<>();
    private ArrayList<String> curremtPermises = new ArrayList<>();
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
        btn_saveChangesEmployee = findViewById(R.id.Btn_savePrivilegiesEmployee_employee);
        btn_update = findViewById(R.id.Btn_updateEmployee_employee);
        btn_delete = findViewById(R.id.Btn_deleteEmployee_employee);
        btn_create = findViewById(R.id.Btn_createEmployee_employee);
        btn_back = findViewById(R.id.Btn_back_employee);

        id_employee = getIntent().getExtras().getString("employee_id");
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
        if(Objects.equals(id_employee, "0")){
            btn_saveChangesEmployee.setVisibility(View.GONE);
            btn_update.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }else {

            btn_update.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_create.setVisibility(View.GONE);
            edt_name.setEnabled(false);
            edt_lastname.setEnabled(false);
            edt_email.setEnabled(false);
            edt_dni.setEnabled(false);
            edt_phone.setEnabled(false);
            edt_password.setEnabled(false);
            loadEmployee(id_employee);

        }
        btn_saveChangesEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_saveChangesEmployee.getText()=="Editar"){
                    edt_name.setEnabled(true);
                    edt_lastname.setEnabled(true);
                    edt_email.setEnabled(true);
                    edt_dni.setEnabled(true);
                    edt_phone.setEnabled(true);
                    edt_password.setEnabled(true);
                    ly_permisesCheckList.setEnabled(true);
                    btn_saveChangesEmployee.setText("Guardar");

                }else{
                    edt_name.setEnabled(false);
                    edt_lastname.setEnabled(false);
                    edt_email.setEnabled(false);
                    edt_dni.setEnabled(false);
                    edt_phone.setEnabled(false);
                    edt_password.setEnabled(false);
                    ly_permisesCheckList.setEnabled(false);

                    ArrayList<String> aux = new ArrayList<>();
                    for (String item : config.getArrPermises()) {
                        CheckBox checkBox = mapEsPermisses.get(item);
                        assert checkBox != null;
                        if(checkBox.isChecked()){
                            aux.add(item);
                        }
                    }
                    curremtPermises = aux;
                    btn_saveChangesEmployee.setText("Editar");

                }
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(id_employee);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // agregar dialog para preguntar si esta seguro de eliminar el empleado
                deleteEmployee(id_employee);
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(valiteFields(), "ok")){
                    ArrayList<String> aux = new ArrayList<>();
                    for (String item : config.getArrPermises()) {
                        CheckBox checkBox = mapEsPermisses.get(item);
                        assert checkBox != null;
                        if(checkBox.isChecked()){
                            aux.add(item);
                        }
                    }
                    curremtPermises = aux;
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

        ly_permisesCheckList.removeAllViews();
        ly_permisesContainer.setVisibility(View.VISIBLE);
        for (String item : config.getArrPermises()) {
            CheckBox checkBox = new CheckBox(getBaseContext());
            checkBox.setText(item); // Establecer el texto del CheckBox con el valor del permiso
            ly_permisesCheckList.addView(checkBox); // Agregar el CheckBox al LinearLayout
            mapEsPermisses.put(item,checkBox);
        }

    }

    public void updateEmployee(String id){
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
                edt_password.getText().toString(),
                curremtPermises);
        Call<EmployeeResponse> call = service.updateEmployeeById(id,requestEmployee,"Bearer "+config.getJwt());
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
                        edt_email.setText(employeeResponse.getEmail());
                        edt_dni.setText(employeeResponse.getDni());
                        edt_phone.setText(employeeResponse.getPhone());
                        edt_password.setText("");
                        curremtPermises = employeeResponse.getPermissions();
                        for (String item:
                                employeeResponse.getPermissions()) {
                            CheckBox checkBox = mapEsPermisses.get(item);
                            checkBox.setChecked(true);
                        }
                        Toast.makeText(getBaseContext(),"EMPLEADO ACTUALIZADO",Toast.LENGTH_SHORT).show();
                        System.out.println("successfull request");
                    }
                }else{
                    //manejar el error de dni que ya existe,
                    Toast.makeText(getBaseContext(),"Mensaje: DNI ya fue registrado, usa ono distinto",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void deleteEmployee(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IEmployeeServices service = retrofit.create(IEmployeeServices.class);
        Call<EmployeeResponse> call = service.deleteEmployeeById(id,"Bearer "+config.getJwt());
        call.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeResponse> call, @NonNull Response<EmployeeResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    employeeResponse = response.body();
                    if(employeeResponse!=null){
                        Toast.makeText(getBaseContext(),"Empleado Eliminado",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
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
                        edt_password.setText("");
                        curremtPermises = employeeResponse.getPermissions();
                        for (String item:
                             employeeResponse.getPermissions()) {
                            CheckBox checkBox = mapEsPermisses.get(item);
                            checkBox.setChecked(true);
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
                edt_password.getText().toString(),
                curremtPermises);
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