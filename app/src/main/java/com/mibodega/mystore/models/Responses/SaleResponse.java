package com.mibodega.mystore.models.Responses;

import com.mibodega.mystore.models.common.ProductSale;

import java.util.ArrayList;

public class SaleResponse {

    private ArrayList<ProductSale> products = new ArrayList<>();
    private String status;
    private Double total;
    private Double subtotal;
    private Double igv;
    private Double discount;
    private String shop;
    private String _id;
    private String createdAt;
    private String updatedAt;
    private int __v;


    public SaleResponse(ArrayList<ProductSale> products, String status, Double total, Double subtotal, Double igv, Double discount, String shop, String _id, String _createdAt, String _updatedAt) {
        this.products = products;
        this.status = status;
        this.total = total;
        this.subtotal = subtotal;
        this.igv = igv;
        this.discount = discount;
        this.shop = shop;
        this._id = _id;
        this.createdAt = _createdAt;
        this.updatedAt = _updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public ArrayList<ProductSale> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSale> products) {
        this.products = products;
    }
}
