package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.up.common.utils.Sql;
import com.up.infant.model.base.BaseSysConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysConfig extends BaseSysConfig<SysConfig> {
	public static final SysConfig dao = new SysConfig();

	public static final String table="t_sys_config t_sys_config";
	public static final String id="t_sys_config.id";
	public static final String iId="t_sys_config.i_id";
	public static final String key="t_sys_config.key";
	public static final String value="t_sys_config.value";
	public static final String status="t_sys_config.status";
	public List<SysConfig> getConfigList(int identifyingId){
		Map<String,String> map=new HashMap<String, String>();
		map.put(iId,identifyingId+"");
		map.put(status,"0");
		String sql= Sql.select("*")+Sql.from(table)+Sql.where(map)+Sql.orderBy(id,true);
		return find(sql);
	}

	public SysConfig getInfo(int pid){
//		Map<String,String> map=new HashMap<String, String>();
//		map.put(id,pid+"");
//		String sql= Sql.select(id,iId,value)+Sql.from(table)+Sql.where(map)+Sql.orderBy(id,true);
		return findById(pid);
	}


	public void updateBatch(List<SysConfig> list) {
		Db.batchUpdate(list,list.size());
	}

	public void deleteBatch(List<SysConfig> deleteGradeList) {
		Object[][] objs = new Object[deleteGradeList.size()][1];
		for(int i = 0;i<deleteGradeList.size();i++){
			objs[i][0] = deleteGradeList.get(i).getId();
		}
		Db.batch("delete from t_sys_config where id = ?",objs,deleteGradeList.size());
	}

	public void addBatch(List<SysConfig> list) {
		Db.batchSave(list,list.size());
	}
}
