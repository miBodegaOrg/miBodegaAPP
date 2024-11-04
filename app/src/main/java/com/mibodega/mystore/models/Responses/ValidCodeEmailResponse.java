package com.mibodega.mystore.models.Responses;

public class ValidCodeEmailResponse {
    private String valid;

    public ValidCodeEmailResponse(String valid) {
        this.valid = valid;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
