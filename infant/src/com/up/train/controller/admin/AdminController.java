package com.up.train.controller.admin;

import com.jfinal.core.Const;
import com.jfinal.upload.UploadFile;
import com.up.common.controller.BaseController;
import com.up.train.model.Apply;
import com.up.train.model.Organization;
import org.eclipse.jetty.deploy.App;

import java.util.Date;
import java.util.List;

/**
 * TODO:后台
 * Created by yyCai
 * on 2016/10/18 0018. 20:51
 * /admin
 */
public class AdminController extends BaseController{

    public void login(){
        Organization org = getModel(Organization.class,"");
        Organization org1 = Organization.dao.findByAccount(org.getAccount());
        if(org1==null){
            resFailure("无此账号");
        }
        else if (org1.getPassword().equals(org.getPassword())){
            resSuccess("登陆成功",org1);
        }
        else{
            resFailure("密码错误");
        }
    }

    /**
     * 获取培训机构对应的报名用户列表
     */
    public void getOrgApplyUser(){
        String id = getPara("id");
        List<Apply> applyList = Apply.dao.findByOrgId(id);
        resSuccess(applyList);
    }
    /**
     * 获取所有的报名用户列表
     */
    public void getAllApply(){
        Apply apply = getModel(Apply.class,"");
        List<Apply> applyList = Apply.dao.findAllApply(apply);
        resSuccess(applyList);
    }
    public void getAllOrg(){
        List<Organization> organizationList = Organization.dao.getList();
        resSuccess(organizationList);
    }
    public void updatePsw(){
        String password = getPara("password");
        String id = getPara("id");
        Organization org = Organization.dao.findById(id);
        org.setPassword(password);
        org.update();
        resSuccess("更新密码成功");
    }
    public void updateStatus(){
        int status = Integer.parseInt(getPara("status"));
        String id = getPara("id");
        Apply apply = Apply.dao.findById(id);
        apply.setStatus(status);
        apply.update();
        resSuccess("操作成功");
    }

    public void checkCdKey(){
        String cdKey = getPara("cd_key");
        String id = getPara("id");
        Apply apply = Apply.dao.findById(id);
        if (cdKey.equalsIgnoreCase(apply.getCdKey())){
            apply.setPayTime(new Date());
            apply.setOrgSurePay(true);
            apply.setStatus(2);
            apply.update();
            resSuccess("验证成功");
            return;
        }
        else{
            resFailure("验证失败");
        }
    }
}
