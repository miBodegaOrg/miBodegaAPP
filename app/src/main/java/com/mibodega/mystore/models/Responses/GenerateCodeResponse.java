package com.mibodega.mystore.models.Responses;

public class GenerateCodeResponse {
    private String code;

    public GenerateCodeResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
