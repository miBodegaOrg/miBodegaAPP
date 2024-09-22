package com.mibodega.mystore.views.sales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.models.common.ProductSale;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterSale;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterSaleDetail;

import java.text.DecimalFormat;

public class DetailSaleActivity extends MainActivity {

    private TextView tv_date,tv_subtotal, tv_descuento,tv_total;
    private RecyclerView rv_saleItemList;
    private RecyclerViewAdapterSaleDetail recyclerViewAdapterSaleDetail;
    private Config config = new Config();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail_sale);
        setContentLayout(R.layout.activity_detail_sale);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalle de Venta");
        }
        tv_date = findViewById(R.id.Tv_date_sale_detail);
        tv_descuento = findViewById(R.id.Tv_descuento_sale_detail);
        tv_subtotal = findViewById(R.id.Tv_subtotal_sale_detail);
        tv_total = findViewById(R.id.Tv_total_sale_detail);
        rv_saleItemList = findViewById(R.id.Rv_itemsale_detail);
        if(config.getSaleResponsecurrent()!=null){
            tv_date.setText(config.getSaleResponsecurrent().getCreatedAt());

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String formatteq = decimalFormat.format(config.getSaleResponsecurrent().getDiscount());
            tv_descuento.setText(formatteq);

            DecimalFormat decimalFormatt = new DecimalFormat("#0.00");
            String formatteeq = decimalFormatt.format(config.getSaleResponsecurrent().getSubtotal());
            tv_subtotal.setText(formatteeq);

            DecimalFormat decimalFormattt = new DecimalFormat("#0.00");
            String formatteeqtr= decimalFormattt.format(config.getSaleResponsecurrent().getTotal());
            tv_total.setText(formatteeqtr);


            recyclerViewAdapterSaleDetail= new RecyclerViewAdapterSaleDetail(getBaseContext(), config.getSaleResponsecurrent().getProducts(), new RecyclerViewAdapterSaleDetail.OnDetailItem() {
                @Override
                public void onClick(ProductSale item) {

                }
            });

            rv_saleItemList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
            rv_saleItemList.setAdapter(recyclerViewAdapterSaleDetail);
        }
    }

}