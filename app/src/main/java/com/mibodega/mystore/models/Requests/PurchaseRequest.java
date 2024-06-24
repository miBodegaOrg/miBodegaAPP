package com.mibodega.mystore.models.Requests;

import com.mibodega.mystore.models.common.ProductSaleV2;

import java.util.ArrayList;

public class PurchaseRequest {
    private String ruc;
    private double discount;
    private double shipping;
    private ArrayList<ProductSaleV2> products = new ArrayList<>();

    public PurchaseRequest(String ruc, double discount, double shipping, ArrayList<ProductSaleV2> products) {
        this.ruc = ruc;
        this.discount = discount;
        this.shipping = shipping;
        this.products = products;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public ArrayList<ProductSaleV2> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSaleV2> products) {
        this.products = products;
    }
}
