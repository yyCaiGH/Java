package com.up.common.pay.wx;

import com.up.common.pay.PayConstant;
import com.up.common.utils.IpUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 22:37
 */
public class WxPay {

    public static Map payInfo(String orderNumber, String body, String totalFee, String notify_url) throws Exception {
        String xmlParams=xmlParams(orderNumber,body,totalFee,notify_url);
        String res = HtmlUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", xmlParams);

        return doXMLParse(res);
    }

    /**
     * 字符参数生成
     * @param orderNumber
     * @param body
     * @param totalFee
     * @param notify_url
     * @return
     */
    public static String xmlParams(String orderNumber, String body, String totalFee, String notify_url) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("trade_type", "APP"); // 交易类型，app
        paramMap.put("spbill_create_ip", IpUtils.localIp()); // 本机的Ip
        paramMap.put("body", body); // 商品描述
        paramMap.put("out_trade_no", orderNumber); // 商户 后台的贸易单号
        paramMap.put("total_fee", "" + totalFee); // 金额必须为整数 单位为分
        paramMap.put("notify_url", notify_url); // 支付成功后，回调地址
        paramMap.put("appid", PayConstant.WX_APP_ID); // appid
        paramMap.put("mch_id", PayConstant.WX_MCH_ID); // 商户号
        paramMap.put("nonce_str", getRandomString(32)); // 随机数
        paramMap.put("sign", createSign(paramMap));// 根据微信签名规则，生成签名
        return mapToXml(paramMap);
    }


    /**
     * 随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * 签名生成，使用默认MD5签名
     *
     * @param paramMap
     * @return
     */
    public static String createSign(HashMap<String, String> paramMap) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>(paramMap);
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + PayConstant.WX_PARTNER_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * map转XML
     *
     * @param map
     * @return
     */
    public static String mapToXml(HashMap<String, String> map) {
        String str = "<xml>";
        Set set = map.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (null != map.get(key) && !"".equals(map.get(key))) {
                str = str + "<" + key + ">" + map.get(key) + "</" + key + ">";
            }
        }
        return str + "</xml>";
    }

    /**
     * 字符串转map
     * @param strxml
     * @return
     * @throws Exception
     */
    public static Map doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            m.put(k, v);
        }
        // 关闭流
        in.close();
        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

}
