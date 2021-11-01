package com.spro.sproauthenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SproAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        sProAuthenticator authenticator = new sProAuthenticator(this);
        return authenticator.getIBinder();
    }
}
