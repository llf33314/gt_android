package com.gt.gtapp.http;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**
 * Description:
 * Created by jack-lin on 2017/9/29 0029.
 * Buddha bless, never BUG!
 */

public class LoadingProgressDialog extends SVProgressHUD{
    public LoadingProgressDialog(Context context) {
        super(context);
        this.showWithStatus("加载中...");
    }
    public LoadingProgressDialog(Context context, String text) {
        super(context);
        this.showWithStatus(text);
    }
}
