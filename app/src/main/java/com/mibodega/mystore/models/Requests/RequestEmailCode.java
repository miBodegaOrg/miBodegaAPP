package com.mibodega.mystore.models.Requests;

public class RequestEmailCode {
    private String email;

    public RequestEmailCode(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
