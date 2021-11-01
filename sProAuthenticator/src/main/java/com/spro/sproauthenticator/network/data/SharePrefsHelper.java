package com.spro.sproauthenticator.network.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.spro.sproauthenticator.utils.Logger;

import static android.content.Context.MODE_PRIVATE;

public class SharePrefsHelper {
    private static final String TAG = "SharePrefsHelper";
    public static final String MY_PREFS = "MY_PREFS";

    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String EXPIRES_IN = "EXPIRES_IN";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String LAST_LOGIN = "LAST_LOGIN";

    SharedPreferences mSharedPreferences;

    public SharePrefsHelper(Context context) {
        Context storageContext = null;
        try{
            final Context deviceContext = context.createDeviceProtectedStorageContext();
            if (!deviceContext.moveSharedPreferencesFrom(context, MY_PREFS)) {
                Logger.d(TAG, "Failed to migrate shared preferences.");
            }
            storageContext = deviceContext;
            mSharedPreferences = storageContext.getSharedPreferences(MY_PREFS, context.MODE_PRIVATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void putTokenType(String token){
        mSharedPreferences.edit().putString(TOKEN_TYPE, token).apply();
    }

    public String getTokenType(){
        return mSharedPreferences.getString(TOKEN_TYPE, null);
    }

    public void putAccessToken(String token){
        mSharedPreferences.edit().putString(ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken(){
        return mSharedPreferences.getString(ACCESS_TOKEN, null);
    }

    public void putRefreshToken(String token){
        mSharedPreferences.edit().putString(REFRESH_TOKEN, token).apply();
    }

    public String getRefreshToken(){
        return mSharedPreferences.getString(REFRESH_TOKEN, null);
    }

    public void putExpiresIn(Integer token){
        mSharedPreferences.edit().putInt(EXPIRES_IN, token).apply();
    }

    public Integer getExpiresIn(){
        return mSharedPreferences.getInt(EXPIRES_IN, 0);
    }

    public void putUserID(Integer user_id){
        mSharedPreferences.edit().putInt(USER_ID, user_id).apply();
    }

    public int getUserID(){
        return mSharedPreferences.getInt(USER_ID, 0);
    }

    public void putUserName(String user_name){
        mSharedPreferences.edit().putString(USER_NAME, user_name).apply();
    }

    public String getUserName(){
        return mSharedPreferences.getString(USER_NAME, null);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public void putEmail(String email) {
        mSharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(EMAIL, null);
    }

    public boolean isLoggedInMode() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedInMode(boolean loggedIn) {
        mSharedPreferences.edit().putBoolean(IS_LOGGED_IN, loggedIn).apply();
    }

    public String getLastLogin() {
        return mSharedPreferences.getString(IS_LOGGED_IN, "");
    }

    public void setLastLogin(String loggedIn) {
        mSharedPreferences.edit().putString(IS_LOGGED_IN, loggedIn).apply();
    }

}
