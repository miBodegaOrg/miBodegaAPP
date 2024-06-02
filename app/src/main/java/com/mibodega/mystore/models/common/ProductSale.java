package com.mibodega.mystore.models.common;

public class ProductSale {
    private String code;
    private int quatity;
    private String name;
    private Double price;

    public ProductSale(String code, int quatity, String name, Double price) {
        this.code = code;
        this.quatity = quatity;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
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
