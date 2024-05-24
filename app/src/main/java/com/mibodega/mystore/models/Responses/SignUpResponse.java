package com.mibodega.mystore.models.Responses;

public class SignUpResponse {
    private String token;

    public SignUpResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
