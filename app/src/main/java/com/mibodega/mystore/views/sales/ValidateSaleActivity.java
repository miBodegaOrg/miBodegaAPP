package com.mibodega.mystore.views.sales;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.services.ISaleServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.SaleTemporalList;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ValidateSaleActivity extends AppCompatActivity {

    private TextView totalSale, subtotalSale, discountSale, igvSale;
    private Button btn_paidSale,btn_cancelSale, btn_backSale;
    private Config config = new Config();
    private SaleTemporalList saleTemporalList = new SaleTemporalList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_sale);
        totalSale = findViewById(R.id.Tv_totalSaleDetail);
        subtotalSale = findViewById(R.id.Tv_subTotalSaleDetail);
        discountSale = findViewById(R.id.Tv_discountSaleDetail);
        igvSale = findViewById(R.id.Tv_igvSaleDetail);

        btn_paidSale = findViewById(R.id.Btn_paidSaleDetail);
        btn_cancelSale = findViewById(R.id.Btn_cancelSaleDetail);
        btn_backSale  = findViewById(R.id.Btn_backSaleDetail);


        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        totalSale.setText("S/ " + decimalFormat.format(saleTemporalList.getSaleCurrent().getTotal()));
        subtotalSale.setText("S/ " + decimalFormat.format(saleTemporalList.getSaleCurrent().getSubtotal()));
        discountSale.setText("S/ " + decimalFormat.format(saleTemporalList.getSaleCurrent().getDiscount()));
        igvSale.setText("S/ " + decimalFormat.format(saleTemporalList.getSaleCurrent().getIgv()));


        btn_paidSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySale(saleTemporalList.getSaleCurrent().get_id());
            }
        });
        btn_cancelSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSale(saleTemporalList.getSaleCurrent().get_id());
            }
        });
        btn_backSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleTemporalList.setSaleCurrent(null);
                finish();

            }
        });



    }

    public void cancelSale(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<SaleResponse> call = service.cancelSale(id, "Bearer " + config.getJwt());
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(Call<SaleResponse> call, Response<SaleResponse> response) {
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(),"Actualizado",Toast.LENGTH_SHORT).show();

                    btn_backSale.setVisibility(View.VISIBLE);
                    btn_cancelSale.setVisibility(View.GONE);
                    btn_paidSale.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getBaseContext(),"no Actualizado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleResponse> call, Throwable t) {

            }
        });
    }

    public void paySale(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();
        ISaleServices service = retrofit.create(ISaleServices.class);
        Call<SaleResponse> call = service.paidSale(id, "Bearer " + config.getJwt());
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(Call<SaleResponse> call, Response<SaleResponse> response) {
                Log.e("error", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(),"Actualizado",Toast.LENGTH_SHORT).show();
                    btn_backSale.setVisibility(View.VISIBLE);
                    btn_cancelSale.setVisibility(View.GONE);
                    btn_paidSale.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getBaseContext(),"no Actualizado",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaleResponse> call, Throwable t) {

            }
        });

    }


}