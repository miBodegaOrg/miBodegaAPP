package com.mibodega.mystore.models.Responses;

import java.util.ArrayList;

public class ChatResponse {
    private String _id;
    private String shop;
    private String createdAt;
    private String updatedAt;
    private int __v;
    private ArrayList<MessageResponse> messages = new ArrayList<>();
    public ChatResponse(String _id, String shop, String createdAt, String updatedAt, ArrayList<MessageResponse> messages) {
        this._id = _id;
        this.shop = shop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageResponse> messages) {
        this.messages = messages;
    }
}
