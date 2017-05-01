package com.up.train;

import com.jfinal.config.Routes;
import com.up.train.controller.admin.AdminController;
import com.up.train.controller.wx.WxController;
import com.up.train.controller.wx.WxSdkController;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class TrainRotes extends Routes{
    @Override
    public void config() {
        add("/admin", AdminController.class);
        add("/wx", WxController.class);
        add("/wxsdk", WxSdkController.class);
    }
}
