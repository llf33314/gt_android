package com.gt.gtapp.http.rxjava.observable;


import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;

import com.gt.gtapp.base.MyApplication;
import com.gt.gtapp.bean.LoginAccountBean;
import com.gt.gtapp.bean.ShowLoginUiMsg;
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.http.HttpResponseException;
import com.gt.gtapp.http.retrofit.BaseResponse;
import com.gt.gtapp.http.retrofit.HttpCall;
import com.gt.gtapp.http.rxjava.RxBus;
import com.gt.gtapp.http.rxjava.observer.BaseObserver;
import com.gt.gtapp.login.LoginActivity;
import com.gt.gtapp.login.LoginHelper;
import com.gt.gtapp.login.StaffListIndustryActivity;
import com.gt.gtapp.main.MainActivity;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

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
                        String msg="";
                        if (!TextUtils.isEmpty(tBaseResponse.getMsg())){
                            msg=tBaseResponse.getMsg();
                        }
                        if (tBaseResponse.isSuccess()) {
                            if (tBaseResponse.getData()!=null){//data为null时不调用 onSuccess  并且不返回原始数据
                                observer.onNext(tBaseResponse.getData());
                            }
                            observer.onComplete();
                        } else  if(tBaseResponse.isTokenPast()){
                            //当前登录页面返回session过期 就是共享失败  判断是否缓存账号密码  有则自动刷新session 没有就调转到登录页面
                            //如果刷新session失败了可能是密码改了 则也是跳转到登录页面 注意一下这里的对话框还是跟原来的一样就是多请求了俩个接口

                            if (LoginHelper.isSaveAccountPsd()){
                                //里面会处理好账号密码错误等
                                LoginHelper.getNewSession((String)Hawk.get(LoginHelper.ACCOUNT_KEY),(String)Hawk.get(LoginHelper.PSD_KEY))
                                        .flatMap(ResultTransformer.<LoginAccountBean>flatMap())
                                        .flatMap(new Function<LoginAccountBean, ObservableSource<BaseResponse<List<StaffListIndustryBean>>>>() {
                                            @Override
                                            public ObservableSource<BaseResponse<List<StaffListIndustryBean>>> apply(@NonNull LoginAccountBean loginAccountBean) throws Exception {
                                                MyApplication.setAccountType(loginAccountBean.getAccountType());
                                                //老板账号
                                                if (loginAccountBean.getAccountType()==1){
                                                    String duoFriendUrl=loginAccountBean.getHomeUrl();

                                                    Intent intent=new Intent(MyApplication.getCurrentActivity(),MainActivity.class);
                                                    //清除所有activity
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("url",duoFriendUrl);
                                                    MyApplication.getCurrentActivity().startActivity(intent);

                                                    return Observable.error(new HttpResponseException(HttpResponseException.SUCCESS_BREAK,""));
                                                }else{
                                                    //员工账号  老板账号直接跳转到网页  员工账号再请求接口或者erp
                                                    return HttpCall.getApiService().staffListIndustry();
                                                }
                                            }
                                        })
                                        .compose(ResultTransformer.<List<StaffListIndustryBean>>transformer())
                                        .subscribe(new BaseObserver<List<StaffListIndustryBean>>() {
                                            @Override
                                            protected void onSuccess(List<StaffListIndustryBean> staffListIndustryBeanList) {
                                                //获取到员工账号的列表
                                                Intent intent=new Intent(MyApplication.getCurrentActivity(),StaffListIndustryActivity.class);
                                                intent.putParcelableArrayListExtra("staffListIndustryList", (ArrayList<? extends Parcelable>) staffListIndustryBeanList);
                                                MyApplication.getCurrentActivity().startActivity(intent);
                                            }

                                            @Override
                                            protected void onFailed(HttpResponseException responseException) {
                                                super.onFailed(responseException);
                                                LoginHelper.clearAccountInfo();
                                                if (!LoginHelper.loginUiIsShow){
                                                    RxBus.get().post(new ShowLoginUiMsg());
                                                }
                                            }
                                        });

                            }else{
                                Intent intent=new Intent(MyApplication.getAppContext(), LoginActivity.class);
                                if (MyApplication.getCurrentActivity().getClass().equals(LoginActivity.class)){
                                    observer.onError(new HttpResponseException(tBaseResponse.getCode(),msg));//这个msg让Observer处理
                                }else{
                                    MyApplication.getAppContext().startActivity(intent);
                                }
                            }
                        }else{
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
                        String msg="";
                        if (!TextUtils.isEmpty(baseResponse.getMsg())){
                            msg=baseResponse.getMsg();
                        }
                        if (baseResponse.isSuccess()) {
                            observer.onNext(baseResponse);
                            observer.onComplete();
                        } else if(baseResponse.isTokenPast()){
                            //里面会处理好账号密码错误等
                            LoginHelper.getNewSession((String)Hawk.get(LoginHelper.ACCOUNT_KEY),(String)Hawk.get(LoginHelper.PSD_KEY))
                                    .flatMap(ResultTransformer.<LoginAccountBean>flatMap())
                                    .flatMap(new Function<LoginAccountBean, ObservableSource<BaseResponse<List<StaffListIndustryBean>>>>() {
                                        @Override
                                        public ObservableSource<BaseResponse<List<StaffListIndustryBean>>> apply(@NonNull LoginAccountBean loginAccountBean) throws Exception {
                                            MyApplication.setAccountType(loginAccountBean.getAccountType());
                                            //老板账号
                                            if (loginAccountBean.getAccountType()==1){
                                                String duoFriendUrl=loginAccountBean.getHomeUrl();

                                                Intent intent=new Intent(MyApplication.getCurrentActivity(),MainActivity.class);
                                                //清除所有activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("url",duoFriendUrl);
                                                MyApplication.getCurrentActivity().startActivity(intent);

                                                return Observable.error(new HttpResponseException(HttpResponseException.SUCCESS_BREAK,""));
                                            }else{
                                                //员工账号  老板账号直接跳转到网页  员工账号再请求接口或者erp
                                                return HttpCall.getApiService().staffListIndustry();
                                            }
                                        }
                                    })
                                    .compose(ResultTransformer.<List<StaffListIndustryBean>>transformer())
                                    .subscribe(new BaseObserver<List<StaffListIndustryBean>>() {
                                        @Override
                                        protected void onSuccess(List<StaffListIndustryBean> staffListIndustryBeanList) {
                                            //获取到员工账号的列表
                                            Intent intent=new Intent(MyApplication.getCurrentActivity(),StaffListIndustryActivity.class);
                                            intent.putParcelableArrayListExtra("staffListIndustryList", (ArrayList<? extends Parcelable>) staffListIndustryBeanList);
                                            MyApplication.getCurrentActivity().startActivity(intent);
                                        }

                                        @Override
                                        protected void onFailed(HttpResponseException responseException) {
                                            super.onFailed(responseException);
                                            LoginHelper.clearAccountInfo();
                                            if (!LoginHelper.loginUiIsShow){
                                                RxBus.get().post(new ShowLoginUiMsg());
                                            }
                                        }
                                    });
                        }else{
                            observer.onError(new HttpResponseException(baseResponse.getCode(),msg));//这个msg让Observer处理
                        }
                    }
                };
            }
        };
    }
}
