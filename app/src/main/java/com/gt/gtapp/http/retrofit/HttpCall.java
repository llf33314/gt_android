package com.gt.gtapp.http.retrofit;

import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.http.ApiService;
import com.gt.gtapp.http.HttpConfig;
import com.gt.gtapp.http.retrofit.converter.string.StringConverterFactory;
import com.gt.gtapp.http.store.CookieJarImpl;
import com.gt.gtapp.http.store.PersistentCookieStore;
import com.gt.gtapp.utils.Logger;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpCall {

    private static ApiService mApiService;

    public static ApiService getApiService(){
        if (mApiService==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (Logger.LOG_LEVEL>1){
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }else{
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            PersistentCookieStore persistentCookieStore = new PersistentCookieStore(MyApplication.getAppContext());
            CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore, MyApplication.getAppContext());

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    // .addNetworkInterceptor(mRequestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .cookieJar(cookieJarImpl)
                    // .authenticator(mAuthenticator2)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.BASE_RUL)
                    .client(okHttpClient)
                    .addConverterFactory(StringConverterFactory.create()) //String 转换
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .validateEagerly(true)
                    .build();

            mApiService = retrofit.create(ApiService.class);
        }
        return mApiService;
    }


}
