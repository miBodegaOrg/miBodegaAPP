package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.ProductCreateRequest;
import com.mibodega.mystore.models.Requests.RequestSignIn;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.shared.Config;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface IProductServices {

    @GET("products")
    Call<PagesProductResponse> getProducts(@Header("Authorization") String token);
    @Multipart()
    @POST("products")
    Call<ProductResponse> createProduct(@PartMap Map<String, RequestBody> request, @Header("Authorization") String token );

}
