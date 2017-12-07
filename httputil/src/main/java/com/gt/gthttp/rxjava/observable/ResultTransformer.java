package com.gt.gthttp.rxjava.observable;


import android.text.TextUtils;


import com.gt.gthttp.HttpResponseException;
import com.gt.gthttp.retrofit.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class ResultTransformer {

    /**
     * 正常格式流程
     */
    public static <T> ObservableTransformer<BaseResponse<T>, T> transformer() {
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                    return upstream
                        .flatMap(ResultTransformer.<T>flatMap())
                        .compose(SchedulerTransformer.<T>transformer());
            }
        };
    }

    public static <T> Function<BaseResponse<T>, ObservableSource<T>> flatMap() {
        return new Function<BaseResponse<T>, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(@NonNull final BaseResponse<T> tBaseResponse) throws Exception {
                return new Observable<T>() {
                    @Override
                    protected void subscribeActual(Observer<? super T> observer) {
                        if (tBaseResponse.isSuccess()) {
                            if (tBaseResponse.getData()!=null){//data为null时不调用 onSuccess  并且不返回原始数据
                                observer.onNext(tBaseResponse.getData());
                            }
                            observer.onComplete();
                        } else {
                            String msg="";
                            if (!TextUtils.isEmpty(tBaseResponse.getMsg())){
                                msg=tBaseResponse.getMsg();
                            }
                            observer.onError(new HttpResponseException(tBaseResponse.getCode(),msg));//这个msg让Observer处理
                        }
                    }
                };
            }
        };
    }

    /**
     * 预处理后返回BaseResponse
     * @return
     */
    public static ObservableTransformer<BaseResponse,BaseResponse> transformerBaseResponse(){
        return new ObservableTransformer<BaseResponse, BaseResponse>() {

            @Override
            public ObservableSource<BaseResponse> apply(@NonNull final Observable<BaseResponse> upstream) {
                return upstream
                        .flatMap(ResultTransformer.<BaseResponse>flatMapResponse())
                        .compose(SchedulerTransformer.<BaseResponse>transformer());
            }
        };
    }

    private static Function<BaseResponse,ObservableSource<BaseResponse>> flatMapResponse(){
        return new Function<BaseResponse, ObservableSource<BaseResponse>>() {
            @Override
            public ObservableSource<BaseResponse> apply(@NonNull final BaseResponse baseResponse) throws Exception {
                return new Observable<BaseResponse>() {
                    @Override
                    protected void subscribeActual(Observer<? super BaseResponse> observer) {
                        if (baseResponse.isSuccess()) {
                            observer.onNext(baseResponse);
                            observer.onComplete();
                        } else {
                            String msg="";
                            if (!TextUtils.isEmpty(baseResponse.getMsg())){
                                msg=baseResponse.getMsg();
                            }
                            observer.onError(new HttpResponseException(baseResponse.getCode(),msg));//这个msg让Observer处理
                        }
                    }
                };
            }
        };
    }
}
