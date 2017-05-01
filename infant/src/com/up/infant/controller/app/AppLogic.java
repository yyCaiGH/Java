package com.up.infant.controller.app;

import com.up.common.def.ResCode;
import com.up.common.pay.wx.HtmlUtil;
import com.up.common.pay.wx.WxPay;
import com.up.common.utils.RandomUtils;
import com.up.infant.model.*;

import java.util.Date;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/10 0010. 15:15
 */
public class AppLogic {
    CustomPage pageDao =new CustomPage();
    Coupon couponDao=new Coupon();
    Member memberDao=new Member();
    MemberCoupon memberCouponDao=new MemberCoupon();
    Exp expDao=new Exp();


    public CustomPage pageInfo(int pageId){
        String sql="SELECT * FROM t_custom_page WHERE id="+pageId+" AND is_show=0";
        return pageDao.findFirst(sql);
    }

    public Coupon couponInfo(int couponId){
        String sql="SELECT * FROM t_coupon WHERE id="+couponId+" AND get_type=0";
        return couponDao.findFirst(sql);

    }

    public ResCode getCoupon(int memberId,int couponId){
        Member member = memberDao.findById(memberId);
        if (null == member) {//用户不存在
            return  ResCode.MEMBER_NO;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            return ResCode.STATUS_ERROR;
        }

        Coupon coupon=couponDao.findById(couponId);
        if (null==coupon){
            return ResCode.COUPON_NULL;
        }
        if (coupon.getGetType()!=0){
            return ResCode.PARAM_ERROR;
        }
        if (coupon.getEndTime().getTime() <= new Date().getTime()) {//过期了
            return ResCode.COUPON_PAST_DUE;
        }
        if (coupon.getIssueCount()<=coupon.getGetCount()){//已经被领取光了
            return ResCode.COUPON_FULL;
        }
        if ( memberCouponDao.getCountByCouponIdAndMemberId(memberId,coupon.getId())>0){
            return ResCode.COUPON_HAS2;
        }
        MemberCoupon memberCoupon=new MemberCoupon();
        memberCoupon.setCdkey(MemberCDKey());
        memberCoupon.setCouponId(couponId);
        memberCoupon.setMemberIdGet(memberId);
        memberCoupon.setStatus(0);
        memberCoupon.setAllot(true);
        memberCoupon.save();
        coupon.setGetCount(coupon.getGetCount()+1);
        coupon.update();
        return ResCode.COUPON_GET_SUCCESS;
    }

    public String MemberCDKey() {
        String cdKey = RandomUtils.randomStr(10);
        if (MemberCoupon.dao.verifyCDKey(cdKey) > 0) {
            return MemberCDKey();
        }
        return cdKey;
    }

    public double getMemberRebates(int memberId){
        String sql="SELECT t_member.`rebate_count` FROM t_member WHERE id="+memberId;
        Member member=memberDao.findFirst(sql);
        return member.getRebateCount();
    }
    
}
