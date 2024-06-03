package com.mibodega.mystore.shared;

import com.mibodega.mystore.models.Responses.CategoryResponse;

import java.util.ArrayList;

public class Config {
    private static String jwt;
    private static String userName;
    private static String user_ruc;
    private static String URL_API ="http://34.168.45.44/api/v1/";

    private static ArrayList<CategoryResponse> arrCategories =  new ArrayList<>();

    public ArrayList<CategoryResponse> getArrCategories() {
        return arrCategories;
    }

    public void setArrCategories(ArrayList<CategoryResponse> arrCategories) {
        Config.arrCategories = arrCategories;
    }

    public String getURL_API() {
        return URL_API;
    }

    public void setURL_API(String _URL_API) {
        URL_API = _URL_API;
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
