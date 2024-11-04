package com.mibodega.mystore.models.Responses;

public class InventoryDataDashboardResponse {
    private String name;
    private String code;
    private Number stock;

    public InventoryDataDashboardResponse(String name, String code, Number stock) {
        this.name = name;
        this.code = code;
        this.stock = stock;
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

    public Number getStock() {
        return stock;
    }

    public void setStock(Number stock) {
        this.stock = stock;
    }
}
