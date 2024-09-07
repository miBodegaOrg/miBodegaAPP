package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class DiscountResponse {
    private String _id;
    private String name;
    private double percentage;
    private int value;
    private boolean active;
    private String shop;
    private ArrayList<String> products = new ArrayList<>();
    private int __v;

    public DiscountResponse(String _id, String name, double percentage, int value, boolean active, String shop, ArrayList<String> products) {
        this._id = _id;
        this.name = name;
        this.percentage = percentage;
        this.value = value;
        this.active = active;
        this.shop = shop;
        this.products = products;
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
