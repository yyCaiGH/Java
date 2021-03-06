package com.up.infant.model;

import com.up.common.utils.Sql;
import com.up.infant.model.base.BaseMemberAddr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class MemberAddr extends BaseMemberAddr<MemberAddr> {
	public static final MemberAddr dao = new MemberAddr();

	public static final String table="t_member_addr t_member_addr";
	public static final String id="t_member_addr.id";
	public static final String mId="t_member_addr.member_id";
	public static final String consignee="t_member_addr.consignee";
	public static final String phone="t_member_addr.phone";
	public static final String address="t_member_addr.address";
	public static final String def="t_member_addr.def";
	public static final String pro="t_member_addr.province";
	public static final String city="t_member_addr.city";
	public static final String area="t_member_addr.area";

	public List<MemberAddr> getList(int memberId){
		Map<String,String> map=new HashMap<String, String>();
		map.put(mId,memberId+"");
		String sql= Sql.select("*")+Sql.from(table)+Sql.where(map);
		return  find(sql);
	}



}
