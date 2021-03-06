package com.up.infant.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("t_article", "id", Article.class);
		arp.addMapping("t_banner", "id", Banner.class);
		arp.addMapping("t_brand", "id", Brand.class);
		arp.addMapping("t_cart", "id", Cart.class);
		arp.addMapping("t_category", "id", Category.class);
		arp.addMapping("t_collect", "id", Collect.class);
		arp.addMapping("t_comment", "id", Comment.class);
		arp.addMapping("t_concern", "id", Concern.class);
		arp.addMapping("t_country", "id", Country.class);
		arp.addMapping("t_coupon", "id", Coupon.class);
		arp.addMapping("t_custom_page", "id", CustomPage.class);
		arp.addMapping("t_exp", "id", Exp.class);
		arp.addMapping("t_goods", "id", Goods.class);
		arp.addMapping("t_goods_points", "id", GoodsPoints.class);
		arp.addMapping("t_goods_tag", "id", GoodsTag.class);
		arp.addMapping("t_identifying", "id", Identifying.class);
		arp.addMapping("t_member", "id", Member.class);
		arp.addMapping("t_member_addr", "id", MemberAddr.class);
		arp.addMapping("t_member_child", "id", MemberChild.class);
		arp.addMapping("t_member_coupon", "id", MemberCoupon.class);
		arp.addMapping("t_member_message", "id", MemberMessage.class);
		arp.addMapping("t_order", "id", Order.class);
		arp.addMapping("t_order_goods", "id", OrderGoods.class);
		arp.addMapping("t_postage_group", "id", PostageGroup.class);
		arp.addMapping("t_postage_province", "id", PostageProvince.class);
		arp.addMapping("t_postage_tpl", "id", PostageTpl.class);
		arp.addMapping("t_province", "id", Province.class);
		arp.addMapping("t_purchase_details", "id", PurchaseDetails.class);
		arp.addMapping("t_region", "code", Region.class);
		arp.addMapping("t_sku", "id", Sku.class);
		arp.addMapping("t_sku_goods", "id", SkuGoods.class);
		arp.addMapping("t_storage_purchase", "id", StoragePurchase.class);
		arp.addMapping("t_storage_returns", "id", StorageReturns.class);
		arp.addMapping("t_sys_config", "id", SysConfig.class);
		arp.addMapping("t_sys_message", "id", SysMessage.class);
		arp.addMapping("t_system_message", "id", SystemMessage.class);
		arp.addMapping("t_user", "id", User.class);
		arp.addMapping("t_vip_grade", "id", VipGrade.class);
		arp.addMapping("test", "id", Test.class);
	}
}

