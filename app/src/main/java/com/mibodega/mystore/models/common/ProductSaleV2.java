package com.mibodega.mystore.models.common;

public class ProductSaleV2 {

    private String code;
    private Number quantity;

    public ProductSaleV2(String code, Number quantity) {
        this.code = code;
        this.quantity = quantity;
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
