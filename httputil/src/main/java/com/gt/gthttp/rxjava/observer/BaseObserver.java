package com.gt.gthttp.rxjava.observer;


import com.gt.gthttp.HttpResponseException;
import com.gt.gthttp.Logger;
import com.gt.gthttp.ToastUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public abstract class BaseObserver<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {
            ToastUtil.getInstance().showToast("网络异常");
        } else if (e instanceof SocketTimeoutException) {  //VPN open
            ToastUtil.getInstance().showToast("服务器响应超时");
        } else if (e instanceof ConnectException) {
            ToastUtil.getInstance().showToast("连接服务器异常");
        } else if (e instanceof UnknownHostException) {
            ToastUtil.getInstance().showToast("无网络连接，请检查网络是否开启");
        } else if (e instanceof UnknownServiceException) {
            ToastUtil.getInstance().showToast("未知的服务器错误");
        } else if (e instanceof IOException) {  //飞行模式等
            ToastUtil.getInstance().showToast("连接服务器异常");
        }else if(e instanceof HttpResponseException){//自定义异常 状态码等
            HttpResponseException  responseException = (HttpResponseException) e;
            onFailed(responseException);
        }else {//(e instanceof RuntimeException)
            Logger.e("HTTP","程序异常"+e.getMessage());
            ToastUtil.getInstance().showToast("后台数据有误！");
        }

    }

    /**
     * 若没有data数据执行该方法
     */
    @Override
    public void onComplete() {
    }

    //非必须重写  兼容没有data的接口
    protected void onSuccess(T t){

    }

    /**
     * 简单提示 服务器返回信息 若需要处理 则重写
     */
    protected void onFailed(HttpResponseException responseException) {
        ToastUtil.getInstance().showNewShort(responseException.getMessage());
    }
}
