package com.gt.gtapp.bean;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public class BossAccountBean {

    /**
     * expireDate : 2017-12-08T08:13:42.377Z
     * expireDay : 0
     * fanbiNum : 0
     * flowNum : 0
     * name : string
     * smsNum : 0
     * version : string
     * versionCode : string
     */



    private String expireDate;
    private int expireDay;
    private double fanbiNum;
    private int flowNum;
    private String name;
    private int smsNum;
    private String version;
    private String versionCode;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(int expireDay) {
        this.expireDay = expireDay;
    }

    public double getFanbiNum() {
        return fanbiNum;
    }

    public void setFanbiNum(double fanbiNum) {
        this.fanbiNum = fanbiNum;
    }

    public int getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(int flowNum) {
        this.flowNum = flowNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(int smsNum) {
        this.smsNum = smsNum;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
