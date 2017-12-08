package com.gt.gtapp.http;



public class HttpResponseException extends RuntimeException {

    /**
     * 用于多接口请求 退出链表 自己提示
     */
    public static int SUCCESS_BREAK=555;

    private  int code;

    public HttpResponseException(int code,String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
