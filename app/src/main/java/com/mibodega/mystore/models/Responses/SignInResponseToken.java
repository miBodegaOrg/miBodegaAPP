package com.mibodega.mystore.models.Responses;

public class SignInResponseToken {
    private String name;
    private String username;
    private String phone;
    private String type;

    public SignInResponseToken(String name, String username, String phone, String type) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
