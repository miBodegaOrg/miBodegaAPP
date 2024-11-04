package com.mibodega.mystore.models.common;

public class RecomendationMessage {
    private int id;
    private String message;
    private String fecha;

    public RecomendationMessage(int id, String message, String fecha) {
        this.id = id;
        this.message = message;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
