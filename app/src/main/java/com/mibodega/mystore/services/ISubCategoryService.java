package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestCategory;
import com.mibodega.mystore.models.Requests.RequestSubCategory;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.SubCategoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISubCategoryService {
    @POST("subcategories")
    Call<SubCategoryResponse> createSubCategories(@Body RequestSubCategory request, @Header("Authorization") String token);

}
