package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;

public class RecyclerViewAdapterEmployee extends RecyclerView.Adapter<RecyclerViewAdapterEmployee.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<EmployeeResponse> employeeList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterEmployee.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }
    public interface OnDetailItem {
        void onClick(EmployeeResponse item);
    }
    public RecyclerViewAdapterEmployee(Context context, ArrayList<EmployeeResponse> employeeList, RecyclerViewAdapterEmployee.OnDetailItem onDetailItem) {
        this.context = context;
        this.employeeList = employeeList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<EmployeeResponse> filteredList) {
        this.employeeList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterEmployee.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterEmployee.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterEmployee.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EmployeeResponse item = employeeList.get(position);
        holder.name.setText(item.getName()+" "+item.getLastname());
        holder.btn_manageEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailItem.onClick(item);
            }
        });
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imgEmployee;
        Button btn_manageEmployee;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Tv_employeeName_item);
            btn_manageEmployee = itemView.findViewById(R.id.Btn_manageEmployee_item);
            imgEmployee = itemView.findViewById(R.id.Imgv_employeeImage_item);
        }
    }
}

