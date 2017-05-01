package com.up.infant.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOrderGoods<M extends BaseOrderGoods<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setSkuGoodsId(java.lang.Integer skuGoodsId) {
		set("sku_goods_id", skuGoodsId);
	}

	public java.lang.Integer getSkuGoodsId() {
		return get("sku_goods_id");
	}

	public void setOrderId(java.lang.Integer orderId) {
		set("order_id", orderId);
	}

	public java.lang.Integer getOrderId() {
		return get("order_id");
	}

	public void setCount(java.lang.Integer count) {
		set("count", count);
	}

	public java.lang.Integer getCount() {
		return get("count");
	}

	public void setPrice(java.lang.Double price) {
		set("price", price);
	}

	public java.lang.Double getPrice() {
		return get("price");
	}

	public void setDisPrice(java.lang.Double disPrice) {
		set("dis_price", disPrice);
	}

	public java.lang.Double getDisPrice() {
		return get("dis_price");
	}

	public void setSkuInfo(java.lang.String skuInfo) {
		set("sku_info", skuInfo);
	}

	public java.lang.String getSkuInfo() {
		return get("sku_info");
	}

	public void setGoodsSn(java.lang.String goodsSn) {
		set("goods_sn", goodsSn);
	}

	public java.lang.String getGoodsSn() {
		return get("goods_sn");
	}

	public void setImgUrl(java.lang.String imgUrl) {
		set("img_url", imgUrl);
	}

	public java.lang.String getImgUrl() {
		return get("img_url");
	}

}
