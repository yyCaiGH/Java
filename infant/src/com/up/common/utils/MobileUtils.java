package com.up.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO:手机工具类
 * <p>
 * Created by 王剑洪 on 2016/10/31.
 */
public class MobileUtils {

    /**
     * 是否手机号
     * @param phone
     * @return
     */
    public static Boolean is(String phone){
        String regExp = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }
}
