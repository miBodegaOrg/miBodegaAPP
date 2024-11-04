package com.mibodega.mystore.views.employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestEmployee;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.LoadingDialogAdapter;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;
import com.mibodega.mystore.views.products.ProductEditActivity;
import com.mibodega.mystore.views.supplier.SupplierRegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageEmployerActivity extends MainActivity {
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
    private LoadingDialogAdapter loadingDialog = new LoadingDialogAdapter();
    private Utils utils = new Utils();
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_manage_employer);
        setContentLayout(R.layout.activity_manage_employer);

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
        ly_permisesCheckList.removeAllViews();
        ly_permisesContainer.setVisibility(View.VISIBLE);
        for (String item : config.getArrPermises()) {
            System.out.println("permises "+item.toString());
            CheckBox checkBox = new CheckBox(getBaseContext());
            checkBox.setText(item);
            checkBox.setChecked(true);
            // Establecer el texto del CheckBox con el valor del permiso
            ly_permisesCheckList.addView(checkBox); // Agregar el CheckBox al LinearLayout
            mapEsPermisses.put(item,checkBox);
        }

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
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Nuevo Empleado");
            }
        }else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Detalles Empleado");
            }
            btn_update.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_create.setVisibility(View.GONE);
            //edt_name.setEnabled(false);
            //edt_lastname.setEnabled(false);
            //edt_email.setEnabled(false);
            //edt_dni.setEnabled(false);
            //edt_phone.setEnabled(false);
            edt_password.setText("");
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
                if(Objects.equals(valiteFieldsupdate(), "")){
                    curremtPermises.clear();
                    ArrayList<String> aux = new ArrayList<>();
                    for (String item : config.getArrPermises()) {
                        CheckBox checkBox = mapEsPermisses.get(item);
                        assert checkBox != null;
                        if(checkBox.isChecked()){
                            aux.add(item);
                            System.out.println(" permiso "+item);
                        }
                    }
                    curremtPermises = aux;
                    updateEmployee(id_employee);
                }else{
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this,"danger","Error",valiteFieldsupdate(),false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    dialog.show();
                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // agregar dialog para preguntar si esta seguro de eliminar el empleado
                dialog.show();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curremtPermises.clear();
                ArrayList<String> aux = new ArrayList<>();
                for (String item : config.getArrPermises()) {
                    CheckBox checkBox = mapEsPermisses.get(item);
                    assert checkBox != null;
                    if(checkBox.isChecked()){
                        aux.add(item);
                        System.out.println(" permiso "+item);
                    }
                }
                curremtPermises = aux;

                if(Objects.equals(valiteFields(), "")){
                    createEmployee();
                }else{
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this,"danger","Error",valiteFields(),false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    dialog.show();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadDialog(ManageEmployerActivity.this);
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
                        for (String item: config.getArrPermises()) {
                            CheckBox checkBox = mapEsPermisses.get(item);
                            checkBox.setChecked(false);
                        }
                        for (String item:
                                employeeResponse.getPermissions()) {
                            CheckBox checkBox = mapEsPermisses.get(item);
                            System.out.println(item);
                            checkBox.setChecked(true);
                        }
                        Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this, "success", "Exitoso", "El empleado ha sido editado correctamente", false);
                        dialog.show();
                    }
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this, "danger", "Error", "No se actualizo", false);
                    dialog.show();
                    //manejar el error de dni que ya existe,
                    //Toast.makeText(getBaseContext(),"Mensaje: DNI ya fue registrado, usa ono distinto",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    public void deleteEmployee(String id){
        View dialogView = getLayoutInflater().from(getBaseContext()).inflate(R.layout.progress_dialog, null);
        loadingDialog.startLoadingDialog(this, dialogView, "Cargando","Porfavor espere...");

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
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this, "success", "Eliminado", "Empleado eliminado", false);
                    if(employeeResponse!=null){
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                loadingDialog.dismissDialog();
                                finish();
                            }
                        });
                    }
                    loadingDialog.dismissDialog();
                }else{
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this, "warning", "No Eliminado", "hubo un error", false);
                    dialog.show();
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
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            loadingDialog.dismissDialog();
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NonNull Call<EmployeeResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
                loadingDialog.dismissDialog();
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
                        for (String item : config.getArrPermises()) {
                           mapEsPermisses.get(item).setChecked(false);
                           System.out.println("- "+item);
                        }
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
                        Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this,"success","Exitoso","Se creó exitosamente el empleado",false);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        dialog.show();
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
        String message = "";
        if(edt_name.getText().toString().trim().length() == 0){
            message += "- Debe ingresar nombre \n";
        }
        if(edt_lastname.getText().toString().trim().length() == 0){
            message += "- Debe ingresar apellidos \n";
        }
        if(curremtPermises.size()< 1){
            message += "- Debe seleccionar al menos un permiso \n";
        }
        if(edt_email.getText().toString().trim().length() == 0){
            message += "- Debe ingresar correo \n";
        }
        if(edt_dni.getText().toString().trim().length() == 0){
            message += "- Debe ingresar DNI \n";
        }
        if(edt_phone.getText().toString().trim().length() == 0){
            message += "- Debe ingresar telefono \n";
        }
        if(edt_password.getText().toString().trim().length() == 0){
            message += "- Debe ingresar contraseña \n";
        }
        if(edt_phone.getText().toString().trim().length() != 0){
            int aux = edt_phone.getText().toString().length();
            if(aux != 9){
                message += "- Debe ingresar un numero telefono con 9 digitos\n";
            }
        }
        if(edt_dni.getText().toString().trim().length() != 0){
            int aux = edt_dni.getText().toString().length();
            if(aux != 8){
                message += "- Debe ingresar un numero telefono con 8 digitos\n";
            }
        }
        if(edt_password.getText().toString().trim().length() != 0){
            int aux = edt_password.getText().toString().length();
            if(aux <6){
                message += "- Debe ingresar una contraseña con 6 digitos\n";
            }
        }


        return message;

    }
    public String valiteFieldsupdate(){
        String message = "";
        if(edt_name.getText().toString().trim().length() == 0){
            message += "- Debe ingresar nombre \n";
        }
        if(edt_lastname.getText().toString().trim().length() == 0){
            message += "- Debe ingresar apellidos \n";
        }
        if(curremtPermises.size()< 1){
            message += "- Debe seleccionar al menos un permiso \n";
        }
        if(edt_email.getText().toString().trim().length() == 0){
            message += "- Debe ingresar correo \n";
        }
        if(edt_dni.getText().toString().trim().length() == 0){
            message += "- Debe ingresar DNI \n";
        }
        if(edt_phone.getText().toString().trim().length() == 0){
            message += "- Debe ingresar telefono \n";
        }

        if(edt_phone.getText().toString().trim().length() != 0){
            int aux = edt_phone.getText().toString().length();
            if(aux != 9){
                message += "- Debe ingresar un numero telefono con 8 digitos\n";
            }
        }
        if(edt_dni.getText().toString().trim().length() != 0){
            int aux = edt_dni.getText().toString().length();
            if(aux != 8){
                message += "- Debe ingresar un numero telefono con 8 digitos\n";
            }
        }
        if(edt_password.getText().toString().trim().length() != 0){
            int aux = edt_password.getText().toString().length();
            if(aux <6){
                message += "- Debe ingresar una contraseña con 6 digitos\n";
            }
        }


        return message;

    }

    public void loadDialog(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete_employee);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroun_custom_rectangle);
        }
        Display display;
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display = context.getDisplay();
            display.getSize(size);
        } else {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                display = windowManager.getDefaultDisplay();
                display.getSize(size);
            }
        }
        int screenWidth = size.x;
        int dialogWidth = (int) (screenWidth * 0.9);
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        TextInputEditText editText = dialog.findViewById(R.id.Edt_confirWordDelete_dialog);
        ImageButton btn_close = dialog.findViewById(R.id.Imgb_custom_closeDialog);

        Button btn_accept = dialog.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("ELIMINAR")){
                    deleteEmployee(id_employee);
                    dialog.dismiss();
                }else{
                    Dialog dialog = utils.getAlertCustom(ManageEmployerActivity.this,"danger","Error","Ingresa correctamente el mensaje de confirmación",false);
                    dialog.show();
                }

            }
        });
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


}