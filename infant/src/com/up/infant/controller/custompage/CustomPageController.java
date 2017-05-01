package com.up.infant.controller.custompage;

import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.controller.BaseController;
import com.up.common.utils.TextUtils;
import com.up.infant.model.CustomPage;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO:自定义单页
 * Created by 王剑洪
 * on 2016/10/21 0021. 21:33
 */
public class CustomPageController extends BaseController {

    /**
     * 参数验证
     * @return
     */
    private Verify<CustomPage> complete() {
        Verify<CustomPage> customPageVerify = new Verify<CustomPage>();
        String title = getPara("title");
        String note = getPara("note");
        String content = getPara("content");
        String state = getPara("state");
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(note) || TextUtils.isEmpty(content) || TextUtils.isEmpty(state)) {//判断信息是否完善
            customPageVerify.setResCode(ResCode.INFO_FULL);
            return customPageVerify;
        }
        int isShow = 0;
        try {
            isShow = Integer.parseInt(state);
        } catch (Exception e) {
            customPageVerify.setResCode(ResCode.PARAM_FORMAT);
            return customPageVerify;
        }
        if (isShow != 1 && isShow != 0) {//isShow,0显示，1隐藏
            customPageVerify.setResCode(ResCode.PARAM_FORMAT);
            return customPageVerify;
        }
        customPageVerify.setResCode(ResCode.SUCCESS);
        CustomPage customPage = new CustomPage();
        customPage.setContent(content);
        customPage.setTitle(title);
        customPage.setNote(note);
        customPage.setIsShow(isShow);
        customPage.setCreateTime(new Date());
        customPageVerify.setData(customPage);
        return customPageVerify;
    }


    public void add() {
        try{
            CustomPage page=getModel(CustomPage.class,"");
            page.setCreateTime(new Date());
            page.save();
            res(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }

    }

    public void updatePage() {
        try{
            CustomPage page=getModel(CustomPage.class,"");
            page.update();
            res(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }

    }

    public void getList(){
        try{
            List<CustomPage> postageGroups=CustomPage.dao.getList();
            resSuccess(postageGroups);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void update() {
        String[] params1 = {"id"};
        Verify<Map<String,Integer>> verify =intVerify(params1);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        Verify<CustomPage> customPageVerify = complete();
        if (customPageVerify.getCode()<0){
            resFailure(customPageVerify.getMsg());
        }else {
            CustomPage customPage= customPageVerify.getData();
            customPage.setId(verify.getData().get(params1[0]));
            customPageVerify.getData().update();
            resSuccess(ResCode.UPDATE_SUCCESS.getMsg());
        }
    }

    public void info(){
        String[] params1 = {"id"};
        Verify<Map<String,Integer>> verify =intVerify(params1);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        CustomPage customPage=new CustomPage().getPageInfo(verify.getData().get(params1[0]));
        resSuccess(ResCode.SUCCESS.getMsg(),customPage);
    }

    public void specifyInfo(){
        String title  = getPara("title");
        CustomPage customPage=new CustomPage().specifyInfo(title);
        if (null==customPage){
            customPage=new CustomPage();
            customPage.setTitle(title);
            customPage.setNote(title);
            customPage.setIsShow(0);
            customPage.setType(0);
            customPage.setContent("页面建设中。。。");
            customPage.setCreateTime(new Date());
        }
        resSuccess(ResCode.SUCCESS.getMsg(),customPage);
    }

    public void del(){
        String[] params = {"id"};
        Verify<Map<String,Integer>> verify =intVerify(params);
        if (verify.getCode()<0){
            resFailure(verify.getMsg());
            return;
        }
        CustomPage customPage=new CustomPage().getPageInfo(verify.getData().get(params[0]));
        if (null==customPage){
            resFailure(ResCode.PARAM_FORMAT.getMsg());
            return;
        }
        customPage.delete();
        resSuccess(ResCode.DEL_SUCCESS.getMsg());
    }

    public void getUserAgreement(){
        CustomPage page=getModel(CustomPage.class,"");

    }

}
