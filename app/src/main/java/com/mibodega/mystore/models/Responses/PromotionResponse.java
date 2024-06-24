package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class PromotionResponse {
    private String _id;
    private String name;
    private String startDate;
    private String endDate;
    private int buy;
    private int pay;
    private boolean active;
    private String shop;
    private String createdAt;
    private String updatedAt;
    private ArrayList<String> products = new ArrayList<>();
    private int __v;

    public PromotionResponse(String _id, String name, String startDate, String endDate, int buy, int pay, boolean active, String shop, String createdAt, String updatedAt, ArrayList<String> products) {
        this._id = _id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.buy = buy;
        this.pay = pay;
        this.active = active;
        this.shop = shop;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
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
