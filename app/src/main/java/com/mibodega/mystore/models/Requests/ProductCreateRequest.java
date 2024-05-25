package com.mibodega.mystore.models.Requests;

import java.util.ArrayList;

import okhttp3.MultipartBody;

public class ProductCreateRequest {
    private String name;
    private String code;
    private Double price;
    private int stock;
    private ArrayList<String> category = new ArrayList<>();
    private MultipartBody.Part imagePart;

    public ProductCreateRequest(String name, String code, Double price, int stock, ArrayList<String> category, MultipartBody.Part imagePart) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.imagePart = imagePart;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public MultipartBody.Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(MultipartBody.Part imagePart) {
        this.imagePart = imagePart;
    }
}
