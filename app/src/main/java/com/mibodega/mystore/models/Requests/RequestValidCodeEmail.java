package com.mibodega.mystore.models.Requests;

public class RequestValidCodeEmail {
    private String code;
    private String email;

    public RequestValidCodeEmail(String code, String email) {
        this.code = code;
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
