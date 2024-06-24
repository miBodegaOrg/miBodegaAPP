package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.PagesSaleResponse;
import com.mibodega.mystore.models.Responses.SaleResponse;
import com.mibodega.mystore.models.Responses.Status;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ISaleServices {
    @POST("sales")
    Call<SaleResponse> createSales(@Body RequestCreateSale requestCreateSale, @Header("Authorization") String token);

    @GET("sales")
    Call<PagesSaleResponse> getSales(@Header("Authorization") String token);

    @POST("sales/paid/{id}")
    Call<SaleResponse> paidSale(@Path("id") String id, @Header("Authorization") String token);
    @POST("sales/cancel/{id}")
    Call<SaleResponse> cancelSale(@Path("id") String id, @Header("Authorization") String token);

    @GET("sales/{id}")
    Call<SaleResponse> getSalesById(@Path("id") String id,@Header("Authorization") String token);


}
