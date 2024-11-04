package com.mibodega.mystore.models.Responses;

public class MessageResponseGpt {
    private String response;

    public MessageResponseGpt(String response) {
        this.response = response;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
