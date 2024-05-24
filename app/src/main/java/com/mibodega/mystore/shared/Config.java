package com.mibodega.mystore.shared;

public class Config {
    private String jwt;
    private String userName;
    private String user_ruc;
    private String URL_API ="http://104.196.251.51/api/v1/";

    public String getURL_API() {
        return this.URL_API;
    }

    public void setURL_API(String URL_API) {
        this.URL_API = URL_API;
    }



    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_ruc() {
        return user_ruc;
    }

    public void setUser_ruc(String user_ruc) {
        this.user_ruc = user_ruc;
    }
}
