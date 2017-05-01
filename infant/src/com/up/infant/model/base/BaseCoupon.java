package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCoupon<M extends BaseCoupon<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setCdkey(java.lang.String cdkey) {
		set("cdkey", cdkey);
	}

	public java.lang.String getCdkey() {
		return get("cdkey");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setStartTime(java.util.Date startTime) {
		set("start_time", startTime);
	}

	public java.util.Date getStartTime() {
		return get("start_time");
	}

	public void setEndTime(java.util.Date endTime) {
		set("end_time", endTime);
	}

	public java.util.Date getEndTime() {
		return get("end_time");
	}

	public void setIssueCount(java.lang.Integer issueCount) {
		set("issue_count", issueCount);
	}

	public java.lang.Integer getIssueCount() {
		return get("issue_count");
	}

	public void setGetCount(java.lang.Integer getCount) {
		set("get_count", getCount);
	}

	public java.lang.Integer getGetCount() {
		return get("get_count");
	}

	public void setUseCount(java.lang.Integer useCount) {
		set("use_count", useCount);
	}

	public java.lang.Integer getUseCount() {
		return get("use_count");
	}

	public void setGetType(java.lang.Integer getType) {
		set("get_type", getType);
	}

	public java.lang.Integer getGetType() {
		return get("get_type");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setTypeInfo(java.lang.String typeInfo) {
		set("type_info", typeInfo);
	}

	public java.lang.String getTypeInfo() {
		return get("type_info");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setGetTypeExpand(java.lang.Integer getTypeExpand) {
		set("get_type_expand", getTypeExpand);
	}

	public java.lang.Integer getGetTypeExpand() {
		return get("get_type_expand");
	}

	public void setDayCount(java.lang.Integer dayCount) {
		set("day_count", dayCount);
	}

	public java.lang.Integer getDayCount() {
		return get("day_count");
	}

	public void setDes(java.lang.String des) {
		set("des", des);
	}

	public java.lang.String getDes() {
		return get("des");
	}

}
