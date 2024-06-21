package com.mibodega.mystore.models.Responses;

import com.mibodega.mystore.models.common.ProductSupplier;

import java.util.ArrayList;

public class SupplierResponse {
    private String _id;
    private String name;
    private String phone;
    private String ruc;
    private String shop;
    private ArrayList<ProductSupplier> products;
    private String createdAt;
    private String updatedAt;
    private int __v;

    public SupplierResponse(String _id, String name, String phone, String ruc, String shop, ArrayList<ProductSupplier> products, String createdAt, String updatedAt, int __v) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.ruc = ruc;
        this.shop = shop;
        this.products = products;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public ArrayList<ProductSupplier> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSupplier> products) {
        this.products = products;
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
}
