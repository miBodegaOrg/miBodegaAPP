package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.DiscountRequest;
import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.DiscountResponse;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IDiscountsService {
    @GET("discounts")
    Call<List<DiscountResponse>> getDiscounts(@Header("Authorization") String token);
    @POST("discounts")
    Call<DiscountResponse> createDiscount(@Body DiscountRequest request, @Header("Authorization") String token);
    @GET("discounts/{id}")
    Call<DiscountResponse> getDiscountsById(@Path("id") String id, @Header("Authorization") String token);

}
