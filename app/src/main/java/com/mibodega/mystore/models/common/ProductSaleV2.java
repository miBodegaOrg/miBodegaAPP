package com.mibodega.mystore.models.common;

public class ProductSaleV2 {
    private String code;
    private int quantity;

    public ProductSaleV2(String code, int quantity) {
        this.code = code;
        this.quantity = quantity;
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

}
