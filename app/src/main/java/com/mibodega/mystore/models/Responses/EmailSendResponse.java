package com.mibodega.mystore.models.Responses;

public class EmailSendResponse {
    private String send;

    public EmailSendResponse(String send) {
        this.send = send;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }
}
