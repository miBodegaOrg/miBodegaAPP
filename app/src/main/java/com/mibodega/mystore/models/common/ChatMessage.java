package com.mibodega.mystore.models.common;

public class ChatMessage {
    private int id;
    private String message;
    private String owner;
    private String fecha;

    public ChatMessage(int id, String message, String owner, String fecha) {
        this.id = id;
        this.message = message;
        this.owner = owner;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
