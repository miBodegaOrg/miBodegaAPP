package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.GenerateCodeResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IBarCodeService {

    @POST("barcode/create/{code}")
    Call<ResponseBody> generateCodeProduct(@Path("code") String code,@Header("Authorization") String token );
}
