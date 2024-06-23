package com.mibodega.mystore.models.Requests;

import java.util.ArrayList;

public class DiscountRequest {
    private String name;
    private String startDate;
    private String endDate;
    private int percentage;
    private boolean active;
    private ArrayList<String> products = new ArrayList<>();

    public DiscountRequest(String name, String startDate, String endDate, int percentage, boolean active, ArrayList<String> products) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
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
