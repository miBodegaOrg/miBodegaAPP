package com.mibodega.mystore.shared.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.mibodega.mystore.R;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryView extends LinearLayout {
    private TextView textViewTitle;
    private ImageButton imageButtonExpand;
    private MaterialCardView mv_card;
    private LinearLayout linearLayoutSubcategories;

    private List<String> selectedSubcategories = new ArrayList<>();

    public SubcategoryView(Context context) {
        super(context);
        init(context);
    }

    public SubcategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubcategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.subcategory_view_product, this, true);

        textViewTitle = findViewById(R.id.textViewTitle);
        imageButtonExpand = findViewById(R.id.imageButtonExpand);
        linearLayoutSubcategories = findViewById(R.id.linearLayoutSubcategories);
        mv_card = findViewById(R.id.mv_subcategories_item);

        mv_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSubcategories();
            }
        });
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }
    public String getTitle() {
        return textViewTitle.getText().toString();
    }

    public void addSubcategory(String subcategory) {
        CheckBox checkBoxSubcategory = new CheckBox(getContext());
        checkBoxSubcategory.setText(subcategory);
        checkBoxSubcategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedSubcategories.add(subcategory);
                } else {
                    selectedSubcategories.remove(subcategory);
                }
            }
        });
        linearLayoutSubcategories.addView(checkBoxSubcategory);
    }

    public List<String> getSelectedSubcategories() {
        return selectedSubcategories;
    }


    private void toggleSubcategories() {
        if (linearLayoutSubcategories.getVisibility() == VISIBLE) {
            linearLayoutSubcategories.setVisibility(GONE);
            imageButtonExpand.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
        } else {
            linearLayoutSubcategories.setVisibility(VISIBLE);
            imageButtonExpand.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
        }
    }
}
