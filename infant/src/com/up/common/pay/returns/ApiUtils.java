package com.up.common.pay.returns;


import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/30 0030. 11:48
 */
public class ApiUtils {

    //退款异步通知页面路径
    String refund_notify_url="yorder/refundNotify";

    /**
     * 退款
     * @param partner
     * @param seller_user_id
     * @param batch_no
     * @param batch_num
     * @param detail_data
     */
    public static Map<String, String> reFoundApi(String partner,String seller_user_id,String batch_no,String batch_num,String detail_data){
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
        sParaTemp.put("partner", partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", AliConfig.getRefund_notify_url());
        // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
        sParaTemp.put("seller_user_id",seller_user_id);
        sParaTemp.put("refund_date", AlipayConfig.refund_date);
        sParaTemp.put("batch_no", batch_no);
        sParaTemp.put("batch_num", batch_num);
        sParaTemp.put("detail_data", detail_data);
        return sParaTemp;
    }

    /**
     *
     * @param partner
     * @param seller_id
     * @param out_trade_no
     * @param subject
     * @param total_fee
     * @param body
     * @return
     */
    public static Map<String,String> payApi(String url,String partner,String seller_id,String out_trade_no,String subject,String total_fee,String body){
        Map<String, String> sParaTemp = new HashMap<String, String>();
        String service = "alipay.wap.create.direct.pay.by.user";//wap网站支付
        sParaTemp.put("service", service);
        sParaTemp.put("partner", partner);
        sParaTemp.put("seller_id",seller_id);
        sParaTemp.put("_input_charset","utf-8");
        sParaTemp.put("payment_type", "1");
        sParaTemp.put("notify_url", AliConfig.getNotify_url());
//        sParaTemp.put("return_url", AliConfig.getReturn_url());
        sParaTemp.put("return_url", url);
        sParaTemp.put("out_trade_no", out_trade_no);//商户订单号，商户网站订单系统中唯一订单号，必填
        sParaTemp.put("subject", subject);//订单名称，必填
        sParaTemp.put("total_fee", total_fee);////付款金额，必填
        sParaTemp.put("show_url", url);//收银台页面上，商品展示的超链接，必填
//        sParaTemp.put("show_url", AliConfig.getShow_url());//收银台页面上，商品展示的超链接，必填
        //sParaTemp.put("app_pay","Y");//启用此参数可唤起钱包APP支付。
        sParaTemp.put("body", body);//商品描述，可空
        return sParaTemp;
    }
}
