package com.up.infant.controller;

import com.tencent.xinge.XingeApp;
import com.up.common.controller.BaseController;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/24 0024. 16:29
 */
public class XinGeController extends BaseController{

    public void push(){

    }

    public static void main(String[] args) {
//        System.out.println(XingeApp.pushTokenAndroid(000, "secretKey", "test", "测试", "token"));

        System.out.println( XingeApp.pushAccountAndroid(2100239950,"72e129adb42a85e80b976d8785f2b41e" , "test", "测试", ""));
    }
}
