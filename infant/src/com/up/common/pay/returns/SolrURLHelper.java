package com.up.common.pay.returns;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取solr服务URL帮助类
 * Title:  <br>
 * project:shopsearch <br>
 * Date:2016年4月4日<br>
 * @author linwb
 */
public class SolrURLHelper {

	private static String solrhost;
	private static String solrport;

	static {
		Properties prop = new Properties();
		InputStream in = SolrURLHelper.class.getClassLoader().getResourceAsStream("/search.properties");
		try {
			prop.load(in);
			solrhost = prop.getProperty("solrhost").trim();
			solrport = prop.getProperty("solrport").trim();
		} catch (IOException e) {
		}
	}

	public static String getDeafultURL() {
		return "http://" + solrhost + ":" + solrport + "/solr/";
	}

	public static String getCoreURL(SolrCoreEnum core) {
		return "http://" + solrhost + ":" + solrport + "/solr/" + core.getCode();
	}
	
	public static String getDeltaURL(SolrCoreEnum core) {
		return "http://" + solrhost + ":" + solrport + "/solr/" + core.getCode()+ "/dataimport";
	}
	
	public static String getDeltaURL(SolrCoreEnum core,String command) {
		return "http://" + solrhost + ":" + solrport + "/solr/" + core.getCode()+ "/dataimport?command="+command;
	}

}
