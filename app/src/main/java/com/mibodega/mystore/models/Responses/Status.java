package com.mibodega.mystore.models.Responses;

public class Status {
    private String msg;

    public Status(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
