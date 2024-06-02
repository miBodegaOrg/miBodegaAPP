package com.mibodega.mystore.models.Responses;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductResponse {
    private String _id;
    private String name;
    private String code;
    private double price;
    private int stock;
    private String image_url;
    private String shop;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private ArrayList<String> category;

    public ProductResponse(String _id, String name, String code, double price, int stock, String image_url, String shop, String createdAt, String updatedAt, ArrayList<String> category) {
        this._id = _id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.stock = stock;
        this.image_url = image_url;
        this.shop = shop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }
}
