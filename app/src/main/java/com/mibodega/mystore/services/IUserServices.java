package com.mibodega.mystore.services;

import com.mibodega.mystore.models.Requests.RequestEmailCode;
import com.mibodega.mystore.models.Requests.RequestSetNewPassword;
import com.mibodega.mystore.models.Requests.RequestSignIn;
import com.mibodega.mystore.models.Requests.RequestSignInShop;
import com.mibodega.mystore.models.Requests.RequestSignUp;
import com.mibodega.mystore.models.Requests.RequestUpdatePassword;
import com.mibodega.mystore.models.Requests.RequestUpdateProfile;
import com.mibodega.mystore.models.Requests.RequestValidCodeEmail;
import com.mibodega.mystore.models.Responses.EmailSendResponse;
import com.mibodega.mystore.models.Responses.SetNewPasswordResponse;
import com.mibodega.mystore.models.Responses.SignInResponse;
import com.mibodega.mystore.models.Responses.SignInResponseToken;
import com.mibodega.mystore.models.Responses.SignUpResponse;
import com.mibodega.mystore.models.Responses.Status;
import com.mibodega.mystore.models.Responses.UpdateProfileResponse;
import com.mibodega.mystore.models.Responses.ValidCodeEmailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IUserServices {
    @POST("auth/signin")
    Call<SignInResponse> post_signin(@Body RequestSignIn request);
    @POST("auth/signup")
    Call<SignUpResponse> post_signup(@Body RequestSignUp request);
    @POST("auth/remember-credentials")
    Call<SignInResponseToken> post_signInToken(@Header("Authorization") String token);
    @PUT("auth/update-profile")
    Call<UpdateProfileResponse> put_updateProfile(@Body RequestUpdateProfile request, @Header("Authorization") String token);

    @PUT("auth/change-password")
    Call<Status> put_updatePassword(@Body RequestUpdatePassword request, @Header("Authorization") String token);

    @POST("auth/send-change-password-email")
    Call<EmailSendResponse> sendEmailCode(@Body RequestEmailCode request);
    @POST("auth/valid-code")
    Call<ValidCodeEmailResponse> validateCode(@Body RequestValidCodeEmail request);
    @POST("auth/change-password-email")
    Call<SetNewPasswordResponse> changePasswordEmail(@Body RequestSetNewPassword request);

}
