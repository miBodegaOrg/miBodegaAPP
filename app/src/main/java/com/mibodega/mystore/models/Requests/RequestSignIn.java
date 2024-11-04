package com.mibodega.mystore.models.Requests;

public class RequestSignIn {
    private String username;
    private String password;
    private boolean rememberCredentials;

    public RequestSignIn(String username, String password, boolean rememberCredentials) {
        this.username = username;
        this.password = password;
        this.rememberCredentials = rememberCredentials;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRememberCredentials() {
        return rememberCredentials;
    }

    public void setRememberCredentials(boolean rememberCredentials) {
        this.rememberCredentials = rememberCredentials;
    }
}
