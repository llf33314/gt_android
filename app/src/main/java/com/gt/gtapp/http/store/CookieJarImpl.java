package com.gt.gtapp.http.store;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.gt.gtapp.http.HttpConfig;
import com.gt.gtapp.login.LoginHelper;
import com.gt.gtapp.utils.Logger;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;
    private Context context;

    /**
     * 由于跨域名 需要用个字段保存
     */
    private String DEFAULT_COOKIE_NAME="DEFAULT_COOKIE_NAME";

    //暂时注释cookie管理后续改用X5看看如何管理
    CookieManager mCookieManager=CookieManager.getInstance();
   // XWalkCookieManager mCookieManager = new XWalkCookieManager();

    public CookieJarImpl(CookieStore cookieStore, Context context) {
        if (cookieStore == null) new IllegalArgumentException("cookieStore can not be null.");
        this.cookieStore = cookieStore;
        this.context=context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Logger.i("GtCookie","saveFromResponse url="+url.host()+" url.host="+url.host()+"  cookies.size="+cookies.size()+" cookies="+cookies.get(0).toString());

        if (cookies!=null&&cookies.size()>0&&HttpConfig.ERP_LOGIN_URL.equals(url.toString())) {
            LoginHelper.removeAllCookie();
            saveCookies(url.host(),cookies);
            cookieStore.add(DEFAULT_COOKIE_NAME, cookies);
        }
        //cookieStore.add(DEFAULT_COOKIE_NAME, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        Logger.i("GtCookie","loadForRequest url="+url.host());
        List<Cookie> cookies= cookieStore.get(DEFAULT_COOKIE_NAME);
        return cookies;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }


    /**
     * 将浏览器cookie 同步到RAM内存中
     */
    private void saveCookies(String url, List<Cookie> cookies) {
        mCookieManager.setAcceptCookie(true);
        for (Cookie c:cookies){
            mCookieManager.setCookie(url,c.toString());
        }
        if (Build.VERSION.SDK_INT < 21) {
         CookieSyncManager.createInstance(context).sync();
        } else {
            mCookieManager.flush();
        }

        /*mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptFileSchemeCookies(true);
        mCookieManager.setCookie(url,cookies);
        mCookieManager.flushCookieStore();*/

    }
}
