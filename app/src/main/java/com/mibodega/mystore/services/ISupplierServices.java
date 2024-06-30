package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestSupplier;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.SupplierResponse;
import com.mibodega.mystore.models.Responses.SupplierResponseV2;

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
    Call<List<SupplierResponseV2>> getSuppliers(@Header("Authorization") String token);
    @POST("suppliers")
    Call<SupplierResponseV2> createSupplier(@Body RequestSupplier requestSupplier, @Header("Authorization") String token);
    @GET("suppliers/{ruc}")
    Call<SupplierResponseV2> getSuppliersById(@Path("ruc") String ruc,@Header("Authorization") String token);
    @PUT("suppliers/{ruc}")
    Call<SupplierResponseV2> updateSuppliersById(@Path("ruc") String ruc,@Body RequestSupplier requestSupplier,@Header("Authorization") String token);
    @DELETE("suppliers/{ruc}")
    Call<SupplierResponseV2> deleteSupplier(@Path("ruc") String ruc,@Header("Authorization") String token);
}
