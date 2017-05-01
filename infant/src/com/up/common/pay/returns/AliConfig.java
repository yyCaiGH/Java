package com.up.common.pay.returns;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * TODO:
 * <p>
 * Created by 王剑洪 on 2016/11/2.
 */
public class AliConfig {
    private static String host_url="";
    private static String notify_url="";
    private static String return_url="";
    private static String refund_notify_url = "http://139.224.192.136:8080/yorder/refundNotify";
    //private static String refund_notify_url = "http://cyy.tunnel.qydev.com/yorder/refundNotify";
    private static String show_url="";

    /*static {
        Properties prop = new Properties();
        InputStream in = SolrURLHelper.class.getClassLoader().getResourceAsStream("/search.properties");
        try {
            prop.load(in);
            host_url = prop.getProperty("hostHead").trim();
            notify_url = host_url+prop.getProperty("notify_url").trim();
            return_url =prop.getProperty("return_url").trim();
            refund_notify_url = prop.getProperty("refund_notify_url").trim();
            show_url = prop.getProperty("show_url").trim();
        } catch (IOException e) {
        }
    }*/

    public static String getNotify_url(){
        return notify_url;
    }
    public static String getReturn_url(){
        return return_url;
    }
    public static String getShow_url(){
        return show_url;
    }

    public static String getRefund_notify_url(){
        return refund_notify_url;
    }
}
