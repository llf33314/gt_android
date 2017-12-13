package com.gt.gtapp.bean;

import java.util.List;

/**
 * 陈丹的登录接口
 */

public class HttpCodeMsgBean {

    /**
     * code : 0
     * erplist : [{"item_key":"1","item_remark":"http://nb.canyin.deeptel.com.cn/menu/index.do,http://maint.deeptel.com.cn/upload/doublepm/kanxiaochu.png","item_value":"小馋猫"}]
     * msg : 登录成功
     */

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
