package com.up.infant.model;

import com.jfinal.plugin.activerecord.Db;
import com.up.infant.model.base.BasePurchaseDetails;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class PurchaseDetails extends BasePurchaseDetails<PurchaseDetails> {
	public static final PurchaseDetails dao = new PurchaseDetails();

	
	public void addBatch(List<PurchaseDetails> list) {
		Db.batchSave(list,list.size());
	}

}
