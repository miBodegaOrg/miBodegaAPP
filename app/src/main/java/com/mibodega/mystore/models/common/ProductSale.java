package com.mibodega.mystore.models.common;

public class ProductSale {
    private String code;
    private int quantity;
    private String name;
    private Double price;

    public ProductSale(String code, int quatity, String name, Double price) {
        this.code = code;
        this.quantity = quatity;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}