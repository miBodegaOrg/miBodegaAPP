package com.mibodega.mystore.views.dashboards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.SaleCategoryDataDashboardResponse;
import com.mibodega.mystore.models.Responses.SaleTimeDataDashboardResponse;
import com.mibodega.mystore.services.IDashboardServices;
import com.mibodega.mystore.shared.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalesDataActivity extends AppCompatActivity {
    private LineChart lineChart;
    private PieChart pieChart;
    private ArrayList<SaleTimeDataDashboardResponse> arrayListSalesDate = new ArrayList<>();
    private ArrayList<SaleCategoryDataDashboardResponse> arrayListCategories = new ArrayList<>();
    private SalesDataActivity.Period timePeriod;
    private Button btn_day, btn_week, btn_month, btn_year;
    private Config config = new Config();
    private TextView tv_titleChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_data);

        lineChart = findViewById(R.id.lineChart_sales);
        pieChart = findViewById(R.id.pieChart);
        btn_day = findViewById(R.id.buttonDay);
        btn_week = findViewById(R.id.buttonWeek);
        btn_month = findViewById(R.id.buttonMonth);
        btn_year = findViewById(R.id.buttonYear);
        tv_titleChart = findViewById(R.id.Tv_titleChart_sales);

        timePeriod = Period.DAY;
        tv_titleChart.setText("Ultimo Dia");
        loadData("day");
        loadPieData();

        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.DAY;
                loadDataUpdate("day");
                tv_titleChart.setText("Ultimo Dia");
            }
        });

        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.WEEK;
                loadDataUpdate("week");
                tv_titleChart.setText("Ultima Semana");
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.MONTH;
                loadDataUpdate("month");
                tv_titleChart.setText("Ultimo Mes");
            }
        });

        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePeriod = Period.YEAR;
                loadDataUpdate("year");
                tv_titleChart.setText("Ultimo Año");
            }
        });
    }
    public void setupPieChart(ArrayList<SaleCategoryDataDashboardResponse> dataList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Obtener los datos del arreglo
        for (SaleCategoryDataDashboardResponse data : dataList) {
            entries.add(new PieEntry(data.getSales(), data.get_id()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categorías");
        List<Integer> colors = new ArrayList<>();
        for (PieEntry entry : entries) {
            String category = entry.getLabel();
            int color = generateColorFromString(category);
            colors.add(color);
        }
        dataSet.setColors(colors);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new PercentFormatter());

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setCenterText("Venta Categorias");
        pieChart.setCenterTextSize(16f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false); // Ocultar las etiquetas inicialmente
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);

        // Agregar listener de clic
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.setDrawEntryLabels(true); // Mostrar etiquetas
                pieChart.setEntryLabelTextSize(8f); // Aumentar el tamaño del texto de la etiqueta seleccionada
                pieChart.setEntryLabelColor(Color.WHITE); // Cambiar el color de la etiqueta seleccionada
                //dataSet.setDrawValues(false); // Ocultar los valores dentro de las porciones
                int selectedIndex = h.getDataIndex();
                for (int i = 0; i < dataSet.getEntryCount(); i++) {
                    PieEntry entry = dataSet.getEntryForIndex(i);
                    entry.setData(i == selectedIndex); // Mostrar solo el valor de la porción seleccionada
                }
                pieChart.invalidate(); // Actualizar el gráfico
            }

            @Override
            public void onNothingSelected() {
                pieChart.setDrawEntryLabels(false); // Ocultar todas las etiquetas al deseleccionar
                pieChart.setEntryLabelTextSize(8f); // Restablecer el tamaño del texto de las etiquetas
                pieChart.setEntryLabelColor(Color.WHITE); // Restablecer el color de las etiquetas
                dataSet.setDrawValues(true); // Mostrar los valores dentro de las porciones
                pieChart.invalidate(); // Actualizar el gráfico
            }
        });

        pieChart.invalidate();
    }
    //load data
    private void loadInitial() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setTouchEnabled(true); // Mantener habilitado para permitir arrastre
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false); // Deshabilitar el escalado
        lineChart.setPinchZoom(false); // Deshabilitar el zoom con gesto de pinza

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels(timePeriod)));
        xAxis.setTextSize(10f);

        xAxis.setLabelCount(getXAxisLabels(timePeriod).size(), true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        List<Entry> salesData = getSalesData(timePeriod);

        LineDataSet dataSet = new LineDataSet(salesData, "Ventas");
        dataSet.setColor(this.getResources().getColor(R.color.md_theme_light_primary));
        dataSet.setLineWidth(5f);
        dataSet.setCircleColor(this.getResources().getColor(R.color.md_theme_light_secondary));
        dataSet.setCircleRadius(5f);

        dataSet.setDrawValues(true); // Mostrar valores en los puntos de datos
        dataSet.setValueTextSize(12f); // Tamaño del texto de los valores
        dataSet.setValueTextColor(Color.BLACK); // Color del texto de los valores
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0.00f) {
                    return ""; // No mostrar valor si es 0.00
                } else {
                    return String.valueOf((int) value); // Formatea el valor como un entero
                }
            }

        });

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void updateChart() {
        List<Entry> salesData = getSalesData(timePeriod);

        LineDataSet dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
        if (dataSet == null) {
            dataSet = new LineDataSet(salesData, "Ventas");
            dataSet.setColor(Color.DKGRAY);
            dataSet.setLineWidth(5f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setCircleRadius(4f);

            dataSet.setDrawValues(true); // Mostrar valores en los puntos de datos
            dataSet.setValueTextSize(12f); // Tamaño del texto de los valores
            dataSet.setValueTextColor(Color.BLACK); // Color del texto de los valores
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (value == 0.00f) {
                        return ""; // No mostrar valor si es 0.00
                    } else {
                        return String.valueOf((int) value); // Formatea el valor como un entero
                    }
                }
            });
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER); // Establecer el modo de dibujo a HORIZONTAL_BEZIER
            dataSet.setDrawCircles(false); // Deshabilitar los círculos en los puntos de datos


            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
        } else {
            dataSet.setValues(salesData);
            lineChart.getData().notifyDataChanged();
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels(timePeriod)));
        xAxis.setLabelCount(getXAxisLabels(timePeriod).size(), true);
        xAxis.setAvoidFirstLastClipping(true);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private int generateColorFromString(String string) {
        int hash = 0;
        for (int i = 0; i < string.length(); i++) {
            hash = 31 * hash + string.charAt(i);
        }
        return Color.argb(255, Math.abs(hash % 256), Math.abs((hash / 256) % 256), Math.abs((hash / (256 * 256)) % 256));
    }
    //build graphics
    private List<Entry> getSalesData(Period period) {
        List<Entry> entries = new ArrayList<>();

        switch (period) {
            case DAY:
                // Código para manejar la respuesta por día
                Map<Integer, Float> salesByHour = new HashMap<>();

                // Rellenar el mapa con ceros para los valores faltantes
                for (int i = 0; i < 24; i++) {
                    salesByHour.put(i, 0f);
                }

                // Obtener los valores de las ventas desde el arrayList
                for (SaleTimeDataDashboardResponse response : arrayListSalesDate) {
                    String hourString = response.get_id();
                    float sales = response.getSales();

                    try {
                        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date time = hourFormat.parse(hourString);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(time);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        salesByHour.put(hour, sales);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Crear las entradas para el gráfico
                for (Map.Entry<Integer, Float> entry : salesByHour.entrySet()) {
                    int hour = entry.getKey();
                    float sales = entry.getValue();
                    entries.add(new Entry(hour, sales));
                }
                break;
            case WEEK:
                // Código para manejar la respuesta de la semana
                Map<Integer, Float> salesByDayOfWeek = new HashMap<>();

                // Rellenar el mapa con ceros para los valores faltantes
                for (int i = 0; i < 7; i++) {
                    salesByDayOfWeek.put(i, 0f);
                }

                // Obtener los valores de las ventas desde el arrayList
                for (SaleTimeDataDashboardResponse response : arrayListSalesDate) {
                    String dateString = response.get_id();
                    float sales = response.getSales();

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
                        Date date = dateFormat.parse(dateString);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 para domingo, 1 para lunes, etc.
                        salesByDayOfWeek.put(dayOfWeek, sales);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Crear las entradas para el gráfico
                for (Map.Entry<Integer, Float> entry : salesByDayOfWeek.entrySet()) {
                    int dayOfWeek = entry.getKey();
                    float sales = entry.getValue();
                    entries.add(new Entry(dayOfWeek, sales));
                }

                break;
            case MONTH:
                Map<String, Float> salesByDay = new HashMap<>();

                for (SaleTimeDataDashboardResponse response : arrayListSalesDate) {
                    String dayMonth = response.get_id();
                    float sales = (float) response.getSales();
                    salesByDay.put(dayMonth, sales);
                }

                Calendar currentCalendar = Calendar.getInstance();
                int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
                int currentYear = currentCalendar.get(Calendar.YEAR);
                int daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                for (int day = 1; day <= daysInMonth; day++) {
                    String key = String.format("%02d-%02d", currentMonth, day);
                    float sales = salesByDay.getOrDefault(key, 0f);
                    entries.add(new Entry(day - 1, sales));
                }
                break;
            case YEAR:
                Map<String, Float> salesByMonthYear = new HashMap<>();

                Calendar currentYearCalendar = Calendar.getInstance();
                int currentYear2 = currentYearCalendar.get(Calendar.YEAR);


                for (SaleTimeDataDashboardResponse response : arrayListSalesDate) {
                    String monthYear = response.get_id();
                    String[] parts = monthYear.split("-");
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);

                    if (year == currentYear2) {
                        float sales = (float) response.getSales();
                        salesByMonthYear.put(monthYear, sales);
                    }
                }


                for (int month = 1; month <= 12; month++) {
                    String key = currentYear2 + "-" + String.format("%02d", month);

                    System.out.println(key);
                    float sales = salesByMonthYear.getOrDefault(key, 0f);
                    System.out.println((month - 1) + " : " + sales);
                    entries.add(new Entry(month - 1, sales));
                }
                break;
        }

        return entries;
    }
    private enum Period {
        DAY, WEEK, MONTH, YEAR
    }
    private Map<Period, Function<Period, List<String>>> labelGenerators = new EnumMap<>(Period.class);
    {
        labelGenerators.put(Period.DAY, this::getDayLabels);
        labelGenerators.put(Period.WEEK, this::getWeekLabels);
        labelGenerators.put(Period.MONTH, this::getMonthLabels);
        labelGenerators.put(Period.YEAR, this::getYearLabels);
    }
    private ArrayList<String> getXAxisLabels(Period period) {
        return new ArrayList<>(labelGenerators.get(period).apply(period));
    }
    private List<String> getDayLabels(Period period) {
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }
    private List<String> getWeekLabels(Period period) {
        ArrayList<String> labels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++) {
            labels.add(new SimpleDateFormat("EEE", new Locale("es", "ES")).format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        return labels;
    }
    private List<String> getMonthLabels(Period period) {
        ArrayList<String> labels = new ArrayList<>();
        Calendar currentCalendarmonth = Calendar.getInstance();
        int daysInMonth = currentCalendarmonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }
    private List<String> getYearLabels(Period period) {
        return Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic");
    }
    //load api data
    private void loadData(String time) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<SaleTimeDataDashboardResponse>> call = service.getDatByDates(time, "Bearer " + config.getJwt());
        System.out.println(config.getJwt());

        call.enqueue(new Callback<List<SaleTimeDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Response<List<SaleTimeDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        arrayListSalesDate = (ArrayList<SaleTimeDataDashboardResponse>) response.body();
                        loadInitial();
                    }
                    System.out.println("successfull request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror " + t.getMessage());
            }
        });
    }
    private void loadDataUpdate(String time) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<SaleTimeDataDashboardResponse>> call = service.getDatByDates(time, "Bearer " + config.getJwt());
        System.out.println(config.getJwt());

        call.enqueue(new Callback<List<SaleTimeDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Response<List<SaleTimeDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        arrayListSalesDate = (ArrayList<SaleTimeDataDashboardResponse>) response.body();
                        updateChart();
                    }
                    System.out.println("successfull request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SaleTimeDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror " + t.getMessage());
            }
        });
    }
    private void loadPieData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IDashboardServices service = retrofit.create(IDashboardServices.class);
        Call<List<SaleCategoryDataDashboardResponse>> call = service.getDataCategoriesSales("Bearer " + config.getJwt());
        System.out.println(config.getJwt());

        call.enqueue(new Callback<List<SaleCategoryDataDashboardResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SaleCategoryDataDashboardResponse>> call, @NonNull Response<List<SaleCategoryDataDashboardResponse>> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        arrayListCategories = (ArrayList<SaleCategoryDataDashboardResponse>) response.body();
                        setupPieChart(arrayListCategories);
                    }
                    System.out.println("successfull request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SaleCategoryDataDashboardResponse>> call, @NonNull Throwable t) {
                System.out.println("errror " + t.getMessage());
            }
        });
    }

}