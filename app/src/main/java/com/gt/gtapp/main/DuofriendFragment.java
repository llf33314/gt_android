package com.gt.gtapp.main;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.gtapp.R;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.ClearCacheBean;
import com.gt.gtapp.bean.LoginFinishMsg;
import com.gt.gtapp.http.rxjava.RxBus;
import com.gt.gtapp.http.rxjava.observable.SchedulerTransformer;
import com.gt.gtapp.utils.commonutil.LogUtils;
import com.gt.gtapp.web.GtBridge;
import com.orhanobut.hawk.Hawk;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/12/6 0006.
 */

@SuppressLint("ValidFragment")
public class DuofriendFragment extends Fragment {

    @BindView(R.id.main_webView)
    WebView webView;
    Unbinder unbinder;
    GtBridge gtBridge;
    String url;

    public DuofriendFragment(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_duofriend, container, false);
        unbinder = ButterKnife.bind(this, v);
        initWebview();
        return v;
    }

    private void initWebview() {
        //webView.loadUrl("http://192.168.3.32:8888/#");

        webView.loadUrl(url);
        gtBridge = new GtBridge();
        webView.addJavascriptInterface(gtBridge, "AndroidAppJs");//添加js监听 这样html就能调用客户端

        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getActivity().getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getActivity().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getActivity().getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        registerClearCache();
    }


    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
    };

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        /**
         *
         * @return 本地jquery
         */
        private WebResourceResponse editResponse() {
            try {
                Log.d("web", "new WebResourceResponse");

                return new WebResourceResponse("application/x-javascript", "utf-8", getActivity().getAssets().open("js/jquery.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //需处理特殊情况
            return null;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (Build.VERSION.SDK_INT < 21) {
                if (url.contains("jquery.js")) {
                    return editResponse();
                }
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= 21) {
                String url = request.getUrl().toString();
                Log.d("web", " shouldInterceptRequest url=" + url + "  ----home=" + Hawk.get("homeURL", ""));
                if (!TextUtils.isEmpty(url) && url.contains("jquery.js")) {
                    return editResponse();
                }

            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // startTime = System.currentTimeMillis();
            if (url.toString().equals(Hawk.get("homeURL", ""))) {
                LogUtils.d("setRedToolbar");
                ((MainActivity) getActivity()).setH5Title("");
                ((MainActivity) getActivity()).showBottom(true);
                ((MainActivity) getActivity()).setRedToolbar();
                MyApplication.showHeader(true);

            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //loadTime.setText("加载时间：" + (System.currentTimeMillis() - startTime) + " ms");
            //showHeader();
            //showBottom();
            //showTitle(MyApplication.getToolBarTextView());
            super.onPageFinished(view, url);
        }
    };

    @Override
    public void onResume() {
        // showHeader();
        // showBottom();
        super.onResume();
    }

    public boolean onBackKeyDown() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

    public void reLoad() {
        webView.reload();
    }

    public void goToHomeUrl() {
        String homeURL = Hawk.get("homeURL", "");
        if (!TextUtils.isEmpty(homeURL)) {
            Log.d("goToHomeUrl", "goToHomeUrl=" + homeURL);
            webView.loadUrl(homeURL);
            gtBridge.showBottom(true);
            gtBridge.showHeader(true);
            //webView.reload();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void registerClearCache() {
        RxBus.get().toObservable(ClearCacheBean.class)
                .compose(SchedulerTransformer.<ClearCacheBean>transformer())
                .subscribe(new Consumer<ClearCacheBean>() {
                    @Override
                    public void accept(@NonNull ClearCacheBean clearCacheBean) throws Exception {
                        if (webView != null) {
                            webView.clearCache(true);
                            webView.clearFormData();
                            getActivity().getCacheDir().delete();
                            webView.loadUrl("javascript: localStorage.clear()");
                            LogUtils.d("clear cache");
                        }
                    }
                });

    }

    private String result = "";

    /**
     * js回调
     *
     * @param jsFn
     */
    public String webLoadJS(final String jsFn) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder("javascript:");
                stringBuilder.append("WebMethod.");
                stringBuilder.append(jsFn);
                stringBuilder.append("(");
                stringBuilder.append(")");
                Log.i("demo", "stringBuilder=" + stringBuilder.toString());
                webView.loadUrl(stringBuilder.toString());
                webView.evaluateJavascript(stringBuilder.toString(), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        result = value;
                    }
                });
            }
        });
        return result;
    }

    private void showHeader() {
        String isShow = webLoadJS("isHeader");
        LogUtils.d("showHeader isShow=" + isShow);
        if (isShow.equals("true")) {
            gtBridge.showHeader(true);
        } else if (isShow.equals("false")) {
            gtBridge.showHeader(false);
        }
    }

    public void showTitle(TextView textView) {
        String title = "";
        LogUtils.d("showTitle title=" + title);

        if (title == null || title.equals("null")) {
            title = "";
        }
        if (textView != null) {
            textView.setText(title);
        }
    }

    private void showBottom() {
        String isShow = webLoadJS("isShowBottom");
        if (TextUtils.isEmpty(isShow)) {
            isShow = "true";
        }
        LogUtils.d("showBottom isShow=" + isShow);
        if (isShow.equals("true")) {
            gtBridge.showBottom(true);
        } else if (isShow.equals("false")) {
            gtBridge.showBottom(false);
        }
    }
}
