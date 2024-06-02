package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Responses.Status;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ISaleServices {
    @POST("sales")
    Call<Status> createSales(@Body RequestCreateSale requestCreateSale, @Header("Authorization") String token);

}
