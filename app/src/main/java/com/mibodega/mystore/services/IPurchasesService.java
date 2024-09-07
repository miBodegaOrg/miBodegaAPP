package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.PromotionRequest;
import com.mibodega.mystore.models.Requests.PurchaseRequest;
import com.mibodega.mystore.models.Responses.PromotionResponse;
import com.mibodega.mystore.models.Responses.PurchaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPurchasesService {
    @GET("purchases")
    Call<List<PurchaseResponse>> getPurchases(@Header("Authorization") String token);
    @POST("purchases")
    Call<PurchaseResponse> createPurchase(@Body PurchaseRequest request, @Header("Authorization") String token);
    @GET("purchases/{id}")
    Call<PurchaseResponse> getPurchaseById(@Path("id") String id, @Header("Authorization") String token);
    @POST("purchases/received/{id}")
    Call<PurchaseResponse> receivedPurchase(@Path("id") String id, @Header("Authorization") String token);

}
