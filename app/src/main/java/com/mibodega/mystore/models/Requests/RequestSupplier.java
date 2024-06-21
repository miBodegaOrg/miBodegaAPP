package com.mibodega.mystore.models.Requests;

import com.mibodega.mystore.models.common.ProductSupplier;

import java.util.ArrayList;

public class RequestSupplier {
    private String name;
    private String phone;
    private String ruc;
    private ArrayList<ProductSupplier> products;

    public RequestSupplier(String name, String phone, String ruc, ArrayList<ProductSupplier> products) {
        this.name = name;
        this.phone = phone;
        this.ruc = ruc;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public ArrayList<ProductSupplier> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSupplier> products) {
        this.products = products;
    }
}
