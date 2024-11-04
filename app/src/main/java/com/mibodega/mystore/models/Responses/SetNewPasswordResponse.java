package com.mibodega.mystore.models.Responses;

public class SetNewPasswordResponse {
    private String msg;

    public SetNewPasswordResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
