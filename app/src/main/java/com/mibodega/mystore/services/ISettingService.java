package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Responses.EmployeeResponse;
import com.mibodega.mystore.models.Responses.Status;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ISettingService {
    @DELETE("settings/delete-all")
    Call<ResponseBody> deleteAllDataShop(@Header("Authorization") String token);
}
