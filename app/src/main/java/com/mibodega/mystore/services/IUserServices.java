package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestSignInShop;
import com.mibodega.mystore.models.Requests.RequestSignUp;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.models.Responses.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserServices {
    @POST("auth/shop/signin")
    Call<SignInResponse> post_signin_shop(@Body RequestSignInShop request);
    @POST("auth/employee/signin")
    Call<SignInResponse> post_signin_employee(@Body RequestSignInShop request);

    @POST("auth/shop/signup")
    Call<SignUpResponse> post_signup(@Body RequestSignUp request);

}
