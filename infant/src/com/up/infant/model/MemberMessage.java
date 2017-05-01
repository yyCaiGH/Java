package com.up.infant.model;

import com.jfinal.plugin.activerecord.Page;
import com.up.infant.model.base.BaseMemberMessage;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class MemberMessage extends BaseMemberMessage<MemberMessage> {
	public static final MemberMessage dao = new MemberMessage();

	public Page<MemberMessage> getMessages(int pageSize,int pageNo,int memberId){
		String select="SELECT t_member_message.*,t_sys_message.title,t_sys_message.content,t_sys_message.type,t_sys_message.create_time ";
		String sql="FROM t_sys_message t_sys_message " +
				"LEFT JOIN t_member_message t_member_message ON t_member_message.msg_id=t_sys_message.id " +
				"WHERE t_member_message.member_id="+memberId+" ORDER BY t_sys_message.create_time DESC";
		return paginate(pageNo,pageSize,true,select,sql);



	}

	public List<MemberMessage> getMessage(int memberId){
		String sql="SELECT * FROM t_member_message WHERE is_read=0 AND member_id="+memberId;
		return find(sql);
	}





}
