package com.gt.gtapp.http;

import com.gt.gtapp.bean.BossAccountBean;
import com.gt.gtapp.bean.LoginAccountBean;
import com.gt.gtapp.bean.SignBean;
import com.gt.gtapp.bean.StaffAccountBean;
import com.gt.gtapp.bean.StaffListIndustryBean;
import com.gt.gtapp.http.retrofit.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wzb on 2017/12/5 0005.
 */

public  interface ApiService {
    @FormUrlEncoded
    @POST("m/manage/login/getSign")
    Observable<BaseResponse<SignBean>> getSign(@Field("loginName")String account, @Field("password")String psd);

    @FormUrlEncoded
    @POST(HttpConfig.ERP_LOGIN_URL)
    Observable<String> erpLogin(@Field("login_name")String account, @Field("password")String psd, @Field("sign")String sign);

    @POST("app/manage/bus/getLoginAccount")
    Observable<BaseResponse<LoginAccountBean>> getLoginAccount();

    @POST("app/manage/bus/listIndustry")
    Observable<BaseResponse<List<StaffListIndustryBean>>> staffListIndustry();

    @POST("app/manage/bus/getAccountInfo")
    Observable<BaseResponse<BossAccountBean>> bossAccountInfo();

    @POST("app/manage/bus/getAccountInfo")
    Observable<BaseResponse<StaffAccountBean>> staffAccountInfo();

    @POST(HttpConfig.ERP_LOGIN_OUT_URL)
    Observable<String> erpLoginOut();

}
