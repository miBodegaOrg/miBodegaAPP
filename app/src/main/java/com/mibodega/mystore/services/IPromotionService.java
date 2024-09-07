package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.DiscountRequest;
import com.mibodega.mystore.models.Requests.PromotionRequest;
import com.mibodega.mystore.models.Responses.DiscountResponse;
import com.mibodega.mystore.models.Responses.PromotionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPromotionService {
    @GET("promotions")
    Call<List<PromotionResponse>> getPromotion(@Header("Authorization") String token);
    @POST("promotions")
    Call<PromotionResponse> createPromotion(@Body PromotionRequest request, @Header("Authorization") String token);
    @GET("discounts/{id}")
    Call<PromotionResponse> getPromotionById(@Path("id") String id, @Header("Authorization") String token);

}
