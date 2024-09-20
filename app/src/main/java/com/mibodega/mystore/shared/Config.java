package com.mibodega.mystore.shared;

import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.CategoryResponseWithProducts;
import com.mibodega.mystore.models.Responses.SignInResponse;

import java.util.ArrayList;

public class Config {
    private static String jwt;
    private static String userName;
    private static String user_ruc;
    private static String URL_API ="http://35.247.30.135/api/v1/";
    private static ArrayList<CategoryResponse> arrCategories =  new ArrayList<>();
    private static ArrayList<String> arrPermises = new ArrayList<>();
    private static ArrayList<CategoryResponseWithProducts> arrCategoriesWithProducts =  new ArrayList<>();
    private static SignInResponse userData;

    public SignInResponse getUserData() {
        return userData;
    }

    public void setUserData(SignInResponse userData) {
        Config.userData = userData;
    }

    public ArrayList<String> getArrPermises() {
        return arrPermises;
    }

    public void setArrPermises(ArrayList<String> arrPermises) {
        Config.arrPermises = arrPermises;
    }

    public ArrayList<CategoryResponseWithProducts> getArrCategoriesWithProducts() {
        return arrCategoriesWithProducts;
    }

    public void setArrCategoriesWithProducts(ArrayList<CategoryResponseWithProducts> arrCategoriesWithProducts) {
        Config.arrCategoriesWithProducts = arrCategoriesWithProducts;
    }


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
