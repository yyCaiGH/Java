package com.up.common.pay.returns;

/**
 * 
 * Title: Solr集合枚举<br>
 * project:shopsearch <br>
 * Date:2016年4月4日<br>
 * 
 * @author linwb
 */
public enum SolrCoreEnum {
	/**
	 * 商品集合
	 */
	GDS("gds"),

	/**
	 * 店铺集合
	 */
	SHOP("shop");

	/**
	 * 集合名词
	 */
	private String code;

	private SolrCoreEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
