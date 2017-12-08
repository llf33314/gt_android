package com.gt.gtapp.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.WindowManager;
import android.widget.TextView;

import com.gt.gtapp.R;
import com.gt.gtapp.base.MyApplication;


/**
 * Created by wzb on 2017/7/12 0012.
 */

public class HttpRequestDialog extends Dialog {
    private final String TAG = HttpRequestDialog.class.getSimpleName();

    private String msg = "加载中...";

    private TextView tvMsg;


    public HttpRequestDialog() {
        this(MyApplication.getAppContext());
    }

    public HttpRequestDialog(String msg) {
        this(MyApplication.getAppContext());
        this.msg = msg;
    }

    private HttpRequestDialog(@NonNull Context context) {
        this(context, R.style.HttpRequestDialogStyle);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
    }

    private HttpRequestDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_http_request);
        setCanceledOnTouchOutside(false);
        tvMsg = (TextView) this.findViewById(R.id.tv_http_request);
        tvMsg.setText(msg);
    }

}
