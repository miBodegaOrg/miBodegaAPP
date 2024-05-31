package com.mibodega.mystore.models.Requests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class ProductCreateRequest {
    private String name;
    private String code;
    private Double price;
    private int stock;
    private ArrayList<String> category;
    private Map<String, RequestBody> imageMap;

    public ProductCreateRequest(String name, String code, Double price, int stock, ArrayList<String> category, RequestBody requestBody,String filename) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.imageMap = new HashMap<>();
        this.imageMap.put("image\"; filename=\"" + filename, requestBody);

    }
}
