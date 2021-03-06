package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.infant.model.base.BaseMemberCoupon;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class 	MemberCoupon extends BaseMemberCoupon<MemberCoupon> {
	public static final MemberCoupon dao = new MemberCoupon();

	public static final String table="t_member_coupon t_member_coupon";
	public static final String id="t_member_coupon.id";
	public static final String mId="t_member_coupon.member_id";
	public static final String cId="t_member_coupon.coupon_id";
	public static final String sta="t_member_coupon.id";

	public List<MemberCoupon> getCoupons(int memberId){
		String sql="SELECT t_member_coupon.*,\n" +
				"t_coupon.title,t_coupon.start_time,t_coupon.end_time,t_coupon.type,t_coupon.type_info,t_coupon.des \n" +
				"FROM t_member_coupon t_member_coupon \n" +
				"LEFT JOIN t_coupon t_coupon ON t_coupon.id=t_member_coupon.coupon_id \n" +
				"WHERE t_member_coupon.member_id_get="+memberId+" or t_member_coupon.member_id_exchange =" +memberId+
				" group by t_member_coupon.cdkey,t_member_coupon.coupon_id";
		return find(sql);
	}

	public Long getCountByCouponIdAndMemberId(int memberId,int couponId){
		String sql="SELECT COUNT(*) FROM t_member_coupon WHERE \n" +
				"( coupon_id="+couponId+" AND member_id_get ="+memberId+")\n" +
				"OR ( coupon_id="+couponId+" AND member_id_exchange="+memberId+")";
		return findFirst(sql).getLong("COUNT(*)");
	}

	public MemberCoupon getByCdKey(String cdKey){
		String sql=" SELECT * FROM t_member_coupon WHERE cdkey='"+cdKey+"' AND member_id_exchange IS NULL";
		return findFirst(sql);
	}


    public Page<MemberCoupon> getList(MemberCoupon coupon, int pageNo, int pageSize) {
		String select = "SELECT t.*,m1.nickname AS nickname_get,m2.nickname AS nickname_use";
		String sqlExceptSelect = "from t_member_coupon AS t"+
				" LEFT JOIN t_member AS m1 ON m1.id = t.member_id_get"+
				" LEFT JOIN t_member AS m2 ON m2.id = t.member_id_use"+
				" WHERE 1=1";
		if (coupon.getStatus()!=null){//0:未使用，1：已使用，2：已过期
			sqlExceptSelect +=" AND t.status = "+coupon.getStatus();
		}
		if(coupon.getAllot()!=null){
			sqlExceptSelect +=" AND t.allot = "+coupon.getAllot();
		}
		if(coupon.getCouponId()!=null){
			sqlExceptSelect +=" AND t.coupon_id = "+coupon.getCouponId();
		}
		sqlExceptSelect +=" order by id asc";
		return paginate(pageNo, pageSize, select, sqlExceptSelect);
    }

    public List<MemberCoupon> getByCouponById(int couponId){
		String sql = "select t.*" +
				" from t_member_coupon AS t"+
				" where t.coupon_id="+couponId;
		return dao.find(sql);
	}
	public void addBatch(List<MemberCoupon> list){
		Db.batchSave(list,list.size());
	}

	public Long verifyCDKey(String cdKey) {
		Long count=findFirst("select count(*) from t_member_coupon where cdkey ='" +cdKey+"'").get("count(*)");
		return count;
	}

	public MemberCoupon getById(int memberCouponId){
		String sql="SELECT t_member_coupon.`id` AS member_coupon_id ,t_coupon.*\n" +
				"FROM t_member_coupon t_member_coupon\n" +
				"LEFT JOIN t_coupon t_coupon ON t_coupon.`id`=t_member_coupon.`coupon_id`\n" +
				"WHERE t_member_coupon.`id`="+memberCouponId+" AND t_member_coupon.`status`=0\n" +
				" AND t_coupon.`end_time`>NOW() AND t_coupon.`start_time`<NOW()";
		return findFirst(sql);
	}
}
