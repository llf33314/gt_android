package com.gt.gthttp;

import android.content.Context;

import com.gt.gthttp.retrofit.BaseApiService;

/**
 * Created by wzb on 2017/12/5 0005.
 * 初始化一些参数
 */

public class HttpInit {
    private static Context appContext;
    private static BaseApiService mBaseApiService;
    private static String baseUrl;

    public static void init(Context context,String baseUrl,BaseApiService apiService){
        HttpInit.appContext=context;
        HttpInit.mBaseApiService=apiService;
        HttpInit.baseUrl=baseUrl;
    }
    public static Context getAppContext(){
        if (appContext==null){
            throw new NullPointerException("GtHttp not Init");
        }
        return appContext;
    }

    public static  BaseApiService getBaseApiService(){
        if (mBaseApiService==null){
            throw new NullPointerException("GtHttp not Init");
        }
        return mBaseApiService;
    }
    public static String getBaseUrl(){
        if (baseUrl==null){
            throw new NullPointerException("GtHttp not Init");
        }
        return baseUrl;
    }
}
