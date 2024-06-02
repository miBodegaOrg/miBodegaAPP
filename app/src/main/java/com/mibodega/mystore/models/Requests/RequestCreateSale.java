package com.mibodega.mystore.models.Requests;

import com.mibodega.mystore.models.common.ProductSale;
import com.mibodega.mystore.models.common.ProductSaleV2;

import java.util.ArrayList;

public class RequestCreateSale {
    private ArrayList<ProductSaleV2> products = new ArrayList<>();

    public RequestCreateSale(ArrayList<ProductSaleV2> products) {
        this.products = products;
    }

    public ArrayList<ProductSaleV2> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSaleV2> products) {
        this.products = products;
    }
}
