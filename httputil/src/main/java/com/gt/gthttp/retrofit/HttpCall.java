package com.gt.gthttp.retrofit;

import com.gt.gthttp.HttpInit;
import com.gt.gthttp.Logger;
import com.gt.gthttp.retrofit.converter.string.StringConverterFactory;
import com.gt.gthttp.web.store.CookieJarImpl;
import com.gt.gthttp.web.store.PersistentCookieStore;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpCall {

    private static BaseApiService mApiService;

    public static BaseApiService getApiService(){
        if (mApiService==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (Logger.LOG_LEVEL>1){
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }else{
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            PersistentCookieStore persistentCookieStore = new PersistentCookieStore(HttpInit.getAppContext());
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore, HttpInit.getAppContext());

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    // .addNetworkInterceptor(mRequestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .cookieJar(cookieJarImpl)
                    // .authenticator(mAuthenticator2)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpInit.getBaseUrl())
                    .client(okHttpClient)
                    .addConverterFactory(StringConverterFactory.create()) //String 转换
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .validateEagerly(true)
                    .build();

            mApiService = retrofit.create(HttpInit.getBaseApiService().getClass());
        }
        return mApiService;
    }


}
