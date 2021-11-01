package com.spro.sproauthenticator.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 4/26/2018.
 */

public class CommonUtils {
    public final static boolean DEBUG = false;

    public final static String ROOM_TYPE_1_1 = "001";
    public final static String ROOM_TYPE_GROUP_CHAT = "002";
    public final static String MESSAGE_TYPE_TEXT = "001";
    public final static String MESSAGE_TYPE_FILE = "002";
    public static final String ACTION_MESSAGE = "iot.ttcn.appchat.RECEIVEMESSAGE";
    public static final String ACTION_TYPING = "iot.ttcn.appchat.TYPING";
    public static final String RECEIVE_MESSAGE = "RECEIVE_MESSAGE";
    public static final String DECYPT_MESSAGE = "DECYPT_MESSAGE";
    public static final String RECEIVE_TYPING = "TYPING";
    public static final String ACTION_UPDATE_LAST_MESSAGE = "iot.ttcn.appchat.UPDATE_LAST_MESSAGE";
    public static final String ACTION_EDIT_GROUP = "android.intent.action.EDIT_GROUP";
    public static final String ACTION_NEW_GROUP = "android.intent.action.NEW_GROUP";
    public static final String ACTION_UPDATE_ROOM = "android.intent.action.UPDATE_ROOM";
    public static final String UPDATE_ROOM = "UPDATE_ROOM";
    public static final String EXTRA_PHOTO_RESULT = "extra_photo_result";
    public static final int REQUEST_PHOTO = 100;
    //SSO authenticator
    public static String KEY_ACCOUNT_USERNAME = "sso_username";
    public static String KEY_ACCOUNT_PASS = "sso_password";
    public static String KEY_ACCOUNT_TYPE = "sso_accountType";
    public static String KEY_AUTHTOKEN = "sso_authtoken";

    //SSO verify OTP
    public static final String GRANT_TYPE = "otp_grant";
    public static String CLIENT_ID = "4";
    public static String CLIENT_SECRET = "K1OVsBg0AwjTd8NvxGjcuv6Pr8yuhKp33aV0yqAz";
    public static String PROVIDER = "users";

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isUserName(String username){
        Pattern pattern;
        Matcher matcher;
        final String USER_PATTERN =  "[a-zA-Z0-9_]+";
        pattern = Pattern.compile(USER_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isPassWord(String pw){
        Pattern pattern;
        Matcher matcher;
        final String PW_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PW_PATTERN);
        matcher = pattern.matcher(pw);
        return matcher.matches();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
