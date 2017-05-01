package com.up.infant.controller.admin;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.User;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/2 0002. 23:09
 */
public class AdminController extends BaseController{
    public void login(){
        String name=getPara("name");
        String password=getPara("password");
        User user=User.dao.isLogin(name,password);
        if (null!=user){
            res(ResCode.SUCCESS);
            setSessionAttr("loginUser", user);
        }else {
            res(ResCode.PP_ERROR);
        }
    }
    public void index1(){
        render("index.html");
    }
}
