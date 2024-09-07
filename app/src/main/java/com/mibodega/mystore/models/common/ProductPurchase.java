package com.mibodega.mystore.models.common;

public class ProductPurchase {
    private Number quantity;
    private String code;

    public ProductPurchase(Number quantity, String code) {
        this.quantity = quantity;
        this.code = code;
    }

    public Number getQuantity() {
        return quantity;
    }

    public void setQuantity(Number quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Métodos adicionales para obtener el valor como double o int si es necesario
    public double getQuantityAsDouble() {
        return quantity.doubleValue();
    }

    public int getQuantityAsInt() {
        return quantity.intValue();
    }
}