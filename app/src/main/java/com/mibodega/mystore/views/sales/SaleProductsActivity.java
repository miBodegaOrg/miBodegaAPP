package com.mibodega.mystore.views.sales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mibodega.mystore.R;
import com.mibodega.mystore.shared.adapters.ViewPagerAdapterSale;

public class SaleProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_products);
        ViewPager2 viewPager = findViewById(R.id.Vp_pagerSaleView_sale);
        ViewPagerAdapterSale adapter = new ViewPagerAdapterSale(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new CreateSaleFragment(), "Crear");
        adapter.addFragment(new SalesListFragment(), "Historial");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout_sale);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getPageTitle(position))
        ).attach();

    }
}