package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestSupplier;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ISupplierServices {
    @GET("suppliers")
    Call<List<SupplierResponse>> getSuppliers(@Header("Authorization") String token);
    @POST("suppliers")
    Call<SupplierResponse> createSupplier(@Body RequestSupplier requestSupplier, @Header("Authorization") String token);
    @GET("suppliers/{ruc}")
    Call<SupplierResponse> getSuppliersById(@Path("ruc") String ruc,@Header("Authorization") String token);
    @PUT("suppliers/{ruc}")
    Call<SupplierResponse> updateSuppliersById(@Body RequestSupplier requestSupplier,@Path("ruc") String code,@Header("Authorization") String token);
    @DELETE("suppliers/{ruc}")
    Call<SupplierResponse> deleteSupplier(@Path("ruc") String ruc,@Header("Authorization") String token);
}
