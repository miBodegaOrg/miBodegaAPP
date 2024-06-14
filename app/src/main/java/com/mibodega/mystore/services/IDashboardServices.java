package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.SaleTimeDataDashboardResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IDashboardServices {
    @GET("dashboards/sales")
    Call<List<SaleTimeDataDashboardResponse>> getDatByDates(
            @Query(value = "period", encoded = true) String period,
            @Header("Authorization") String token);

}
