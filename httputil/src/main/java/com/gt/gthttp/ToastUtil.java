package com.gt.gthttp;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;



/**
 * Created by wzb on 2017/7/11 0011.
 */

public class ToastUtil {
    private static volatile ToastUtil sToastUtil = null;

    private Toast mToast = null;

    /**
     * 获取实例
     *
     * @return
     */
    public static ToastUtil getInstance() {
        if (sToastUtil == null) {
            synchronized (ToastUtil.class) {
                if (sToastUtil == null) {
                    sToastUtil = new ToastUtil();
                }
            }
        }
        return sToastUtil;
    }

    protected Handler handler = new Handler(Looper.getMainLooper());

    public  void showToast(final String tips){
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast(final int tips){
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast(final String tips, final int duration) {
        if (android.text.TextUtils.isEmpty(tips)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(HttpInit.getAppContext(), tips, duration);
                    mToast.show();
                } else {
                    //mToast.cancel();
                    //mToast.setView(mToast.getView());
                    mToast.setText(tips);
                    mToast.setDuration(duration);
                    mToast.show();
                }
            }
        });
    }

    public void showToast(final int tips, final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(HttpInit.getAppContext(), tips, duration);
                    mToast.show();
                } else {
                    //mToast.cancel();
                    //mToast.setView(mToast.getView());
                    mToast.setText(tips);
                    mToast.setDuration(duration);
                    mToast.show();
                }
            }
        });
    }
    public  void showNewShort(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
                     Toast.makeText(HttpInit.getAppContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
