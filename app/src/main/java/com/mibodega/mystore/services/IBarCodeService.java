package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.GenerateCodeResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IBarCodeService {

    @POST("barcode/create/{code}")
    Call<GenerateCodeResponse> generateCodeProduct(@Header("Authorization") String token );
}
