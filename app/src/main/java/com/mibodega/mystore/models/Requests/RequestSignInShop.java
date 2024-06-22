package com.mibodega.mystore.models.Requests;

public class RequestSignInShop {
    private String ruc;
    private String password;

    public RequestSignInShop(String ruc, String password) {
        this.ruc = ruc;
        this.password = password;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
