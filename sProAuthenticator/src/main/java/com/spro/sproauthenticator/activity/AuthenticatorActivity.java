package com.spro.sproauthenticator.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spro.sproauthenticator.AccountGeneral;
import com.spro.sproauthenticator.R;
import com.spro.sproauthenticator.network.helper.APIServiceHelper;
import com.spro.sproauthenticator.network.token.LoginRequest;
import com.spro.sproauthenticator.network.token.LoginResponse;
import com.spro.sproauthenticator.utils.CommonUtils;
import com.spro.sproauthenticator.utils.Logger;
import com.spro.sproauthenticator.utils.MyToast;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SMSCODE = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    EditText mUserName;
    EditText mPassword;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        mAccountManager = AccountManager.get(getBaseContext());

        mUserName = (EditText) findViewById(R.id.accountName);
        mPassword = (EditText) findViewById(R.id.accountPassword);

        mAuthTokenType = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        if (accountName != null) {
            ((TextView) findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The sign up activity returned that the user has successfully created an account
        Logger.d(TAG, "AuthenticatorActivity: onActivityResult");
        if (requestCode == REQ_SMSCODE && resultCode == RESULT_OK) {
            finishLogin(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void submit() {

        final String userName = mUserName.getText().toString();
        final String userPass = mPassword.getText().toString();
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        if (userName == null || userName.isEmpty()) {
            MyToast.showErrorMessage(AuthenticatorActivity.this, getString(R.string.enter_username));
            return;
        } else {
            mUserName.setError(null);
        }

        if (userPass == null || userPass.isEmpty()) {
            MyToast.showErrorMessage(AuthenticatorActivity.this, getString(R.string.enter_password));
            return;
        } else {
            mPassword.setError(null);
        }

        if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
            MyToast.showErrorMessage(AuthenticatorActivity.this, getString(R.string.network_error));
            return;
        }
        fetchRequestLoginInfo(userName, userPass);

    }

    public void fetchRequestLoginInfo(final String userName, final String userPass) {
        LoginRequest request = new LoginRequest();
        request.setUsername(userName);
        request.setPassword(userPass);
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

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
                            if(response.getCode()==429){
                                MyToast.showLongErrorMessage(AuthenticatorActivity.this, "Mã OTP đã được gửi đến điện thoại, vui lòng kiểm tra và nhập mã OTP");
                            }
                            Intent intent = new Intent(AuthenticatorActivity.this, VerifyOtpActivity.class);
                            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                            intent.putExtra(CommonUtils.KEY_ACCOUNT_PASS, userPass);
                            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                            startActivityForResult(intent, REQ_SMSCODE);
                        }else{
                                MyToast.showLongErrorMessage(AuthenticatorActivity.this, response.getMessage());
                        }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.d(TAG, "fetchRequestLoginInfo - onFailure: " + t.toString());
                MyToast.showLongErrorMessage(AuthenticatorActivity.this, "fetchRequestLoginInfo - onFailure");
            }
        });
    }

    private void finishLogin(Intent intent) {
        try {
            Logger.d(TAG, "Authenticator: finishLogin");
            String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String accountPassword = intent.getStringExtra(CommonUtils.KEY_ACCOUNT_PASS);
            String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
            Logger.d(TAG, "Authenticator: accountName = "+ accountName
                                + "\n accountPassword = "+ accountPassword
                                + "\n accountType = " + accountType);
            Account account = new Account(accountName, accountType);
            if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
                Logger.d(TAG, "> finishLogin > addAccountExplicitly" + accountPassword);
                String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                String authtokenType = mAuthTokenType;
                Logger.d(TAG, " auth token" + authtoken);
                // Creating the account on the device and setting the auth token we got
                // (Not setting the auth token will cause another call to the server to authenticate the user)
                mAccountManager.addAccountExplicitly(account, accountPassword, null);
                mAccountManager.setAuthToken(account, authtokenType, authtoken);
            } else {
                Logger.d(TAG, "> finishLogin > setPassword>" + accountPassword);
                mAccountManager.setPassword(account, accountPassword);
            }
            //add phone number info
            mAccountManager.setUserData(account, "phone_number", "0987654321");     //ttcn88-for test only

            Logger.d(TAG, "> finishLogin > setAccountAuthenticatorResult>");
            setAccountAuthenticatorResult(intent.getExtras());
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Logger.d(TAG, "> finishLogin > Exception>" + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
