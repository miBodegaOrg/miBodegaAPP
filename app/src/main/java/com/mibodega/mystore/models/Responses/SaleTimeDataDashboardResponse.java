package com.mibodega.mystore.models.Responses;

public class SaleTimeDataDashboardResponse {
    private String _id;
    private int sales;

    public SaleTimeDataDashboardResponse(String _id, int sales) {
        this._id = _id;
        this.sales = sales;
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
}
