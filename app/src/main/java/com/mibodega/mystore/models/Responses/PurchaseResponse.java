package com.mibodega.mystore.models.Responses;

import com.mibodega.mystore.models.common.ProductSaleV2;

import java.util.ArrayList;

public class PurchaseResponse {
    private String _id;
    private String status;
    private double total;
    private double subtotal;
    private double discount;
    private String shop;
    private String supplier;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private ArrayList<ProductSaleV2> products = new ArrayList<>();

    public PurchaseResponse(String _id, String status, double total, double subtotal, double discount, String shop, String supplier, String createdAt, String updatedAt, ArrayList<ProductSaleV2> products) {
        this._id = _id;
        this.status = status;
        this.total = total;
        this.subtotal = subtotal;
        this.discount = discount;
        this.shop = shop;
        this.supplier = supplier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.products = products;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<ProductSaleV2> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSaleV2> products) {
        this.products = products;
    }
}
