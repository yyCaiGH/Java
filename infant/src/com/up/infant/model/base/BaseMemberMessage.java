package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMemberMessage<M extends BaseMemberMessage<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setMemberId(java.lang.Integer memberId) {
		set("member_id", memberId);
	}

	public java.lang.Integer getMemberId() {
		return get("member_id");
	}

	public void setMsgId(java.lang.Integer msgId) {
		set("msg_id", msgId);
	}

	public java.lang.Integer getMsgId() {
		return get("msg_id");
	}

	public void setIsRead(java.lang.Integer isRead) {
		set("is_read", isRead);
	}

	public java.lang.Integer getIsRead() {
		return get("is_read");
	}

}