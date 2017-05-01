package com.up.infant.controller.coupon;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.controller.BaseController;
import com.up.common.utils.DateUtils;
import com.up.common.utils.SmsParm;
import com.up.common.utils.SmsUtils;
import com.up.infant.config.MIConfig;
import com.up.infant.model.Coupon;
import com.up.infant.model.Member;
import com.up.infant.model.MemberCoupon;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO:优惠券
 * Created by 王剑洪
 * on 2016/10/24 0024. 21:24
 */
public class CouponController extends BaseController {

    private CouponLogic logic = new CouponLogic();
    public void addAutoCoupon() {
        //【标题】
        //【优惠券类型扩展字段,type=0(表示折扣数)1(表示抵用金额)2(表示满？减？，格式100-10)3(为空)】
        //【说明】
        String[] strParams = {"title", "typeInfo", "des"};
        //【有效天数】
        //【优惠券类型，0折扣券，1抵用券，2满减券，3免运费】
        //【每次获取数量】
        String[] intParams = {"day", "type", "expand","getType"};
        Verify<Map<String, String>> strVerify = strVerify(strParams);
        Verify<Map<String, Integer>> intVerify = intVerify(intParams);
        if (strVerify.getCode() < 0) {
            resFailure(strVerify.getMsg());
            return;
        }
        if (intVerify.getCode() < 0) {
            resFailure(intVerify.getMsg());
            return;
        }
        if (!logic.typeInfoVerify(intVerify.getData().get("type"), strVerify.getData().get("typeInfo"))) {
            resFailure(ResCode.PARAM_FORMAT.getMsg());
            return;
        }
        try {
            Coupon coupon = new Coupon();
            coupon.setTitle(strVerify.getData().get("title"));
            Date startTime = new Date();
            Date endTime = DateUtils.getNextDate(startTime, intVerify.getData().get("day"));
            coupon.setStartTime(startTime);
            coupon.setEndTime(endTime);
            coupon.setDayCount(intVerify.getData().get("day"));
            coupon.setType(intVerify.getData().get("type"));
            coupon.setTypeInfo(strVerify.getData().get("typeInfo"));
            //coupon.setGetType(2);//系统自动派送
            coupon.setGetType(intVerify.getData().get("getType"));
            coupon.setGetTypeExpand(intVerify.getData().get("expand"));
            coupon.setCreateTime(startTime);
            coupon.setDes(strVerify.getData().get("des"));
            coupon.setCdkey(logic.CDKey(coupon));
            coupon.save();
            //这边要写批量派送的方法
            if(coupon.getGetType()==2) {
                logic.sendCouponToMembers(coupon);
            }
            //这边要写批量派送的方法
            resSuccess(ResCode.COUPON_SUCCESS);
        } catch (Exception e) {
           logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }

    public void addNormalCoupon() {
        //【标题】
        //【优惠券类型扩展字段,type=0(表示折扣数)1(表示抵用金额)2(表示满？减？，格式100-10)3(为空)】
        //【说明】
        //【有效期开始时间】
        //【有效期结束时间】
        String[] strParams = {"title", "typeInfo", "des","startTime","endTime"};
        //【发行数量】
        //【优惠券类型，0折扣券，1抵用券，2满减券，3免运费】
        //【获取途径，0用户点击获取，1管理员手动分配】
        String[] intParams={"issueCount","type","getType"};
        Verify<Map<String, String>> strVerify = strVerify(strParams);
        Verify<Map<String, Integer>> intVerify = intVerify(intParams);
        if (strVerify.getCode() < 0) {
            resFailure(strVerify.getMsg());
            return;
        }
        if (intVerify.getCode() < 0) {
            resFailure(intVerify.getMsg());
            return;
        }
        if (!logic.timeVerify(strVerify.getData().get("startTime"),strVerify.getData().get("endTime"))){
            resFailure(ResCode.COUPON_TIME_ERROR.getMsg());
            return;
        }
        if (!logic.typeInfoVerify(intVerify.getData().get("type"), strVerify.getData().get("typeInfo"))) {
            resFailure(ResCode.PARAM_FORMAT.getMsg());
            return;
        }
        try{
            Coupon coupon = new Coupon();
            coupon.setTitle(strVerify.getData().get("title"));
            coupon.setIssueCount(intVerify.getData().get("issueCount"));
            Date startTime=DateUtils.format(strVerify.getData().get("startTime"),"yyyy-MM-dd hh");
            Date endTime=DateUtils.format(strVerify.getData().get("endTime"),"yyyy-MM-dd hh");
            coupon.setStartTime(startTime);
            coupon.setEndTime(endTime);
            coupon.setType(intVerify.getData().get("type"));
            coupon.setTypeInfo(strVerify.getData().get("typeInfo"));
            coupon.setGetType(intVerify.getData().get("getType"));
            coupon.setDes(strVerify.getData().get("des"));
            coupon.setCreateTime(new Date());
            coupon.setCdkey(logic.CDKey(coupon));
            coupon.save();
            logic.createCoupon(coupon,0);
            resSuccess(ResCode.COUPON_SUCCESS.getMsg());
        }catch (Exception e){
            logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }
    public void updateNormalCoupon() {
        //【标题】
        //【优惠券类型扩展字段,type=0(表示折扣数)1(表示抵用金额)2(表示满？减？，格式100-10)3(为空)】
        //【说明】
        //【有效期开始时间】
        //【有效期结束时间】
        String[] strParams = {"title", "typeInfo", "des","startTime","endTime"};
        //【发行数量】
        //【优惠券类型，0折扣券，1抵用券，2满减券，3免运费】
        //【获取途径，0用户点击获取，1管理员手动分配】
        String[] intParams={"id","oldIssueCount","issueCount","type","getType"};
        Verify<Map<String, String>> strVerify = strVerify(strParams);
        Verify<Map<String, Integer>> intVerify = intVerify(intParams);
        if (strVerify.getCode() < 0) {
            resFailure(strVerify.getMsg());
            return;
        }
        if (intVerify.getCode() < 0) {
            resFailure(intVerify.getMsg());
            return;
        }
        if (!logic.timeVerify(strVerify.getData().get("startTime"),strVerify.getData().get("endTime"))){
            resFailure(ResCode.COUPON_TIME_ERROR.getMsg());
            return;
        }
        if (!logic.typeInfoVerify(intVerify.getData().get("type"), strVerify.getData().get("typeInfo"))) {
            resFailure(ResCode.PARAM_FORMAT.getMsg());
            return;
        }
        try{
            Coupon coupon = new Coupon();
            coupon.setId(intVerify.getData().get("id"));
            coupon.setTitle(strVerify.getData().get("title"));
            coupon.setIssueCount(intVerify.getData().get("issueCount"));
            int oldIssueCount = intVerify.getData().get("oldIssueCount");//之前设置的发行量
            Date startTime=DateUtils.format(strVerify.getData().get("startTime"),"yyyy-MM-dd hh");
            Date endTime=DateUtils.format(strVerify.getData().get("endTime"),"yyyy-MM-dd hh");
            coupon.setStartTime(startTime);
            coupon.setEndTime(endTime);
            coupon.setType(intVerify.getData().get("type"));
            coupon.setTypeInfo(strVerify.getData().get("typeInfo"));
            coupon.setGetType(intVerify.getData().get("getType"));
            coupon.setDes(strVerify.getData().get("des"));
            coupon.setCreateTime(new Date());
            coupon.setCdkey(logic.CDKey(coupon));
            coupon.update();
            logic.createCoupon(coupon,oldIssueCount);
            resSuccess(ResCode.COUPON_SUCCESS.getMsg());
        }catch (Exception e){
            logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }
    public void getList(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            Coupon coupon=getModel(Coupon.class,"",true);
            Page<Coupon> couponPage = Coupon.dao.getList(coupon,pageNo,pageSize);
            resSuccess(couponPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     *获取用户点击领取的优惠劵
     */
    public void getUserClickCouponList(){
        resSuccess(Coupon.dao.getUserClickCouponList());
    }
    /**
     * 获取用户优惠券列表
     */
    public void getCouponsByMember(){
        Member member=getModel(Member.class,"");
        List<MemberCoupon> memberCouponList=logic.couponByMember(member);
        resSuccess(memberCouponList);
    }

    /**
     * 优惠券兑换
     */
    public void exchangeCoupon(){
        int memberId=getParaToInt("id");
        String cdKey=getPara("cdKey");
        res(logic.exchange(memberId,cdKey));
    }

    /**
     * 获取优惠劵使用情况
     */
    public void getCouponList(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            MemberCoupon coupon=getModel(MemberCoupon.class,"",true);
            Page<MemberCoupon> couponPage = MemberCoupon.dao.getList(coupon,pageNo,pageSize);
            resSuccess(couponPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    public void getCouponById(){
        Coupon coupon=getModel(Coupon.class,"");
        coupon = Coupon.dao.findById(coupon.getId());
        if (coupon.getEndTime().getTime()<=new Date().getTime()){
            coupon.put("overdue",1);//已过期
        }
        else{
            coupon.put("overdue",0);//未过期
        }
        resSuccess(coupon);
    }

    /**
     * 分配优惠券给会员
     */
    public void sendCouponToMember(){
        try{
            int memberCouponId = Integer.parseInt(getPara("memberCouponId"));
            int memberId = Integer.parseInt(getPara("memberId"));
            MemberCoupon memberCoupon = MemberCoupon.dao.findById(memberCouponId);
            memberCoupon.setMemberIdGet(memberId);
            memberCoupon.setAllot(true);
            memberCoupon.update();
            //发送短信给会员
            Member member = Member.dao.findById(memberId);
            SmsParm parm = new SmsParm();
            parm.setPhone(member.getPhone());
            parm.setCouponTitle("");
            parm.setCouponCode(memberCoupon.getCdkey());
            parm.setSmsType(0);
            parm.setUserName(member.getNickname());
            SmsUtils.sendSms(parm);
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

}
