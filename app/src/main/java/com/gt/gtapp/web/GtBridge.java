package com.gt.gtapp.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.gt.gtapp.base.BaseActivity;
import com.gt.gtapp.base.BaseConstant;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.http.HttpResponseException;
import com.gt.gtapp.http.retrofit.BaseResponse;
import com.gt.gtapp.http.retrofit.HttpCall;
import com.gt.gtapp.http.rxjava.observable.DialogTransformer;
import com.gt.gtapp.http.rxjava.observable.ResultTransformer;
import com.gt.gtapp.http.rxjava.observer.BaseObserver;
import com.gt.gtapp.login.LoginActivity;
import com.gt.gtapp.login.StaffListIndustryActivity;
import com.gt.gtapp.main.DuofriendFragment;
import com.gt.gtapp.main.MainActivity;
import com.gt.gtapp.utils.commonutil.AppManager;
import com.gt.gtapp.utils.commonutil.AppUtils;
import com.gt.gtapp.utils.commonutil.ClipboardUtils;
import com.gt.gtapp.utils.commonutil.IntentUtils;
import com.gt.gtapp.utils.commonutil.NetworkUtils;
import com.gt.gtapp.utils.commonutil.StringUtils;
import com.gt.gtapp.utils.commonutil.ToastUtil;
import com.gt.gtapp.utils.commonutil.Utils;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzb on 2017/11/30 0030.
 */

public class GtBridge {
    /**
     * 回到概况页面
     */
    @JavascriptInterface
    public void goToHomeUrl() {
        DuofriendFragment duofriendFragment=((MainActivity) (MyApplication.getCurrentActivity())).getDuoFriendFragment();
        if (duofriendFragment!=null){
            duofriendFragment.goToHomeUrl();
        }
    }
    /**
     * 是否显示消息列表
     */
    @JavascriptInterface
    public void showMessage() {
        Toast.makeText(MyApplication.getAppContext(),"您有新的消息来了",Toast.LENGTH_LONG).show();
    }
    /**
     * 是否显示App底部
     */
    @JavascriptInterface
    public void showBottom(final boolean isShow) {
        Log.d("bottom","showBottom="+isShow);
        ((MainActivity) (MyApplication.getCurrentActivity())). runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    ((MainActivity) (MyApplication.getCurrentActivity())).showBottom(isShow);
                } else {
                    ((MainActivity) (MyApplication.getCurrentActivity())).showBottom(isShow);
                }
            }
        });
    }
    /**
     * 是否显示App头部
     */
    @JavascriptInterface
    public void showHeader(final boolean isShow) {
        Log.d("showHeader","showHeader="+isShow);
        Hawk.put(BaseConstant.HAWK_LEFT_IS_SHOW_HEADER,isShow);
        MyApplication.showHeader(isShow);
    }
    /**
     * 切换行业
     */
    @JavascriptInterface
    public void switchIndustry() {
        HttpCall.getApiService().staffListIndustry().compose(ResultTransformer.<List<StaffListIndustryBean>>transformer())
                .compose(new DialogTransformer().<List<StaffListIndustryBean>>transformer())
                .subscribe(new BaseObserver<List<StaffListIndustryBean>>() {
                    @Override
                    protected void onSuccess(List<StaffListIndustryBean> staffListIndustryBeanList) {
                        //获取上次点击选择记录的Hawk
                        //获取到员工账号的列表
                        Intent intent = new Intent(MyApplication.getAppContext(), StaffListIndustryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putParcelableArrayListExtra("staffListIndustryList", (ArrayList<? extends Parcelable>) staffListIndustryBeanList);
                        MyApplication.getAppContext().startActivity(intent);
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                        if (responseException.getCode() == BaseResponse.TOKEN_PAST_TIME) {
                            //animShowLoginView();
                            //session过期 请求刷新session并且登录其实就是再调用login按钮

                        }
                    }
                });

    }

    /**
     * 打开安卓原生界面
     *
     * @param activityPackageName 完整路径包名
     */
    @JavascriptInterface
    public String startActivity(String activityPackageName) {
        String res = "";
        try {
            Class c = Class.forName(activityPackageName);
            Intent intent = new Intent(MyApplication.getAppContext(), c);
            MyApplication.getAppContext().startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            res = "找不到该页面路径";
        } finally {
            return res;
        }
    }

    /**
     * 获取App版本号
     */
    @JavascriptInterface
    public String getAppVersionName() {
        return AppUtils.getAppVersionName();
    }

    /**
     * 获取App版本码
     *
     * @return App版本码
     */
    @JavascriptInterface
    public int getAppVersionCode() {
        return AppUtils.getAppVersionCode();
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    @JavascriptInterface
    public int getStatusHeight() {
        Context context = Utils.getContext();
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @return
     */
    @JavascriptInterface
    public int getBottomStatusHeight() {
        int totalHeight = getDpi();

        int contentHeight = getScreenHeight();

        return totalHeight - contentHeight;
    }

    /**
     * 复制文本到剪贴板
     */
    @JavascriptInterface
    public void copyText(final CharSequence text) {
        ClipboardUtils.copyText(text);
    }

    /**
     * 获取剪贴板的文本
     */
    @JavascriptInterface
    public CharSequence getText() {
        return ClipboardUtils.getText();
    }

    /**
     * 获取设备系统版本号
     */
    @JavascriptInterface
    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备型号
     */
    @JavascriptInterface
    public String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取厂商设备
     */
    @JavascriptInterface
    public String getProductName() {
        String productName = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            productName = (String) get.invoke(c, "ro.product.brand");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productName;
    }

    /**
     * 打开系统网络设置界面
     */
    @JavascriptInterface
    public void openWirelessSettings() {
        Utils.getContext().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     */
    @JavascriptInterface
    public boolean isConnected() {
        return NetworkUtils.isConnected();
    }

    /**
     * 判断移动数据是否打开
     */
    @JavascriptInterface
    public boolean getDataEnabled() {
        try {
            TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     * <p>需系统应用 需添加权限{@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>}</p>
     */
    @JavascriptInterface
    public void setDataEnabled(final boolean enabled) {
        try {
            TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断wifi是否打开
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @JavascriptInterface
    public boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}</p>
     */
    @JavascriptInterface
    public void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 判断wifi是否连接状态
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     */
    @JavascriptInterface
    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     */
    @JavascriptInterface
    @SuppressLint("HardwareIds")
    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : null;
    }

    /**
     * 拨打电话
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     */
    @JavascriptInterface
    public void call(final String phoneNumber) {
        Utils.getContext().startActivity(IntentUtils.getCallIntent(phoneNumber));
    }

    /**
     * 发送短信
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.SEND_SMS"/>}</p>
     */
    @JavascriptInterface
    public void sendSmsSilent(final String phoneNumber, final String content) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getContext(), 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    @JavascriptInterface
    public int getScreenWidth() {
        return Utils.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    @JavascriptInterface
    public int getScreenHeight() {
        return Utils.getContext().getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    @JavascriptInterface
    public void setPortrait(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @JavascriptInterface
    public boolean isLandscape() {
        return Utils.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    @JavascriptInterface
    public boolean isPortrait() {
        return Utils.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕分辨率dip
     */
    @JavascriptInterface
    public int getDpi() {
        Context context = Utils.getContext();
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 会被下一条覆盖
     */
    @JavascriptInterface
    public void showToast(String text) {
        ToastUtil.getInstance().showToast(text);
    }

    /**
     * 测试
     */
    @JavascriptInterface
    public String testFromJs(String fromJsString) {
        ToastUtil.getInstance().showToast(fromJsString);
        return "from android";
    }
}
