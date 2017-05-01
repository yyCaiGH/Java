package com.up.common.controller;

import com.up.common.def.ResCode;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/9 0009. 17:04
 */
public class ErrorController extends BaseController{

    public void error(){
        res(ResCode.PARAM_ERROR);
    }
}
