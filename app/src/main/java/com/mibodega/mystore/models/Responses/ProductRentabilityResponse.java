package com.mibodega.mystore.models.Responses;

public class ProductRentabilityResponse {
    private String name;
    private String code;
    private double rentability;

    public ProductRentabilityResponse(String name, String code, double rentability) {
        this.name = name;
        this.code = code;
        this.rentability = rentability;
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

    public double getRentability() {
        return rentability;
    }

    public void setRentability(double rentability) {
        this.rentability = rentability;
    }
}
