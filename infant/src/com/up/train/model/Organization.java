package com.up.train.model;

import com.up.train.model.base.BaseOrganization;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Organization extends BaseOrganization<Organization> {
	public static final Organization dao = new Organization();
	public List<Organization> getList(){
		String sql = "select * from t_organization";
		return  find(sql);
	}

    public Organization findByAccount(String account) {
		String sql = "select * from t_organization where account='"+account+"'";
		return findFirst(sql);
    }


}
