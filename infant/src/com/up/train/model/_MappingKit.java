package com.up.train.model;

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
		arp.addMapping("t_admin", "id", Admin.class);
		arp.addMapping("t_apply", "id", Apply.class);
		arp.addMapping("t_exam", "id", Exam.class);
		arp.addMapping("t_organization", "id", Organization.class);
		arp.addMapping("t_user", "id", User.class);
	}
}

