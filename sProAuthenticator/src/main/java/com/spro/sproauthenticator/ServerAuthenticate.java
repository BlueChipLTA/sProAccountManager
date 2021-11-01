package com.spro.sproauthenticator;

import android.app.Activity;

public interface ServerAuthenticate {
    public String userSignIn(Activity mActivity, final String user, final String pass, String authType) throws Exception;
}
