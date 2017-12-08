
package com.gt.gtapp.http.rxjava.observable;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.http.LoadingProgressDialog;
import com.gt.gtapp.utils.commonutil.AppManager;

import android.os.Handler;
import android.os.Looper;



import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class DialogTransformer {

    //这个对话框、网络加载能否取消  默认能
    private boolean cancelable=true;

    public DialogTransformer() {
        this(true);
    }

    public DialogTransformer(boolean cancelable) {
        this.cancelable = cancelable;
    }

    private LoadingProgressDialog showDialog(final Disposable disposable){
        LoadingProgressDialog  httpRequestDialog = new LoadingProgressDialog(MyApplication.getCurrentActivity());
        if (cancelable) {
            httpRequestDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(SVProgressHUD svProgressHUD) {
                    disposable.dispose();
                }
            });
        }
        httpRequestDialog.show();
        return httpRequestDialog;
    }

    public <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T, T>() {
            private LoadingProgressDialog httpRequestDialog;
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {

                return  upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull final Disposable disposable) throws Exception {

                        if (Looper.myLooper()!=Looper.getMainLooper()){
                            Handler handler=new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    httpRequestDialog=showDialog(disposable);
                                }
                            });
                        }else{
                            httpRequestDialog=showDialog(disposable);
                        }
                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (httpRequestDialog.isShowing()) {
                            httpRequestDialog.dismiss();
                        }
                    }
                });
            }
        };
    }
}


/*
package com.gt.gtapp.http.rxjava.observable;

import android.content.DialogInterface;

import com.gt.gtapp.http.HttpRequestDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class DialogTransformer {

    //这个对话框、网络加载能否取消  默认能
    private boolean cancelable=true;

    public DialogTransformer() {
        this(true);
    }

    public DialogTransformer(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T, T>() {
            private HttpRequestDialog httpRequestDialog;
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {

                return  upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull final Disposable disposable) throws Exception {
                        httpRequestDialog = new HttpRequestDialog();
                        httpRequestDialog.show();
                        if (cancelable) {
                            httpRequestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    disposable.dispose();
                                }
                            });
                        }
                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (httpRequestDialog.isShowing()) {
                            httpRequestDialog.cancel();
                        }
                    }
                });
            }
        };
    }
}
*/
