package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.GenerateCodeResponse;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponseByCode;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IProductServices {

    @GET("products")
    Call<PagesProductResponse> getProducts(@Header("Authorization") String token);
    @Multipart()
    @POST("products")
    Call<ProductResponseByCode> createProduct(@PartMap Map<String, RequestBody> request, @Header("Authorization") String token );

    @GET("products/{code}")
    Call<ProductResponse> getProductByCode(@Path("code") String code, @Header("Authorization") String token);

    @GET("products")
    Call<PagesProductResponse> getProductByName(@Query("search") String name, @Query("limit") int limit, @Header("Authorization") String token);

    @GET("products")
    Call<PagesProductResponse> getProductByCategorySubcategorySearch(
            @Query(value = "search", encoded = true) String name,
            @Query(value = "category", encoded = true) String category,
            @Query(value = "subcategory", encoded = true) String subcategory,
            @Query(value = "limit", encoded = true) int limit,
            @Header("Authorization") String token);

    @POST("products/generate-code")
    Call<GenerateCodeResponse> generateCodeProduct(@Header("Authorization") String token );

    @Multipart()
    @PUT("products/{id}")
    Call<ProductResponseByCode> updateProduct(@Path("id") String id, @PartMap Map<String, RequestBody> request, @Header("Authorization") String token );

    @DELETE("products/{id}")
    Call<ProductResponse> deleteProductById(@Path("id") String id, @Header("Authorization") String token);

}
