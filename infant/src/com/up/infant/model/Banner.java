package com.up.infant.model;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.utils.Sql;
import com.up.infant.model.base.BaseBanner;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Banner extends BaseBanner<Banner> {
	public static final Banner dao = new Banner();

	public static String table="t_banner t_banner";
	public static String id="t_banner.id";
	public static String name="t_banner.name";
	public static String img="t_banner.img";
	public static String sort="t_banner.sort";//排序
	public static String is_show="t_banner.is_show";//是否显示，0否，1是
	public static String type="t_banner.type";//跳转类型，0单页，1文章，2优惠券，3品牌
	public static String create_time="t_banner.create_time";

	public List<Banner> getBanners(){
//		String sql= Sql.select(id,img,type)+Sql.from(table)+Sql.orderBy(sort,true);
		String sql="SELECT * \n" +
				"FROM t_banner \n" +
				"WHERE \n" +
				"TYPE<>3 AND\n" +
				"is_show=1 ORDER BY sort ASC,create_time DESC";
		return find(sql);
	}

	public List<Banner> getBook(){
		String sql="SELECT * \n" +
				"FROM t_banner \n" +
				"WHERE \n" +
				"TYPE=3 AND\n" +
				"is_show=1 ORDER BY sort ASC,create_time DESC LIMIT 4";
		return find(sql);
	}

	public boolean hasSort(int index){
		String sql= "select count(*) AS count from t_banner where sort="+index;
		Long count=findFirst(sql).get("count");
		if (count>0){
			return true;
		}
		return false;
	}

	public Page<Banner> getBanners(int pageNo,int pageSize) {
		return paginate(pageNo, pageSize, "select *", "from t_banner order by id asc");

	}
}