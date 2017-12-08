package com.gt.gtapp.bean;

import java.util.List;

/**
 * 陈丹的登录接口
 */

public class LoginBean {

    /**
     * code : 0
     * erplist : [{"item_key":"1","item_remark":"http://nb.canyin.deeptel.com.cn/menu/index.do,http://maint.deeptel.com.cn/upload/doublepm/kanxiaochu.png","item_value":"小馋猫"}]
     * msg : 登录成功
     */

    private String code;
    private String msg;
    private List<ErplistBean> erplist;

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

    public List<ErplistBean> getErplist() {
        return erplist;
    }

    public void setErplist(List<ErplistBean> erplist) {
        this.erplist = erplist;
    }

    public static class ErplistBean {
        /**
         * item_key : 1
         * item_remark : http://nb.canyin.deeptel.com.cn/menu/index.do,http://maint.deeptel.com.cn/upload/doublepm/kanxiaochu.png
         * item_value : 小馋猫
         */

        private String item_key;
        private String item_remark;
        private String item_value;

        public String getItem_key() {
            return item_key;
        }

        public void setItem_key(String item_key) {
            this.item_key = item_key;
        }

        public String getItem_remark() {
            return item_remark;
        }

        public void setItem_remark(String item_remark) {
            this.item_remark = item_remark;
        }

        public String getItem_value() {
            return item_value;
        }

        public void setItem_value(String item_value) {
            this.item_value = item_value;
        }
    }
}
