package com.mibodega.mystore.models.Requests;

import com.mibodega.mystore.models.common.ProductSale;

import java.util.ArrayList;

public class RequestCreateSale {
    private ArrayList<ProductSale> products = new ArrayList<>();

    public RequestCreateSale(ArrayList<ProductSale> products) {
        this.products = products;
    }

    public ArrayList<ProductSale> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSale> products) {
        this.products = products;
    }
}
