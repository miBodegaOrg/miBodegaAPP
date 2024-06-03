package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.PagesProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ICategoryServices {
    @GET("categories")
    Call<List<CategoryResponse>> getCategories(@Header("Authorization") String token);
}
