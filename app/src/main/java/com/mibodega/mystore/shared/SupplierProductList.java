package com.mibodega.mystore.shared;

import android.widget.Toast;

import com.mibodega.mystore.models.Responses.ProductResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupplierProductList {
    private static ArrayList<ProductResponse> arrayList = new ArrayList<>();

    public SupplierProductList() {
    }

    public ArrayList<ProductResponse> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ProductResponse> arrayList) {
        SupplierProductList.arrayList = arrayList;
    }

    public void addProduct(ProductResponse product) {
        if (!arrayList.contains(product)) {
            arrayList.add(product);
        } else {
        }
    }

    // Función para eliminar un producto de la lista
    public void removeProduct(ProductResponse product) {
        arrayList.remove(product);
    }

    public void cleanAll() {
        arrayList.clear();
    }
}
