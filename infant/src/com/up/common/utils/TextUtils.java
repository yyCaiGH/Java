package com.up.common.utils;

/**
 * TODO:字符串工具类
 * Created by 王剑洪
 * on 2016/10/21 0021. 21:57
 */
public class TextUtils {
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (null==str||str.trim().length()<=0){
            return true;
        }
        return false;
    }

    public static boolean isBigEmpty(String str){
        if (null==str||str.trim().length()<=0||"null".equals(str)){
            return true;
        }
        return false;
    }
    public static boolean isInteger(String str){
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static Integer toInteger(String str){
        Integer  i = null;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
        return i;
    }

    public static boolean isLong(String str){
        try {
            Long.parseLong(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static Long toLong(String str){
        Long  i = null;
        try {
            i = Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
        return i;
    }
    public static boolean isDouble(String str){
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static Double toDouble(String str){
        Double  i = null;
        try {
            i = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
        return i;
    }
}
