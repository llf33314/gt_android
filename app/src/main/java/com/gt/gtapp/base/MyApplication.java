package com.gt.gtapp.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.gt.gtapp.http.ApiService;
import com.gt.gtapp.http.HttpConfig;
import com.gt.gtapp.utils.Logger;
import com.gt.gthttp.HttpInit;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by wzb on 2017/12/5 0005.
 */

public class MyApplication extends Application {
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        HttpInit.init(this, HttpConfig.BASE_RUL, new ApiService());
        preInitX5WebCore();
    }

    private void preInitX5WebCore() {

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    public static Context getAppContext(){
        return appContext;
    }
}
