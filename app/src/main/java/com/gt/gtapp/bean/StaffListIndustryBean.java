package com.gt.gtapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public class StaffListIndustryBean implements Parcelable{

    /**
     * code : string
     * name : string
     * status : 0
     * url : string
     */

    private String code;
    private String name;
    private int status;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeString(this.url);
    }

    public StaffListIndustryBean() {
    }

    protected StaffListIndustryBean(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.status = in.readInt();
        this.url = in.readString();
    }

    public static final Creator<StaffListIndustryBean> CREATOR = new Creator<StaffListIndustryBean>() {
        @Override
        public StaffListIndustryBean createFromParcel(Parcel source) {
            return new StaffListIndustryBean(source);
        }

        @Override
        public StaffListIndustryBean[] newArray(int size) {
            return new StaffListIndustryBean[size];
        }
    };
}
