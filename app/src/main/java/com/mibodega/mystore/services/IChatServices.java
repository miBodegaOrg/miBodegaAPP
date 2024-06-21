package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestCreateSale;
import com.mibodega.mystore.models.Requests.RequestMessage;
import com.mibodega.mystore.models.Responses.CategoryResponse;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.GenerateCodeResponse;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IChatServices {
    @GET("chats")
    Call<List<ChatResponse>> getChats(@Header("Authorization") String token);
    @GET("chats/{id}")
    Call<ChatResponse> getChatsById(@Path("id") String id,@Header("Authorization") String token);
    @POST("chats")
    Call<MessageResponseGpt> createContextCreateChat(@Body RequestMessage request, @Header("Authorization") String token );
    @POST("chats/response/{id}")
    Call<MessageResponseGpt> requestQuestionGPT(@Path("id") String id, @Body RequestMessage request, @Header("Authorization") String token );


}
