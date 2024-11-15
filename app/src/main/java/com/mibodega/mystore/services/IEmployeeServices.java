package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.PromotionRequest;
import com.mibodega.mystore.models.Requests.RequestEmployee;
import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.models.Responses.PermissionResponse;
import com.mibodega.mystore.models.Responses.PromotionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IEmployeeServices {
    @GET("employees")
    Call<List<EmployeeResponse>> getEmployees(@Header("Authorization") String token);
    @POST("employees")
    Call<EmployeeResponse> createEmployee(@Body RequestEmployee request, @Header("Authorization") String token);
    @PUT("employees/{id}")
    Call<EmployeeResponse> updateEmployeeById(@Path("id") String id,@Body RequestEmployee request, @Header("Authorization") String token);
    @GET("employees/{id}")
    Call<EmployeeResponse> getEmployeeById(@Path("id") String id, @Header("Authorization") String token);
    @DELETE("employees/{id}")
    Call<EmployeeResponse> deleteEmployeeById(@Path("id") String id, @Header("Authorization") String token);
    @GET("employees/permissions")
    Call<PermissionResponse> getPermises(@Header("Authorization") String token);

}
