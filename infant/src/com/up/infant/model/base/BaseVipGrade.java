package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseVipGrade<M extends BaseVipGrade<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setAmount(java.lang.Double amount) {
		set("amount", amount);
	}

	public java.lang.Double getAmount() {
		return get("amount");
	}

	public void setDiscount(java.lang.Double discount) {
		set("discount", discount);
	}

	public java.lang.Double getDiscount() {
		return get("discount");
	}

	public void setRebate(java.lang.Double rebate) {
		set("rebate", rebate);
	}

	public java.lang.Double getRebate() {
		return get("rebate");
	}

}
