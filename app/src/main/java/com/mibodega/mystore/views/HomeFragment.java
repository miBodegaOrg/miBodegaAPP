package com.mibodega.mystore.views;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.mibodega.mystore.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        BarChart chart = root.findViewById(R.id.chart);

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


        chart.invalidate();
        return root;
    }
}