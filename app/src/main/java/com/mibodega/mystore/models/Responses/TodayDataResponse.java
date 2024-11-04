package com.mibodega.mystore.models.Responses;

public class TodayDataResponse {
    private Number sales;
    private Number total;

    public TodayDataResponse(Number sales, Number total) {
        this.sales = sales;
        this.total = total;
    }

    public Number getSales() {
        return sales;
    }

    public void setSales(Number sales) {
        this.sales = sales;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
