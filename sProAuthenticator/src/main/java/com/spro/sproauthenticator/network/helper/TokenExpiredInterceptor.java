package com.spro.sproauthenticator.network.helper;


import com.spro.sproauthenticator.network.token.LoginRequest;
import com.spro.sproauthenticator.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenExpiredInterceptor implements Interceptor {
    private static final String AUTHORIZATION = "Authorization";
    private String TAG = getClass().getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        request = builder.build();
        Response response = chain.proceed(request);

        if (response.code() == 403) {
            Logger.d("1407." + TAG, "TokenExpiredInterceptor: Ma loi 403 - refresh token");
            refreshToken(builder);
            }
        if (response.code() == 500) {
            Logger.d("1407." + TAG, "Token is invalid: Ma loi 500 - refresh token");
            refreshToken(builder);
        }
        if (response.code() == 400) {
            Logger.d("1407." + TAG, "BadRequest: Ma loi 400 - refresh token");
            refreshToken(builder);
        }
        if (response.code() == 401) {
            Logger.d("1407." + TAG, "BadRequest: Ma loi 401 - refresh token");
            refreshToken(builder);
        }
        return chain.proceed(request);
    }

    private void refreshToken(final Request.Builder builder) {
        LoginRequest request = new LoginRequest();
        Logger.d("0909." + TAG, "refreshToken:");

        //request.setIMEI(DeviceSystemInfo.getRawMACEthernet());
        //request.setMacWifi(DeviceSystemInfo.getRawMACWifi());
        //request.setSerialNumber(DeviceSystemInfo.getRawSerialNumber());
/*
        APIServiceHelper.getNewToken(request, TokenResponse.class, new APIServiceHelper.IResponseCallback<TokenResponse>() {
            @Override
            public void onPrepare() {
            }

            @Override
            public void onResponse(TokenResponse response) {
                if (response != null && response.getData() != null) {
                    //PreferenceManager.getInstance(WarrantyApplication.getContext()).setKeyToken(response.getData().getToken());
                    //setAuthHeader(builder, PreferenceManager.getInstance(WarrantyApplication.getContext()).getToken());
                    //EventBus.getDefault().postSticky(new TokenExpiredEvent());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

 */
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) {
            builder.header(AUTHORIZATION, token);
        }
    }
}
