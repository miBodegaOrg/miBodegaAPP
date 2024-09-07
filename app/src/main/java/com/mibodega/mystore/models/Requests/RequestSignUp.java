package com.mibodega.mystore.models.Requests;

public class RequestSignUp {
    private String name;
    private String address;
    private String phone;
    private String ruc;
    private String password;

    public RequestSignUp(String name, String address, String phone, String ruc, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.ruc = ruc;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
