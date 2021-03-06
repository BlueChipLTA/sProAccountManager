package com.spro.sproaccountmanager_example;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import com.spro.sproauthenticator.AccountGeneral;
import com.spro.sproauthenticator.sProAuthenticator;
import com.spro.sproauthenticator.utils.Logger;

public class MainActivity extends Activity {

    private String TAG = "ttcn88." +this.getClass().getSimpleName();
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_INVALIDATE = "state_invalidate";

    private AccountManager mAccountManager;
    private AlertDialog mAlertDialog;
    private boolean mInvalidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = AccountManager.get(this);
        sProAuthenticator authenticator = new sProAuthenticator(this);

        findViewById(R.id.btnAddAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.spro.sproauthentictor.sync");
//                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setComponent (new
                                ComponentName("com.spro.sproauthenticator",
                        "com.spro.sproauthenticator.sync.SyncDataBroadcast"));

                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btnGetAuthToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false);
            }
        });

        findViewById(R.id.btnGetAuthTokenByFeature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
            }
        });

        findViewById(R.id.btnRefreshToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            }
        });



//        if (savedInstanceState != null) {
//            boolean showDialog = savedInstanceState.getBoolean(STATE_DIALOG);
//            boolean invalidate = savedInstanceState.getBoolean(STATE_INVALIDATE);
//            if (showDialog) {
//                showAccountPicker(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, invalidate);
//            }
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            outState.putBoolean(STATE_DIALOG, true);
//            outState.putBoolean(STATE_INVALIDATE, mInvalidate);
//        }
    }

    /**
     * Add new account to the account manager
     * @param accountType
     * @param authTokenType
     */
    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType,
                                                                                authTokenType,
                                                                        null,
                                                                        null,
                                                                        this,
                new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");
                    Log.d("ttcn88", "AddNewAccount Bundle is " + bnd);

                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Show all the accounts registered on the account manager. Request an auth token upon user select.
     * @param authTokenType
     */
    private void showAccountPicker(final String authTokenType, final boolean invalidate) {
        mInvalidate = invalidate;
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        Logger.d("ttcn88", "number of availableAccounts: " + availableAccounts.length);
        if (availableAccounts.length == 0) {
            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
        } else {
            if(invalidate)
                invalidateAuthToken(availableAccounts[0], authTokenType);
            else
                getExistingAccountAuthToken(availableAccounts[0], authTokenType);
//            String name[] = new String[availableAccounts.length];
//            for (int i = 0; i < availableAccounts.length; i++) {
//                name[i] = availableAccounts[i].name;
//            }

            // Account picker
//            Log.d("ttcn88", "showAccountPicker pic account");
//            mAlertDialog = new AlertDialog.Builder(this).setTitle("Pick Account")
//                    .setAdapter(new ArrayAdapter<String>(getBaseContext(),
//                                    android.R.layout.simple_list_item_1, name),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if(invalidate)
//                                        invalidateAuthToken(availableAccounts[which], authTokenType);
//                                    else
//                                        getExistingAccountAuthToken(availableAccounts[which], authTokenType);
//                                }
//                            }
//                    ).create();
//            mAlertDialog.show();
        }
    }

    /**
     * Get the auth token for an existing account on the AccountManager
     * @param account
     * @param authTokenType
     */
    private void getExistingAccountAuthToken(Account account, String authTokenType) {
        Logger.d("ttcn88", "Enter getExistingAccountAuthToken"
                + "\nauthTokenType = "+authTokenType
                + "\naccount = "+ account.toString());
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account,
                                                                                    authTokenType,
                                                                                    null,
                                                                                    this,
                                                                                    null,
                                                                                    null);
        Logger.d("ttcn88", "after");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();
                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);

                    showMessage((authtoken != null) ? "SUCCESS!\ntoken: " + authtoken : "FAIL");
                    Log.d("ttcn88", "GetToken Bundle is " + bnd);
                } catch (Exception e) {
                    Logger.d(TAG, "get getExistingAccountAuthToken FAILED: = "+ e.toString());
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     *
     * @param accountType
     * @param authTokenType
     */
    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            showMessage(((authtoken != null) ? "SUCCESS!\ntoken: " + authtoken : "FAIL"));
                            Log.d(" vb                               ", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }
                , null);
    }

    /**
     * Invalidates the auth token for the account
     *
     * @param account
     * @param authTokenType
     */
    private void invalidateAuthToken(final Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();

                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    mAccountManager.invalidateAuthToken(account.type, authtoken);
                    showMessage(account.name + " invalidated");
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }
}