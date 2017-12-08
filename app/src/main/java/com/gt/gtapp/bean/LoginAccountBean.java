package com.gt.gtapp.bean;

/**
 * Created by wzb on 2017/12/7 0007.
 *
 * 登录以后获取账号erp信息
 */

public class LoginAccountBean {

    /**
     * accountType : 0
     * homeUrl : string
     */

    /**
     * 0 是员工  1 是老板
     */
    private int accountType;

    private String homeUrl;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }
}
