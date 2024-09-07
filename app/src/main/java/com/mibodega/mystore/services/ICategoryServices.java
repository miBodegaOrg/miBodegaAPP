package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestCategory;
import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.CategoryResponseWithProducts;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ICategoryServices {
    @POST("categories")
    Call<CategoryResponse> createCategories(@Body RequestCategory request, @Header("Authorization") String token);

    @GET("categories")
    Call<List<CategoryResponse>> getCategories(@Header("Authorization") String token);
    @GET("categories/with-products")
    Call<List<CategoryResponseWithProducts>> getCategoriesWithProducts(@Header("Authorization") String token);
}
