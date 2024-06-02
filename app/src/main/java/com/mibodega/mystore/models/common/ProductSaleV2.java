package com.mibodega.mystore.models.common;

public class ProductSaleV2 {
    private String code;
    private int quantity;
    private String name;

    public ProductSaleV2(String code, int quantity, String name) {
        this.code = code;
        this.quantity = quantity;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
