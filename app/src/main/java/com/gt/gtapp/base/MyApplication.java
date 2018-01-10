package com.gt.gtapp.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.gtapp.BuildConfig;
import com.gt.gtapp.utils.Logger;
import com.gt.gtapp.utils.commonutil.ActivityUtils;
import com.gt.gtapp.utils.commonutil.AppManager;
import com.gt.gtapp.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by wzb on 2017/12/5 0005.
 */

public class MyApplication extends Application {

    private static boolean isNeedUpdate=true;
    private static Activity currentActivity;


    private static int accountType;

    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext=this;
        initLogger();
        Hawk.init(this).build();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity=activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }


    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean arg0) {
            // TODO Auto-generated method stub
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            Logger.d("onViewInitFinished", " onViewInitFinished is " + arg0);
        }

        @Override
        public void onCoreInitFinished() {
            // TODO Auto-generated method stub
        }
    };


    private void initLogger(){
        if (BuildConfig.DEBUG){
            Logger.LOG_LEVEL=Logger.VERBOS;
        }else{
            Logger.LOG_LEVEL=Logger.ERROR;
        }
    }



    public static void deleteHawk(){
        Hawk.delete(BaseConstant.HAWK_LEFT_IS_SHOW_HEADER);
    }
    public static void showHeader(final boolean isShow){
        ((BaseActivity) (MyApplication.getCurrentActivity())). runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    ((BaseActivity) (MyApplication.getCurrentActivity())).showToolBar();
                } else {
                    ((BaseActivity) (MyApplication.getCurrentActivity())).goneToolBar();
                }
            }
        });
    }
    public static TextView getToolBarTextView(){
        return  ((BaseActivity) (MyApplication.getCurrentActivity())).toolBarTitle;
    }
    public static RelativeLayout getToolBar(){
        LogUtils.d("ActivityUtils.getTopActivity(appContext)="+ActivityUtils.getTopActivity());
        return  ((BaseActivity) (MyApplication.getCurrentActivity())).mToolbar;
    }
    public static Activity getCurrentActivity(){
        return ActivityUtils.getTopActivity();
    }

    public static int getAccountType() {
        return accountType;
    }

    public static void setAccountType(int accountType) {
        MyApplication.accountType = accountType;
    }

    public static Context getAppContext(){
        return appContext;
    }

    public static void appExit(){
        android.os.Process.killProcess(Process.myPid());
    }
    public static boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public static void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

}
