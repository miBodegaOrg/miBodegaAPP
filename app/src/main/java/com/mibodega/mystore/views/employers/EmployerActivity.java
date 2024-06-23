package com.mibodega.mystore.views.employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.services.IEmployeeServices;
import com.mibodega.mystore.services.IPurchasesService;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterEmployee;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterPurchase;
import com.mibodega.mystore.views.supplier.SupplierRegisterActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployerActivity extends AppCompatActivity {

    private TextInputEditText searchEmployer;
    private FloatingActionButton btn_newEmployee;
    private RecyclerView rv_employeeList;
    private ArrayList<EmployeeResponse> arrEmployeeList;
    private Config config = new Config();
    private RecyclerViewAdapterEmployee adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        searchEmployer = findViewById(R.id.Edt_searchEmployee_employee);
        btn_newEmployee = findViewById(R.id.Btn_addNewEmployee_employee);
        rv_employeeList = findViewById(R.id.Rv_employeeList_employee);
        btn_newEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("employee_id","0");
                Intent mg = new Intent(getBaseContext(),ManageEmployerActivity.class);
                mg.putExtras(bundle);
                startActivity(mg);
            }
        });
        searchEmployer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loadEmployers();
    }
    public void loadEmployers(){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IEmployeeServices service = retrofit.create(IEmployeeServices.class);
        Call<List<EmployeeResponse>> call = service.getEmployees("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<EmployeeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<EmployeeResponse>> call, @NonNull Response<List<EmployeeResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    arrEmployeeList = (ArrayList<EmployeeResponse>) response.body();
                    if(arrEmployeeList!=null){
                        rv_employeeList.removeAllViews();
                        adapter = new RecyclerViewAdapterEmployee(getBaseContext(), arrEmployeeList, new RecyclerViewAdapterEmployee.OnDetailItem() {
                            @Override
                            public void onClick(EmployeeResponse item) {
                                Bundle bundle = new Bundle();
                                bundle.putString("employee_id",item.get_id());
                                Intent mg = new Intent(getBaseContext(),ManageEmployerActivity.class);
                                mg.putExtras(bundle);
                                startActivity(mg);
                            }
                        });
                        rv_employeeList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_employeeList.setAdapter(adapter);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<EmployeeResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployers();
    }
}