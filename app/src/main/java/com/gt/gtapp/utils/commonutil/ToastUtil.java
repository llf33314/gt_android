package com.gt.gtapp.utils.commonutil;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.gtapp.base.MyApplication;

import org.w3c.dom.Text;


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


    public void showToast(final String tips, final int duration) {
        if (android.text.TextUtils.isEmpty(tips)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(MyApplication.getAppContext(), tips, duration);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    TextView tv = getToastTextView(mToast.getView());
                    if (tv!=null){
                        tv.setGravity(Gravity.CENTER);
                    }

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

    /**
     * 只获取第一个TextView
     */
    private TextView getToastTextView(View view){
            if (view instanceof TextView){
                return (TextView) view;
            }
            if (view instanceof ViewGroup){
                ViewGroup viewGroup= (ViewGroup) view;
                int viewCount=viewGroup.getChildCount();
                for (int i=0;i<viewCount;i++){
                    View childView=viewGroup.getChildAt(i);
                    if (childView instanceof TextView){
                        return (TextView) childView;
                    }
                    if (childView instanceof ViewGroup){
                        return getToastTextView( childView);
                    }
                }
            }else{
                return null;
            }
            return null;
    }


    public  void showNewShort(final String msg){
        handler.post(new Runnable() {
            @Override
            public void run() {
               Toast t= Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                TextView tv = getToastTextView(t.getView());
                if (tv!=null){
                    tv.setGravity(Gravity.CENTER);
                }
                t.show();
            }
        });
    }
}
