package com.up.infant.controller;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.Verify;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.SysMessage;
import java.util.Date;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/24 0024. 14:26
 */
public class MessageController extends BaseController {


    public void add() {
        String[] params = {"title", "content"};
        Verify<Map<String, String>> verify = strVerify(params);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        SysMessage message=new SysMessage();
        message.setTitle(verify.getData().get(params[0]));
        message.setContent(verify.getData().get(params[1]));
        message.setCreateTime(new Date());
        message.save();
        resSuccess(ResCode.SAVE_SUCCESS.getMsg());
    }

    public void update(){
        String[] params1 = {"id"};
        Verify<Map<String,Integer>> verify1 =intVerify(params1);
        if (verify1.getCode()<0){
            resFailure(verify1.getMsg());
            return;
        }
        String[] params = {"title", "content"};
        Verify<Map<String, String>> verify = strVerify(params);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        SysMessage message=new SysMessage();
        message.setId(verify1.getData().get(params1[0]));
        message.setTitle(verify.getData().get(params[0]));
        message.setContent(verify.getData().get(params[1]));
        message.setCreateTime(new Date());
        message.update();
        resSuccess(ResCode.UPDATE_SUCCESS.getMsg());
    }

    public void info(){
        String[] params1 = {"id"};
        Verify<Map<String,Integer>> verify =intVerify(params1);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        SysMessage message=new SysMessage().getInfo(verify.getData().get(params1[0]));
        resSuccess(message);
    }

    public void del(){
        String[] params1 = {"id"};
        Verify<Map<String,Integer>> verify =intVerify(params1);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        SysMessage message=new SysMessage().getInfo(verify.getData().get(params1[0]));
        message.delete();
        resSuccess(ResCode.DEL_SUCCESS.getMsg());
    }

    public void  getList(){
        String[] iParams={"pageSize","pageNo"};
        Verify<Map<String,Integer>> verify =intVerify(iParams);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        Page<SysMessage> page=new SysMessage().getList(verify.getData().get("pageSize"),verify.getData().get("pageNo"));
        resSuccess(page);

    }

}
