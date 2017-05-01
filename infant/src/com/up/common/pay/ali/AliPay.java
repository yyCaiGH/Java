package com.up.common.pay.ali;

import com.alipay.api.internal.util.AlipaySignature;
import com.up.common.pay.PayConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 20:44
 */
public class AliPay {

    /**
     * 创建订单信息
     *
     * @param out_trade_no 订单号
     * @param subject      商品名称
     * @param body         商品详情
     * @param price        价格，单位元
     * @param notify_url   服务器异步通知页面路径
     * @return
     */
    public static String getOrderInfo(String out_trade_no, String subject, String body, String price, String notify_url) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PayConstant.ALI_PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + PayConstant.ALI_SELLER_ID + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        orderInfo += "&app_id=" + PayConstant.ALI_APP_ID;//支付宝分配给开发者的应用ID
        orderInfo += "&method=alipay.trade.app.pay";//接口名称
        orderInfo += "&timestamp=" + timestamp();//时间戳
        orderInfo += "&version=1.0";//调用的接口版本，固定为：1.0

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        //orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * 客户端参数
     *
     * @param out_trade_no 订单号
     * @param subject      商品名称
     * @param body         商品详情
     * @param price        价格，单位元
     * @param notify_url   服务器异步通知页面路径
     * @return
     */
    public static String payInfo(String out_trade_no, String subject, String body, double price, String notify_url) {
//        String orderInfo = getOrderInfo(out_trade_no, subject, body, price, notify_url);
        Map<String, String> orderInfo = params(notify_url, price, out_trade_no, subject);
        orderInfo = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            orderInfo.put("sign", URLEncoder.encode(orderInfo.get("sign"), "UTF-8"));
            orderInfo.put("biz_content", URLEncoder.encode(orderInfo.get("biz_content"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        String payInfo = "app_id=" + orderInfo.get("app_id") +
                "&biz_content=" + orderInfo.get("biz_content") +
                "&charset=" + orderInfo.get("charset") +
                "&format=" + orderInfo.get("format") +
                "&method=" + orderInfo.get("method") +
                "&notify_url=" + orderInfo.get("notify_url") +
                "&sign_type=" + orderInfo.get("sign_type") +
                "&timestamp=" + orderInfo.get("timestamp") +
                "&version=" + orderInfo.get("version") +
                "&sign=" + orderInfo.get("sign");
        return payInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private static Map<String, String> sign(Map<String, String> content) {
//        Map<String,String> map=new HashMap<String, String>();
        try {
            content.put("sign", AlipaySignature.rsaSign(content, PayConstant.ALI_RSA_PRIVATE, "UTF-8"));
            content.put("sign_type", "RSA");
            return content;
        } catch (Exception e) {
            return null;
        }

//        return SignUtils.sign(content, PayConstant.ALI_RSA_PRIVATE);
    }

    /**
     * 获取签名方式
     *
     * @return
     */
    private static String getSignType() {
        return "sign_type=RSA";
    }


    public static String timestamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static String params1(String notifyUrl, double totalAmount, String outTradeNo, String subject) {
        BizContent content = new BizContent(totalAmount, outTradeNo, subject);
        String orderStr = "app_id=" + PayConstant.ALI_APP_ID +
                "&biz_content=" + content.toString() +
                "&charset=utf-8" +
                "&format=json" +
                "&method=alipay.trade.app.pay" +
                "&notify_url=" + notifyUrl +
                "&timestamp=" + timestamp() +
                "&version=1.0";
        return orderStr;
    }

    public static Map<String, String> params(String notifyUrl, double totalAmount, String outTradeNo, String subject) {
        BizContent content = new BizContent(totalAmount, outTradeNo, subject);
        Map<String, String> map = new HashMap<String, String>();
        map.put("app_id", PayConstant.ALI_APP_ID);
        map.put("biz_content", content.toString());
        map.put("charset", "utf-8");
        map.put("format", "json");
        map.put("method", "alipay.trade.app.pay");
        map.put("notify_url", notifyUrl);
        map.put("timestamp", timestamp());
        map.put("version", "1.0");
//        String orderStr="app_id="+PayConstant.ALI_APP_ID +
//                "&biz_content="+ content.toString()+
//                "&charset=utf-8" +
//                "&format=json" +
//                "&method=alipay.trade.app.pay" +
//                "&notify_url=" +notifyUrl+
//                "&timestamp="+timestamp()+
//                "&version=1.0";
        return map;
    }

//    String payInfo = AliPay.payInfo(oldOrder.getSerialNumber(), "母婴商品", "母婴商品", payPrice , PayConstant.ALI_NOTIFY_URL);
    public static String orderInfo(String notifyUrl, double totalAmount, String outTradeNo, String subject) {
        BizContent content = new BizContent(totalAmount, outTradeNo, subject);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(PayConstant.ALI_APP_ID, content.toString(),notifyUrl);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, PayConstant.ALI_RSA_PRIVATE);
        String orderInfo = orderParam + "&" + sign;
        return orderInfo;
    }


}
