package com.mibodega.mystore.models.Requests;

import java.util.ArrayList;

public class PromotionRequest {
    private String name;
    private String startDate;
    private String endDate;
    private int buy;
    private int pay;
    private boolean active;
    private ArrayList<String> products = new ArrayList<>();

    public PromotionRequest(String name, String startDate, String endDate, int buy, int pay, boolean active, ArrayList<String> products) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.buy = buy;
        this.pay = pay;
        this.active = active;
        this.products = products;
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

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }
}
