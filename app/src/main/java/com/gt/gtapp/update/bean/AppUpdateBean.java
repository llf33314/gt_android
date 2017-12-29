package com.gt.gtapp.update.bean;

import java.io.Serializable;

/**
 * Description:
 * Created by jack-lin on 2017/8/23 0023.
 */

public class AppUpdateBean implements Serializable{
    public String appVersionCode="";
    public String appVersionName="";
    public String resultFlag="";
    public String apkUrl="";
    public String remarks="0";//0-不需要重新登录，1-需要重新登录

}
