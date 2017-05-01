package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseArticle<M extends BaseArticle<M>> extends Model<M> implements IBean {

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

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setImgUrl(java.lang.String imgUrl) {
		set("img_url", imgUrl);
	}

	public java.lang.String getImgUrl() {
		return get("img_url");
	}

	public void setPageView(java.lang.Integer pageView) {
		set("page_view", pageView);
	}

	public java.lang.Integer getPageView() {
		return get("page_view");
	}

	public void setLikeCount(java.lang.Integer likeCount) {
		set("like_count", likeCount);
	}

	public java.lang.Integer getLikeCount() {
		return get("like_count");
	}

	public void setComment(java.lang.Integer comment) {
		set("comment", comment);
	}

	public java.lang.Integer getComment() {
		return get("comment");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setIsTop(java.lang.Boolean isTop) {
		set("is_top", isTop);
	}

	public java.lang.Boolean getIsTop() {
		return get("is_top");
	}

	public void setTopTime(java.util.Date topTime) {
		set("top_time", topTime);
	}

	public java.util.Date getTopTime() {
		return get("top_time");
	}

	public void setIsRecommend(java.lang.Boolean isRecommend) {
		set("is_recommend", isRecommend);
	}

	public java.lang.Boolean getIsRecommend() {
		return get("is_recommend");
	}

	public void setRecommendTime(java.util.Date recommendTime) {
		set("recommend_time", recommendTime);
	}

	public java.util.Date getRecommendTime() {
		return get("recommend_time");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setFrom(java.lang.Integer from) {
		set("from", from);
	}

	public java.lang.Integer getFrom() {
		return get("from");
	}

	public void setBrand1(java.lang.String brand1) {
		set("brand1", brand1);
	}

	public java.lang.String getBrand1() {
		return get("brand1");
	}

	public void setBrand2(java.lang.String brand2) {
		set("brand2", brand2);
	}

	public java.lang.String getBrand2() {
		return get("brand2");
	}

	public void setBrand3(java.lang.String brand3) {
		set("brand3", brand3);
	}

	public java.lang.String getBrand3() {
		return get("brand3");
	}

	public void setBrand4(java.lang.String brand4) {
		set("brand4", brand4);
	}

	public java.lang.String getBrand4() {
		return get("brand4");
	}

	public void setBrand5(java.lang.String brand5) {
		set("brand5", brand5);
	}

	public java.lang.String getBrand5() {
		return get("brand5");
	}

	public void setBrand6(java.lang.String brand6) {
		set("brand6", brand6);
	}

	public java.lang.String getBrand6() {
		return get("brand6");
	}

	public void setTags(java.lang.String tags) {
		set("tags", tags);
	}

	public java.lang.String getTags() {
		return get("tags");
	}

	public void setRatio(java.math.BigDecimal ratio) {
		set("ratio", ratio);
	}

	public java.math.BigDecimal getRatio() {
		return get("ratio");
	}

}