package com.up.infant.controller.message;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.common.base.Verify;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.MemberMessage;
import com.up.infant.model.SysMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/24 0024. 14:26
 */
public class MessageController extends BaseController {
    MessageLogic logic=new MessageLogic();

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
        logic.messageAddToMember(message.getId());
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
    public void  getPageList(){
        String[] iParams={"pageSize","pageNo"};
        Verify<Map<String,Integer>> verify =intVerify(iParams);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        Page<SysMessage> page=new SysMessage().getPageList(verify.getData().get("pageSize"),verify.getData().get("pageNo"));
        resSuccess(page);
    }

    /**
     * 区分系统和推送
     */
    public void  getPageListByType(){
        String[] iParams={"pageSize","pageNo","type"};
        Verify<Map<String,Integer>> verify =intVerify(iParams);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        Page<SysMessage> page=new SysMessage().getPageList(verify.getData().get("pageSize"),verify.getData().get("pageNo"),verify.getData().get("type"));
        resSuccess(page);
    }

    public void getMemberMessage(){
        ComParams params=getBean(ComParams.class,"");
        Page<MemberMessage> page=logic.getMemberMessage(params);
        resSuccess(page);
    }

    public void MessageIsRead(){
        int id=getParaToInt("id");
        boolean result=logic.msgIsRead(id);
        if (result){
            res(ResCode.SUCCESS);
        }else {
            res(ResCode.ERROR);
        }
    }

    public void MessageAllRead(){
        int id=getParaToInt("memberId");
        logic.allRead(id);
        res(ResCode.SUCCESS);
    }


}
