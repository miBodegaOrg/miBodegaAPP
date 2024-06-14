package com.mibodega.mystore.views.dashboards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SaleTimeDataDashboardResponse;
import com.mibodega.mystore.services.IDashboardServices;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalesDataActivity extends AppCompatActivity {

    private LineChart lineChart;
    private ArrayList<SaleTimeDataDashboardResponse> arrayList = new ArrayList<>();
    private SalesDataActivity.Period timePeriod;
    private Button btn_day,btn_week,btn_month,btn_year;
    private Config config = new Config();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_data);

        lineChart = findViewById(R.id.lineChart);
        btn_day = findViewById(R.id.buttonDay);
        btn_week = findViewById(R.id.buttonWeek);
        btn_month = findViewById(R.id.buttonMonth);
        btn_year = findViewById(R.id.buttonYear);

        timePeriod = Period.DAY;
        loadData("day");
        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.DAY;
                loadDataUpdate("day");
            }
        });
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePeriod = Period.WEEK;
                loadDataUpdate("week");

            }
        });
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePeriod = Period.MONTH;
                loadDataUpdate("month");
            }
        });
        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.YEAR;
                loadDataUpdate("year");
                updateChart();
            }
        });

    }


    private List<Entry> getSalesData(Period period) {
        List<Entry> entries = new ArrayList<>();

        switch (period) {
            case DAY:
                    for (int i = 0; i < arrayList.size(); i++) {

                        String hour = arrayList.get(i).get_id();
                        float sales = (float) arrayList.get(i).getSales();
                        // Convertir la hora a un valor numérico (0-23)
                        int hourValue = Integer.parseInt(hour.split(":")[0]);
                        entries.add(new Entry(hourValue, sales));
                    }

                break;
            case WEEK:
                    for (int i = 0; i < arrayList.size(); i++) {
                        String dayMonth = arrayList.get(i).get_id();
                        float sales = (float) arrayList.get(i).getSales();

                        // Obtener el día y el mes del campo "_id"
                        String[] parts = dayMonth.split("-");
                        int month = Integer.parseInt(parts[0]);
                        int day = Integer.parseInt(parts[1]);

                        // Crear un objeto Calendar con el día y el mes
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, month - 1); // Los meses en Calendar van de 0 a 11
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        // Obtener el índice del día de la semana (0-6)
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Los días en Calendar van de 1 a 7
                        entries.add(new Entry(dayOfWeek, sales));
                    }

                break;
            case MONTH:

                break;
            case YEAR:
                    for (int i = 0; i < arrayList.size(); i++) {
                        String monthYear = arrayList.get(i).get_id();
                        float sales = (float) arrayList.get(i).getSales();

                        // Obtener el mes y el año del campo "_id"
                        String[] parts = monthYear.split("-");
                        int year = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);

                        // Crear un objeto Calendar con el mes y el año
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month - 1); // Los meses en Calendar van de 0 a 11

                        // Obtener el índice del mes (0-11)
                        int monthIndex = calendar.get(Calendar.MONTH);

                        entries.add(new Entry(monthIndex, sales));
                    }

                break;
        }

        return entries;
    }

    private void loadInitial(){
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels(timePeriod))); // Inicialmente, mostrar etiquetas para el último día


        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);


        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        List<Entry> salesData = getSalesData(timePeriod); // Inicialmente, obtener datos para el último día

        LineDataSet dataSet = new LineDataSet(salesData, "Ventas");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    private void updateChart() {
        List<Entry> salesData = getSalesData(timePeriod);

        LineDataSet dataSet = new LineDataSet(salesData, "Ventas");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);


        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels(timePeriod)));


        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private enum Period {
        DAY, WEEK, MONTH, YEAR
    }
    private ArrayList<String> getXAxisLabels(Period period) {
        ArrayList<String> labels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        switch (period) {
            case DAY:
                for (int i = 0; i < 24; i++) {
                    labels.add(String.format("%02d:00", i));
                }
                break;
            case WEEK:
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                for (int i = 0; i < 7; i++) {
                    labels.add(new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime()));
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                break;
            case MONTH:
                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= daysInMonth; i++) {
                    labels.add(String.valueOf(i));
                }
                break;
            case YEAR:
                for (int i = 0; i < 12; i++) {
                    calendar.set(currentYear, i, 1);
                    labels.add(new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime()));
                }
                break;
        }

        return labels;
    }

    private void loadData(String time){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<SaleTimeDataDashboardResponse>> call = service.getDatByDates(time,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<SaleTimeDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Response<List<SaleTimeDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    if(response.body()!=null){
                    arrayList = (ArrayList<SaleTimeDataDashboardResponse>) response.body();
                    loadInitial();
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    private void loadDataUpdate(String time){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<SaleTimeDataDashboardResponse>> call = service.getDatByDates(time,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<SaleTimeDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Response<List<SaleTimeDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        arrayList = (ArrayList<SaleTimeDataDashboardResponse>) response.body();
                        updateChart();
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
}