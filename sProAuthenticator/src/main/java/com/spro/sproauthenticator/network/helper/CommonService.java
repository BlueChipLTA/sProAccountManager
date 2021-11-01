package com.spro.sproauthenticator.network.helper;

import com.spro.sproauthenticator.network.token.LoginRequest;
import com.spro.sproauthenticator.network.token.TokenRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CommonService {
    @GET
    Call<String> getResponse(@Url String url);

    @GET
    Call<String> getResponseWithHeader(@HeaderMap Map<String, String> header, @Url String url);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/v1/auth/otp")
    Call<String> getLogin(@Body LoginRequest data);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/v1/oauth/token")
    Call<String> getToken(@Body TokenRequest data);

}
