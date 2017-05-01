package com.up.train.utils;

import sun.applet.Main;

/**
 * Created by Administrator on 2017/4/25 0025.
 */
public class Test {
    public static final void main(String [] s){
        String url = "http://cyy.tunnel.qydev.com/index-wx.html#/access/signin";
        if(url.contains("#")){
            url = url.substring(0,url.indexOf("#"));
        }
        System.out.println(url);
    }
}
