package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class EmployeeResponse {
    private String _id;
    private String name;
    private String lastname;
    private String email;
    private String dni;
    private String phone;
    private String password;
    private String shop;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private ArrayList<String> permissions = new ArrayList<>();

    public EmployeeResponse(String _id, String name, String lastname, String email, String dni, String phone, String password, String shop, String createdAt, String updatedAt, ArrayList<String> permissions) {
        this._id = _id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.password = password;
        this.shop = shop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.permissions = permissions;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
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

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
