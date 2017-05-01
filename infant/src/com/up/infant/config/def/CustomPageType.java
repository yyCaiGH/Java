package com.up.infant.config.def;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/21 0021. 21:36
 */
public enum CustomPageType {
    COMMON(0,"普通单页"),ABOUT_US(1,"关于我们"),SALES_RETURN(2,"退货说明"),HELP(3,"帮助中心");
    private int code;
    private String name;

    CustomPageType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
