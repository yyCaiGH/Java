package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGoodsPoints<M extends BaseGoodsPoints<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setGoodsId(java.lang.Integer goodsId) {
		set("goods_id", goodsId);
	}

	public java.lang.Integer getGoodsId() {
		return get("goods_id");
	}

	public void setSellPoint(java.lang.String sellPoint) {
		set("sell_point", sellPoint);
	}

	public java.lang.String getSellPoint() {
		return get("sell_point");
	}

}
