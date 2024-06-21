package com.mibodega.mystore.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mibodega.mystore.R;
import com.mibodega.mystore.views.dashboards.SalesDataActivity;


public class DashboardsFragment extends Fragment {

    private CardView Cv_SaleOption_dashboard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboards, container, false);
        Cv_SaleOption_dashboard = root.findViewById(R.id.Cv_SaleOption_dashboard);
        Cv_SaleOption_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg  = new Intent(getContext(), SalesDataActivity.class);
                startActivity(mg);
            }
        });

        return root;
    }
}