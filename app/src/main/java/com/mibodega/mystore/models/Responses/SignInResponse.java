package com.mibodega.mystore.models.Responses;

public class SignInResponse {
    private String _id;
    private String name;
    private String address;
    private String phone;
    private String ruc;
    private String password;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private String token;

    public SignInResponse(String _id, String name, String address, String phone, String ruc, String password, String createdAt, String updatedAt, String token) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.ruc = ruc;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
