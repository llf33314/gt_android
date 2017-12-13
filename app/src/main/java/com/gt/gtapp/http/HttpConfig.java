package com.gt.gtapp.http;

/**
 * Created by wzb on 2017/12/5 0005.
 */

public class HttpConfig {
    // 本地
    //public static String BASE_RUL="http://192.168.3.98:8401";
    // 测试
    public static String BASE_RUL="https://app.deeptel.com.cn";

    //陈丹erp登录接口
    //测试
    // public static final String ERP_LOGIN_URL="https://deeptel.com.cn/ErpMenus/79B4DE7C/DoubleErplogin.do";
     public static final String ERP_LOGIN_URL="https://deeptel.com.cn/ErpMenus/79B4DE7C/Erplogin.do";
    //堡垒
    // public static final String ERP_LOGIN_URL="https://nb.deeptel.com.cn/ErpMenus/79B4DE7C/DoubleErplogin.do";
    //正式
    //public static final String ERP_LOGIN_URL="https://duofriend.com/ErpMenus/79B4DE7C/DoubleErplogin.do";

    public static final String ERP_LOGIN_OUT_URL="https://deeptel.com.cn/ErpMenus/79B4DE7C/ErpEmptySession.do";
}
