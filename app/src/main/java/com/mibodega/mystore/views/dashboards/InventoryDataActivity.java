package com.mibodega.mystore.views.dashboards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mibodega.mystore.MainActivity;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.InventoryDataDashboardResponse;
import com.mibodega.mystore.services.ICategoryServices;
import com.mibodega.mystore.services.IDashboardServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.views.chatbot.ChatBotGlobalFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventoryDataActivity extends MainActivity {

    private DrawerLayout drawerLayout;
    private FrameLayout chatFragmentContainer;
    private List<InventoryDataDashboardResponse> productList = new ArrayList<>();
    private Config config = new Config();
    private BarChart barChart;
    private ProgressBar pgb_loadInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        BarChart chart = findViewById(R.id.barChart_inventory);

        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);


        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 500f)); // Ejemplo de dato: 500 ventas en enero
        entries.add(new BarEntry(1f, 750f));
        entries.add(new BarEntry(2f, 1000f));
        entries.add(new BarEntry(3f, 500f));
        entries.add(new BarEntry(4f, 580f));
        entries.add(new BarEntry(5f, 800f));


        BarDataSet dataSet = new BarDataSet(entries, "Ventas mensuales soles");

        dataSet.setColor(Color.GRAY);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        BarData data = new BarData(dataSet);
        chart.setData(data);

        chart.getDescription().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("Ene", "Feb", "Mar","Abr","May","Jun"))); // Etiquetas del eje X
        chart.invalidate();*/

        //setContentView(R.layout.activity_inventory_data);
        setContentLayout(R.layout.activity_inventory_data);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Datos Inventario");
        }
        barChart = findViewById(R.id.barChart_inventory);
        drawerLayout = findViewById(R.id.drawer_layout);
        chatFragmentContainer = findViewById(R.id.chat_fragment_container);
        pgb_loadInventory = findViewById(R.id.Pgb_loadInventory_inventory);
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
        pgb_loadInventory.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);

        loadDataInventory();


    }





    private void loadDataInventory() {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<InventoryDataDashboardResponse>> call = service.getDataProductInventory("Bearer "+config.getJwt());
        call.enqueue(new Callback<List<InventoryDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<InventoryDataDashboardResponse>> call, @NonNull Response<List<InventoryDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    productList = (ArrayList<InventoryDataDashboardResponse>) response.body();
                    if(productList!=null){
                        // Crear las entradas de barras
                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                        ArrayList<String> productNames = new ArrayList<>();

                        for (int i = 0; i < productList.size(); i++) {
                            barEntries.add(new BarEntry(i, (productList.get(i).getStock().floatValue())));
                            productNames.add(productList.get(i).getName());
                        }

                        // Crear el DataSet
                        BarDataSet barDataSet = new BarDataSet(barEntries, "Stock de productos");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextSize(12f);

                        // Crear el objeto BarData y asignarlo al gráfico
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);

                        barChart.setDragEnabled(true); // Habilitar el desplazamiento
                        barChart.setVisibleXRangeMaximum(5); // Limitar las barras visibles
                        barChart.setPinchZoom(false); // Habilitar zoom
                        barChart.setScaleXEnabled(true);
                        barChart.setScaleYEnabled(false);
                        barChart.setPinchZoom(false);
                        barChart.setDoubleTapToZoomEnabled(false);

                        barChart.getXAxis().setDrawGridLines(false);
                        barChart.getAxisLeft().setDrawGridLines(false);
                        barChart.getAxisRight().setDrawGridLines(false);
                        Description description = new Description();
                        description.setText("."); // Cambia el texto a lo que desees
                        barChart.setDescription(description);

                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(productNames));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setGranularityEnabled(true);
                        xAxis.setLabelRotationAngle(-45); // Rotar etiquetas en eje X



                        YAxis yAxisLeft = barChart.getAxisLeft();
                        YAxis yAxisRight = barChart.getAxisRight();
                        yAxisRight.setEnabled(false); // Deshabilitar eje Y derecho

                        barData.setBarWidth(0.3f); // Ajustar ancho de las barras

                        barChart.setExtraOffsets(1, 5, 1, 2); // Ajustar márgenes
                        barChart.setData(barData);
                        barChart.invalidate();
                        pgb_loadInventory.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<InventoryDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }


}