package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.common.utils.TextUtils;
import com.up.infant.model.base.BaseStoragePurchase;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class StoragePurchase extends BaseStoragePurchase<StoragePurchase> {
	public static final StoragePurchase dao = new StoragePurchase();


    public Page<StoragePurchase> getList(ComParams params) {
        String select = "SELECT *";
        String sqlExceptSelect = "from t_storage_purchase"+
                " WHERE 1=1";
        sqlExceptSelect +=" order by id DESC";//DESC
        return paginate(params.getPageNo(), params.getPageSize(), select, sqlExceptSelect);
    }
}
