package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.up.common.utils.Sql;
import com.up.infant.model.base.BaseSku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Sku extends BaseSku<Sku> {
	public static final Sku dao = new Sku();

	public static final String table="t_sku";
	public static final String id="t_sku.id";
	public static final String name="t_sku.name";
	public static final String pId="t_sku.parent_id";
	public static final String status="t_sku.status";

	public List<Sku> getList(int parentId ){
		String sql= Sql.select("*")+Sql.from(table);
		if (parentId>=0){
			Map<String,String> map=new HashMap<String, String>();
			map.put(pId,parentId+"");
			map.put(status,"0");
			sql=sql+Sql.where(map);
		}

		return find(sql);
	}

	public Map<String,Object> getListAll(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Sku> parentSku = getList(0);
		for (int i=0;i<parentSku.size();i++){
			map.put(parentSku.get(i).getName(),getList(parentSku.get(i).getId()));
		}
		return map;
	}

	/**
	 * 根据名称获取sku
	 * @param skuName
	 * @return
	 */
	public Sku getByName(String skuName){
		Map<String,String> where=new HashMap<String, String>();
		where.put(name,skuName+"");
		where.put(pId,"0");
		where.put(status,"0");
		String sql= Sql.select("*")+Sql.from(table)+Sql.where(where);
		return findFirst(sql);
	}

    /**
     * 根据名称获取所有skus属性名称，主要用来判断是否重复了
     * @param skuName
     * @return
     */
    public List<Sku> getAllByName(String skuName){
        Map<String,String> where=new HashMap<String, String>();
        where.put(name,skuName+"");
        where.put(pId,"0");
		where.put(status,"0");
        String sql= Sql.select("name")+Sql.from(table)+Sql.where(where);
        return find(sql);
    }

	public void addBatch(List<Sku> skuList){
		Db.batchSave(skuList,skuList.size());
	}

	public void deleteBatch(int pId){
		List<Sku> oldSkus=new Sku().getList(pId);
		Object[][] objs = new Object[oldSkus.size()][1];
		for(int i = 0;i<oldSkus.size();i++){
			objs[i][0] = oldSkus.get(i).getId();
		}
		Db.batch("delete from t_sku where id = ?",objs,oldSkus.size());
	}

	public List<Sku> getColor(){
		String sql="SELECT t_sku.* FROM t_sku t_sku\n" +
				"LEFT JOIN t_sku t_sku2 ON t_sku2.parent_id=0 AND t_sku2.name ='颜色'\n" +
				"WHERE t_sku.parent_id=t_sku2.id\n";
		return find(sql);
	}


    public void updateBatch(List<Sku> list) {
		Db.batchUpdate(list,list.size());
    }

	public void deleteBatch1(List<Sku> deleteGradeList) {
		Object[][] objs = new Object[deleteGradeList.size()][1];
		for(int i = 0;i<deleteGradeList.size();i++){
			objs[i][0] = deleteGradeList.get(i).getId();
		}
		Db.batch("delete from t_sku where id = ?",objs,deleteGradeList.size());
	}

	public void updateBatchToDelete(Integer id) {
		List<Sku> skus=new Sku().getList(id);
		for (int i=0;i<skus.size();i++){
			skus.get(i).setStatus(1);
		}
		updateBatch(skus);
	}
}