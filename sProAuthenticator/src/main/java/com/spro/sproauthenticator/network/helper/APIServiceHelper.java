package com.spro.sproauthenticator.network.helper;

import android.util.Log;

//import androidx.annotation.NonNull;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

import com.google.gson.Gson;
import com.spro.sproauthenticator.BuildConfig;
import com.spro.sproauthenticator.network.api.APIUrl;
import com.spro.sproauthenticator.network.token.LoginRequest;
import com.spro.sproauthenticator.network.token.TokenRequest;
import com.spro.sproauthenticator.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

//import javax.annotation.Nonnull;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class APIServiceHelper {
    private static final int NETWORK_TIMEOUT = 60;
    private static String TAG = "APIServiceHelper";
    private static Retrofit retrofit;
    private static Retrofit retrofitNotify;
    private static OkHttpClient okHttpClient;
    private static OkHttpClient okHttpClientNotify;

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static TrustManager[] trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return trustManagers;
    }

    private static CommonService getCommonService() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            // Install the all-trusting trust manager
            try {
                okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                okHttpClientBuilder.connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
                okHttpClientBuilder.readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
                okHttpClientBuilder.writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
                //okHttpClientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
//                okHttpClientBuilder.addInterceptor(new TokenExpiredInterceptor());
//                okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());

                okHttpClient = okHttpClientBuilder.build();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(APIUrl.BASE_URL)
                    .client(okHttpClient)
                    .build();

        return retrofit.create(CommonService.class);
    }


    private static Map<String, String> getHeader(String token) {
        HashMap<String, String> map;
        map = new HashMap<>();
        map.put("x-access-token", token);

        String culture = "vn-" + Locale.getDefault().getLanguage();
        map.put("culture", culture);

        return map;
    }


    /*
     * Ham goi API login
     */
    public static <T> void requestLogin(LoginRequest request, final Class<T> classType, final IResponseCallback<T> iResponseCallback) {
        iResponseCallback.onPrepare();
        Call<String> objectCall = getCommonService().getLogin(request);
        Logger.d(TAG, "Gui request Login");

        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        Log.d("1407." + TAG, "getNewToken - response: "+ response.body());
                        T dataResponse = new Gson().fromJson(response.body(), classType);
                        iResponseCallback.onResponse(dataResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        iResponseCallback.onFailure(new Throwable(e.getMessage()));
                    }
                } else {
                    iResponseCallback.onFailure(new Throwable("Return null response!"));
                    Log.d("1407." + TAG, "getNewToken - response: onFailure");
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<String> call, Throwable t) {
                iResponseCallback.onFailure(t);
            }
        });
    }

    /*
     * Ham goi API get token
     */
    public static <T> void requestToken(TokenRequest request, final Class<T> classType, final IResponseCallback<T> iResponseCallback) {
        iResponseCallback.onPrepare();
        Call<String> objectCall = getCommonService().getToken(request);
        Logger.d(TAG, "Gui request Token");

        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        Log.d(TAG, "requestToken - response: "+ response.body());
                        T dataResponse = new Gson().fromJson(response.body(), classType);
                        iResponseCallback.onResponse(dataResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        iResponseCallback.onFailure(new Throwable(e.getMessage()));
                    }
                } else {
                    iResponseCallback.onFailure(new Throwable("Return null response!"));
                    Log.d(TAG, "requestToken - response: onFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponseCallback.onFailure(t);
            }
        });
    }

    public interface IResponseCallback<T> {
        void onPrepare();

        void onResponse(T response);

        void onFailure(Throwable t);
    }
}