package com.mibodega.mystore.models.common;

public class ProductSupplier {
    private String code;
    private Double cost;

    public ProductSupplier(String code, Double cost) {
        this.code = code;
        this.cost = cost;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

}
