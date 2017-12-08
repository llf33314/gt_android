package com.gt.gtapp.login;

/**
 * Created by wzb on 2017/12/8 0008.
 */

public enum IndustryEnums {

    EAT("1", "康小厨"),
    CAR("2", "车小算"),
    LOOK("3", "样子"),
    PROPERTY("5", "揽胜家园"),
    MEDICAL("6", "小红十"),
    SHOP("7", "翼店"),
    STOCK("8", "敏库"),
    HOTEL("9", "客来驿"),
    EDUCATION("11", "小盼"),

    ERROR1("4", ""),
    ERROR2("10", ""),
    ;

    private final String code;
    private final String desc;

    IndustryEnums(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
