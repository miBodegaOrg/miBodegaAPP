package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestSignIn;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SignInResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IProductServices {
    @GET("products")
    Call<List<ProductResponse>> getProducts(@Header("Authorization") String token);
    @POST("products")
    Call<SignInResponse> createProduct(@Body RequestSignIn request,@Header("Authorization") String token );

}
