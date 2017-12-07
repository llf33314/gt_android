package com.gt.gthttp.retrofit;

/**
 * 接口数据类型
 */
public class BaseResponse<T> {

    public static final int SUCCESS_STATUS = 0;

    private int code;
    private T data;
    private String msg="";

    public boolean isSuccess() {
        return  code == SUCCESS_STATUS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
