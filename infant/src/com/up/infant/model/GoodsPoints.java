package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.up.infant.model.base.BaseGoodsPoints;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class GoodsPoints extends BaseGoodsPoints<GoodsPoints> {
	public static final GoodsPoints dao = new GoodsPoints();

	public void addBatch(List<GoodsPoints> list){
		Db.batchSave(list,list.size());
	}

	public void deleteBatch(int goodsId){
		GoodsPoints goodsPoints = new GoodsPoints();
		goodsPoints.setGoodsId(goodsId);
		List<GoodsPoints> oldSkus=new GoodsPoints().getList(goodsPoints);
		Object[][] objs = new Object[oldSkus.size()][1];
		for(int i = 0;i<oldSkus.size();i++){
			objs[i][0] = oldSkus.get(i).getId();
		}
		Db.batch("delete from t_goods_points where id = ?",objs,oldSkus.size());
	}

    public List<GoodsPoints> getList(GoodsPoints goodsPoints) {
		String sql = "select * from t_goods_points t where 1=1";
		if (goodsPoints.getGoodsId()!=null){
			sql +=" AND t.goods_id="+goodsPoints.getGoodsId();
		}
		sql +=" order by t.id asc";
		return dao.find(sql);
    }

    public List<GoodsPoints> points(int goodsId){
		String sql="SELECT * FROM t_goods_points WHERE goods_id="+goodsId;
		return find(sql);
	}
}