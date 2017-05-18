package com.up.train.controller.wx;

import com.jfinal.core.Const;
import com.jfinal.upload.UploadFile;
import com.up.common.controller.BaseController;
import com.up.common.utils.RandomUtils;
import com.up.train.model.Apply;
import com.up.train.model.Exam;
import com.up.train.model.Organization;
import org.eclipse.jetty.deploy.App;

import java.util.Date;
import java.util.List;

/**
 * TODO:微信
 * Created by yyCai
 * on 2016/10/18 0018. 20:51
 * /wx
 */
public class WxController extends BaseController{

    public void home(){
        List<Organization> organizationList = Organization.dao.getList();
        resSuccess("获取数据成功",organizationList);
    }

    /**
     * 报名
     */
    public void apply(){
        Apply apply = getModel(Apply.class,"");
        Apply apply1 = Apply.dao.findApplyingByPhone(apply);
        if (apply1!=null){
            resFailure("该手机号用户于"+apply1.getCreateTime()+"已经报名成功,请点击我已报名！");
            return;
        }
        apply.setCreateTime(new Date());
        apply.setCdKey(CDKey());
        apply.setStatus(1);
        apply.save();
        resSuccess("报名成功",apply);
    }
    private String CDKey() {
        String cdKey = RandomUtils.randomStr(6);
        if (Apply.dao.verifyCDKey(cdKey) > 0) {
            return CDKey();
        }
        return cdKey;
    }
    /**
     * 更新
     */
    public void update(){
        Apply apply = getModel(Apply.class,"",true);
        //String name = getPara("name");
        Apply apply1 = Apply.dao.findById(apply.getId());
        if(apply.getUserPhone().equals(apply1.getUserPhone())){//手机号无修改
            apply.update();
            resSuccess("修改成功");
        }
        else{
            Apply apply2 = Apply.dao.findApplyingByPhone(apply);
            if (apply2!=null){
                resFailure("该手机号用户于"+apply2.getCreateTime()+"已经报名成功");
                return;
            }
            apply.update();
            resSuccess("修改成功");
        }
    }

    /**
     * 获取报名信息列表包含培训机构名称
     */
    public void getApplyDataByPhone(){
        String phone = getPara("phone");
        List<Apply> apply = Apply.dao.findByPhone(phone);
        resSuccess(apply);
    }
    public void getApplyDataById(){
        String id = getPara("id");
        Apply apply = Apply.dao.findApplyById(id);
        resSuccess(apply);
    }
    /**
     * 获取报名信息无培训机构名称
     */
    public void getApplyDataByPhone2(){
        String phone = getPara("phone");
        Apply apply = Apply.dao.findByPhone2(phone);
        resSuccess(apply);
    }

    public void addExam(){
        Exam exam = getModel(Exam.class,"");
        exam.setTime(new Date());
        exam.save();
        resSuccess("suc");
    }
    public void getExamList(){
        resSuccess(Exam.dao.getList());
    }
}
