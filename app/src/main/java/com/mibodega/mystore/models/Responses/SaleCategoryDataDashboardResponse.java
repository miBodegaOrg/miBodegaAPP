package com.mibodega.mystore.models.Responses;

public class SaleCategoryDataDashboardResponse {
    private String _id;
    private int sales;
    private double percentage;

    public SaleCategoryDataDashboardResponse(String _id, int sales, double percentage) {
        this._id = _id;
        this.sales = sales;
        this.percentage = percentage;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
