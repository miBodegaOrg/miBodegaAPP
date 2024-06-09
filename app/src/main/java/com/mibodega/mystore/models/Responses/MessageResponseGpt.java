package com.mibodega.mystore.models.Responses;

public class MessageResponseGpt {
    private String _id;
    private String response;

    public MessageResponseGpt(String _id, String response) {
        this._id = _id;
        this.response = response;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
