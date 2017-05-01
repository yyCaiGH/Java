package com.up.infant.controller.coupon;

import com.up.common.def.ResCode;
import com.up.common.utils.DateUtils;
import com.up.common.utils.RandomUtils;
import com.up.infant.model.Coupon;
import com.up.infant.model.Member;
import com.up.infant.model.MemberCoupon;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO:优惠券逻辑处理
 * Created by 王剑洪
 * on 2016/10/31 0031. 23:59
 */
public class CouponLogic {

    MemberCoupon memberCouponDao = new MemberCoupon();
    Coupon couponDao = new Coupon();

    /**
     * 优惠券拓展字段是否对应优惠券类型
     *
     * @param type
     * @param typeInfo
     * @return
     */
    public boolean typeInfoVerify(int type, String typeInfo) {
        if (type == 0) {
            try {
                double discount = Double.parseDouble(typeInfo);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (type == 1) {
            try {
                double discountMoney = Double.parseDouble(typeInfo);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (type == 2) {
            String[] full = typeInfo.split("-");
            if (full.length != 2) {
                return false;
            }
            try {
                int f = Integer.parseInt(full[0]);
                int s = Integer.parseInt(full[1]);
                if (f <= 0 || s <= 0) {
                    return false;
                }
                if (f - s <= 0) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 开始时间跟结束时间校验
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean timeVerify(String startTime, String endTime) {
        String format = "yyyy-MM-dd hh";
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            Date start = df.parse(startTime);
            Date end = df.parse(endTime);
            if (end.getTime() - start.getTime() <= 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String CDKey(Coupon coupon) {
        String cdKey = RandomUtils.randomStr(10);
        if (coupon.verifyCDKey(cdKey) > 0) {
            return CDKey(coupon);
        }
        return cdKey;
    }

    public String MemberCDKey() {
        String cdKey = RandomUtils.randomStr(10);
        if (MemberCoupon.dao.verifyCDKey(cdKey) > 0) {
            return MemberCDKey();
        }
        return cdKey;
    }

    /**
     * 获取用户的优惠券
     *
     * @param member
     * @return
     */
    public List<MemberCoupon> couponByMember(Member member) {
        return memberCouponDao.getCoupons(member.getId());
    }

    public ResCode exchange(int memberId, String cdKey) {
//
        MemberCoupon memberCoupon1 = memberCouponDao.getByCdKey(cdKey);
        if (null==memberCoupon1){//对应兑换码的优惠券不存在
            return ResCode.COUPON_NULL;
        }

        if ( memberCouponDao.getCountByCouponIdAndMemberId(memberId,memberCoupon1.getCouponId())>0){
            return ResCode.COUPON_HAS;
        }
        Coupon coupon = couponDao.findById(memberCoupon1.getCouponId());
        if (coupon.getEndTime().getTime() <= new Date().getTime()) {//过期了
            return ResCode.COUPON_PAST_DUE;
        }
        if (coupon.getIssueCount()<=coupon.getGetCount()){//已经被领取光了
            return ResCode.COUPON_FULL;
        }
        MemberCoupon memberCoupon=memberCoupon1;
        memberCoupon.setId(null);
        memberCoupon.setMemberIdExchange(memberId);
        memberCoupon.save();
        coupon.setGetCount(coupon.getGetCount()+1);
        coupon.update();
        return ResCode.COUPON_EXCHANGE_SUCCESS;
    }

    /**
     * 批量分发优惠券给所有会员
     * @param coupon
     */
    public void sendCouponToMembers(Coupon coupon){
        List<Member> memberList = Member.dao.getAllMembers(0);//获取全部正常的会员
        List<MemberCoupon> mcList = new ArrayList<MemberCoupon>();
        for (int i = 0;i<memberList.size();i++){
            MemberCoupon memberCoupon = new MemberCoupon();
            memberCoupon.setCouponId(coupon.getId());
            memberCoupon.setMemberIdGet(memberList.get(i).getId());
            memberCoupon.setCdkey(MemberCDKey());
            memberCoupon.setStatus(0);
            memberCoupon.setAllot(true);
            mcList.add(memberCoupon);
        }
        MemberCoupon.dao.addBatch(mcList);
        coupon.setIssueCount(memberList.size());
        coupon.update();
    }

    public void createCoupon(Coupon coupon,int oldIssueCount) {
        List<MemberCoupon> mcList = new ArrayList<MemberCoupon>();
        for (int i = 0;i<(coupon.getIssueCount()-oldIssueCount);i++){
            MemberCoupon memberCoupon = new MemberCoupon();
            memberCoupon.setCouponId(coupon.getId());
            memberCoupon.setCdkey(MemberCDKey());
            memberCoupon.setStatus(0);
            memberCoupon.setAllot(false);
            mcList.add(memberCoupon);
        }
        MemberCoupon.dao.addBatch(mcList);
    }
}
