package com.up.common.utils;

import com.jfinal.kit.PropKit;

/**
 * TODO:日志工具类
 * Created by 王剑洪
 * on 2016/10/18 0018. 21:58
 */
public class Logger {

    public static boolean isDevMode=true;

    public static void i(String tag,Object o){
        if (!isDevMode){
            return;
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("【"+tag+"】\n"+o.toString());
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    public static void e(String tag,Object o){
        if (!isDevMode){
            return;
        }
        System.err.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.err.println("【"+tag+"】\n"+o.toString());
        System.err.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
