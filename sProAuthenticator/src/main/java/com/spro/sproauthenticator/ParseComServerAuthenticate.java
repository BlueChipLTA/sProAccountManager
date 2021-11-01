package com.spro.sproauthenticator;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.spro.sproauthenticator.activity.AuthenticatorActivity;
import com.spro.sproauthenticator.activity.VerifyOtpActivity;
import com.spro.sproauthenticator.network.helper.APIServiceHelper;
import com.spro.sproauthenticator.network.token.LoginRequest;
import com.spro.sproauthenticator.network.token.LoginResponse;
import com.spro.sproauthenticator.utils.CommonUtils;
import com.spro.sproauthenticator.utils.Logger;
import com.spro.sproauthenticator.utils.MyToast;

public class ParseComServerAuthenticate implements ServerAuthenticate{
    public final String TAG = "ttcn88.ParseComServerAuthenticate";
    private final int REQ_SMSCODE = 1;
    @Override
    public String userSignIn(final Activity mActivity, final String userName, final String userPass, final String accountType) throws Exception {
        Logger.d(TAG, "userSignIn");

        LoginRequest request = new LoginRequest();
        request.setUsername(userName);
        request.setPassword(userPass);
//        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        APIServiceHelper.requestLogin(request, LoginResponse.class, new APIServiceHelper.IResponseCallback<LoginResponse>() {
            @Override
            public void onPrepare() {
                Logger.d(TAG, "fetchRequestLoginInfo - onPrepare()");
            }

            @Override
            public void onResponse(LoginResponse response) {
                if (response != null) {
                    Log.d(TAG, "getSuccess: " + response.getSuccess());
                    if (response.getSuccess()) {
                        //Request failed
                        //show log
                        if(response.getCode()==429){
                            MyToast.showLongErrorMessage(mActivity, "Mã OTP đã được gửi đến điện thoại, vui lòng kiểm tra và nhập mã OTP");
                        }
                        Intent intent = new Intent(mActivity, VerifyOtpActivity.class);
                        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                        intent.putExtra(CommonUtils.KEY_ACCOUNT_PASS, userPass);
                        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                        mActivity.startActivityForResult(intent, REQ_SMSCODE);
                    }else{
                        MyToast.showLongErrorMessage(mActivity, response.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.d(TAG, "fetchRequestLoginInfo - onFailure: " + t.toString());
                MyToast.showLongErrorMessage(mActivity, "fetchRequestLoginInfo - onFailure");
            }
        });

        return null;
    }
}
