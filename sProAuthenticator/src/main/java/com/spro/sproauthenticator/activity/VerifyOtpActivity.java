package com.spro.sproauthenticator.activity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

//import androidx.appcompat.app.AppCompatActivity;

import com.spro.sproauthenticator.R;
import com.spro.sproauthenticator.network.data.SharePrefsHelper;
import com.spro.sproauthenticator.network.helper.APIServiceHelper;
import com.spro.sproauthenticator.network.token.TokenRequest;
import com.spro.sproauthenticator.network.token.TokenResponse;
import com.spro.sproauthenticator.utils.CommonUtils;
import com.spro.sproauthenticator.utils.Logger;
import com.spro.sproauthenticator.utils.MyToast;

public class VerifyOtpActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    private EditText mInputCode;
    private Intent mIntent;

    private SharePrefsHelper mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mSharedPreferences = new SharePrefsHelper(VerifyOtpActivity.this);

        mIntent = this.getIntent();
        mInputCode = findViewById(R.id.input_code);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mOtpCode = mInputCode.getText().toString();

                if (mOtpCode == null || mOtpCode.isEmpty()) {
                    MyToast.showErrorMessage(VerifyOtpActivity.this, getString(R.string.enter_otp_code));
                    return;
                } else {
                    mInputCode.setError(null);
                }

                if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
                    MyToast.showErrorMessage(VerifyOtpActivity.this, getString(R.string.network_error));
                    return;
                }

                //request token
                TokenRequest tokenRequest = new TokenRequest();
                tokenRequest.setGrantType(CommonUtils.GRANT_TYPE);
                tokenRequest.setClientId(CommonUtils.CLIENT_ID);
                tokenRequest.setClientSecret(CommonUtils.CLIENT_SECRET);
                tokenRequest.setProvider(CommonUtils.PROVIDER);
                tokenRequest.setUsername(mIntent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));   //extra string send from intent
                tokenRequest.setOtp(mOtpCode);
                fetchRequestTokenInfo(tokenRequest);
            }
        });


    }



    public void fetchRequestTokenInfo(TokenRequest tokenRequest) {
        Log.d(TAG, "fetchRequestTokenInfo: " + tokenRequest.toString());
        APIServiceHelper.requestToken(tokenRequest, TokenResponse.class, new APIServiceHelper.IResponseCallback<TokenResponse>() {
            @Override
            public void onPrepare() {
                Logger.d(TAG, "fetchRequestTokenInfo - onPrepare()");
            }

            @Override
            public void onResponse(TokenResponse response) {
//                Log.d(TAG, "onResponse: " + response.toString());
                if (response != null) {
                    Logger.d(TAG, "fetchRequestTokenInfo: " + response.toString());
                    if (response.getSuccess()) {
                        Logger.d(TAG, "fetchRequestTokenInfo: Luu token");
//                        //Luu token
                        mSharedPreferences.putTokenType(response.getData().getTokenType());
                        mSharedPreferences.putAccessToken(response.getData().getAccessToken());
                        mSharedPreferences.putRefreshToken(response.getData().getRefreshToken());
                        mSharedPreferences.putExpiresIn(response.getData().getExpiresIn());
                        mSharedPreferences.putEmail(response.getData().getEmail());
                        mSharedPreferences.putUserID(response.getData().getId());
                        mSharedPreferences.putUserName(response.getData().getUsername());
                        mSharedPreferences.setLastLogin(response.getData().getLastLogin());
                        mSharedPreferences.setLoggedInMode(true);

                        //send feedback thong tin
                        Intent res = getIntent();
                        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, response.getData().getUsername());
                        res.putExtra(CommonUtils.KEY_ACCOUNT_PASS, mIntent.getStringExtra(CommonUtils.KEY_ACCOUNT_PASS));
                        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, mIntent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
                        res.putExtra(AccountManager.KEY_AUTHTOKEN, response.getData().getAccessToken());
                        //finish activity and add account into system account manager
                        Logger.d(TAG, "Feedback result OK to previous activiy");
                        setResult(RESULT_OK, res);
                        finish();
                    }else{
//                        if(response.getCode() == 401){      //Loi gui request khi token da bi xoa
//                            MyToast.showLongErrorMessage(VerifyOtpActivity.this, "Request is missing a required parameters. Please try again!");
//                        }else{
                            MyToast.showLongErrorMessage(VerifyOtpActivity.this, "The Request is missing a required parameters. Please try again!");
                            Logger.d(TAG, "fetchRequestTokenInfo: getMessage: " + response.getMessage());
//                        }
                        Logger.d(TAG, "Feedback result CANCEL to previous activiy");
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.d(TAG, "fetchRequestTokenInfo - onFailure: " + t.toString());
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Logger.d(TAG, "VerifyOTP: onBackPressed: ");
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

}