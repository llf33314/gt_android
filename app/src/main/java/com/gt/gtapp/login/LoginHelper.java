package com.gt.gtapp.login;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.HttpCodeMsgBean;
import com.gt.gtapp.bean.LoginAccountBean;
import com.gt.gtapp.bean.ShowLoginUiMsg;
import com.gt.gtapp.bean.SignBean;
import com.gt.gtapp.http.HttpResponseException;
import com.gt.gtapp.http.retrofit.BaseResponse;
import com.gt.gtapp.http.retrofit.HttpCall;
import com.gt.gtapp.http.rxjava.RxBus;
import com.gt.gtapp.http.rxjava.observable.ResultTransformer;
import com.gt.gtapp.http.store.PersistentCookieStore;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by wzb on 2017/12/13 0013.
 */

public class LoginHelper {

    public static final String ACCOUNT_KEY="loginAccount";
    public static final String PSD_KEY="loginPsd";

    private static Gson gson=new Gson();

    public static boolean loginUiIsShow=false;

    /**
     * 账号密码请求getSign接口跟erp登录接口刷新Session
     */
    public static Observable<BaseResponse<LoginAccountBean>> getNewSession(final String account, final String psd){
       return HttpCall.getApiService()
                .getSign(account,psd)
                .flatMap(ResultTransformer.<SignBean>flatMap())
                .flatMap(new Function<SignBean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull SignBean signBean) throws Exception {

                        return HttpCall.getApiService().erpLogin(account, psd, gson.toJson(signBean));
                    }
                })
                .flatMap(new Function<String, ObservableSource<BaseResponse<LoginAccountBean>>>() {
                    @Override
                    public ObservableSource<BaseResponse<LoginAccountBean>> apply(@NonNull String s) throws Exception {
                        String jsonResult;
                        HttpCodeMsgBean httpCodeMsgBean = null;
                        try {
                            jsonResult = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
                            httpCodeMsgBean = gson.fromJson(jsonResult, HttpCodeMsgBean.class);
                        } catch (Exception e) { //后台数据有误json转化出错
                            Intent intent=new Intent(MyApplication.getAppContext(), LoginActivity.class);
                            //不是在登录页面刷新session 有可能是密码改了等
                            if (!MyApplication.getCurrentActivity().getClass().equals(LoginActivity.class)){
                                MyApplication.getAppContext().startActivity(intent);
                            }
                            removeAllCookie();
                            return Observable.error( new HttpResponseException(500,"后台数据有误"));
                        }

                        if ("0".equals(httpCodeMsgBean.getCode())) {//登录成功
                            return HttpCall.getApiService().getLoginAccount();
                        }else{
                            Intent intent=new Intent(MyApplication.getAppContext(), LoginActivity.class);
                            //不是在登录页面刷新session 有可能是密码改了等
                            clearAccountInfo();
                            if (!MyApplication.getCurrentActivity().getClass().equals(LoginActivity.class)){
                                MyApplication.getAppContext().startActivity(intent);
                            }else if (!loginUiIsShow){
                                //是否显示登录UI 极端情况下才会走到这里
                                RxBus.get().post(new ShowLoginUiMsg());
                            }
                            return Observable.error(new HttpResponseException(2,"用户名或密码错误"));
                        }
                    }
                });
    }
    public static void saveAccountPsdHawk(String account,String psd){
        Hawk.put(ACCOUNT_KEY,account);
        Hawk.put(PSD_KEY,psd);
    }

    public static void clearAccountPsdHawk(){
        Hawk.delete(ACCOUNT_KEY);
        Hawk.delete(PSD_KEY);
    }

    public static boolean isSaveAccountPsd(){
        return !TextUtils.isEmpty((String)Hawk.get(ACCOUNT_KEY))&&!TextUtils.isEmpty((String)Hawk.get(PSD_KEY));
    }

    /**
     * 是否登录过  用cookie判断
     * @return
     */
    public static boolean isLogined(){
        PersistentCookieStore persistentCookieStore = PersistentCookieStore.getInstance();
        return persistentCookieStore.getCookies()!=null&&persistentCookieStore.getCookies().size()>0;
    }

    public static void removeAllCookie(){
        PersistentCookieStore persistentCookieStore = PersistentCookieStore.getInstance();
        persistentCookieStore.removeAll();
    }

    public static void clearAccountInfo(){
        removeAllCookie();
        Hawk.delete(StaffListIndustryActivity.STAFF_CHOOSE_URL);
        clearAccountPsdHawk();
    }

}
