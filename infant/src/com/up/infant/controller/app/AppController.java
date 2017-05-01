package com.up.infant.controller.app;

import com.google.gson.Gson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.Coupon;
import com.up.infant.model.CustomPage;
import com.up.infant.model.Exp;
import com.up.infant.model.gen.LogisticsBean;
import com.up.infant.model.gen.LogisticsData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/10 0010. 15:15
 */
public class AppController extends BaseController{
    AppLogic logic=new AppLogic();

    public void page(){
        int pageId=getParaToInt("pageId");
        CustomPage page=logic.pageInfo(pageId);
        if (null==page){
            res(ResCode.PAGE_NULL);
        }else {
            resSuccess(page);
        }
    }

    public void coupon(){
        int couponId=getParaToInt("couponId");
        Coupon coupon=logic.couponInfo(couponId);
        if (null==coupon){
            res(ResCode.COUPON_NULL);
        }else {
            resSuccess(coupon);
        }
    }

    public void getCoupon(){
        int couponId=getParaToInt("couponId");
        int memberId=getParaToInt("memberId");
        res(logic.getCoupon(memberId,couponId));

    }

    public void getMemberRebates(){
        int memberId=getParaToInt("memberId");
        resSuccess(logic.getMemberRebates(memberId));

    }

//    public void test(){
//        String json= PropKit.get("JSON");
//        LogisticsBean logisticsBean=new Gson().fromJson(json,LogisticsBean.class);
//        List<LogisticsData> list=logisticsBean.getShowapi_res_body().getExpressList();
//        List<Exp> exps=new ArrayList<Exp>();
//        for (int i=0;i<list.size();i++){
//            Exp exp=new Exp();
//            LogisticsData data=list.get(i);
//            exp.setImgUrl(data.getImgUrl());
//            exp.setSimpleName(data.getSimpleName());
//            exp.setPhone(data.getPhone());
//            exp.setExpName(data.getExpName());
//            exp.setUrl(data.getUrl());
//            exp.setNote(data.getNote());
//            exps.add(exp);
//        }
//        Db.batchSave(exps,exps.size());
//    }





}
