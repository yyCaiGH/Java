package com.up.common.pay;

import com.jfinal.kit.PropKit;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 21:01
 */
public class PayConstant {

//    public static final String HTTP_HEAD="http://ycss.tunnel.qydev.com";
    public static final String HTTP_HEAD="http://139.224.192.136:8080";
    //支付宝
    //service 接口名称，固定值。
    public static final String ALI_SERVICE_APP="mobile.securitypay.pay";
    //partner 签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
    public static final String ALI_PARTNER="2088521150256472";
    //seller_id 卖家支付宝账号（邮箱或手机号码格式）或其对应的支付宝唯一用户号（以2088开头的纯16位数字）。
    public static final String ALI_SELLER_ID=ALI_PARTNER;
    // 商户私钥，pkcs8格式x
    public static final String ALI_RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK0MQ16LrgiZeuU5" +
            "JuIP7iJsVSc51nhr0dcBD69tH97GpmL7gUICa5aNCoi49fQQDjhwRpYtuSURxLEE" +
            "ezeHUgGVdXYvHH3EN0OICAxrfmoG2XA5meIod4VB9mwk3afnVk2CCtbknD7C9CW8" +
            "R1WSi5kkwti74WmVB8n57xcsq/pnAgMBAAECgYBAtw5WxEvwYV2JbecxsZ1v+C2c" +
            "PtALNt9B1d7Ezz2U9Ztx9vau9fLAWbyvxuQMQxjeWHa9YRoVV+DEBpYB/TRYT8js" +
            "infNLwjwbxHtmRS8j8womdQOXub0ABr52rtPg3+C1tCI1iC6EmZb8+0q/BSm0kQs" +
            "49n6y60ttQ6Gtn4OgQJBANkc6jCd5qv8ayOMxwqJzYZZMBJQL8iw7Lb6SUau4T1J" +
            "bzW/Q+7pDm2GWYXKedPmhB+lcVSOUTnV7KS/MrJ5OycCQQDMCt+VHs0rvXejmQWx" +
            "0XzxDOE9FHy1GUfX1yvX5JzqwfZ0SSeI9cuezTCDunGhQYp5+aNbw1+oipTkq2ih" +
            "C87BAkBpYZGCW4IVRIW0UaZixRRO+4dLvEQVQ1sCTWW3uzZ0iupByj5s/g4UtztI" +
            "p0iOw7qpqaHiitl+GwmrxWoUGqfNAkB4P6Q6freNopCCgqkffYUzjlKjq5I/LFHx" +
            "uyZkip+Lnpm/OGmrRqgkw2pnB5b14OlaLyv95WtXqaxMA1SbLJlBAkAjwr/pZGPK" +
            "O7ZLBQZWnLVKiwoVxaByk+tZ1PWWIMyD50sYaUOIGOdnrpQqOCE/ZjCeKCPw7wBw" +
            "UCxPhBzgMNju";
//    public static final String ALI_RSA_PRIVATE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

//    public static final String ALI_APP_ID="2016122604635657";
    public static final String ALI_APP_ID="2016122504598868";

    /**
     * 支付宝回掉地址
     */
    public static final String ALI_NOTIFY_URL= HTTP_HEAD+"/pay/aliOrderPay";


    //微信
    //appid 微信开放平台审核通过的应用APPID
    public static  final String WX_APP_ID="";
    //mch_id 微信支付分配的商户号
    public static  final String WX_MCH_ID="";
    //sign_type 签名类型
    public static final String WX_SIGN_TYPE="MD5";
    //key 商户号对应的密钥
    public static String WX_PARTNER_KEY = "db426a9829e4b49a0dcac7b4162da6d3";//商户号对应的密钥
    /**
     * 支付宝回掉地址
     */
    public static final String WX_NOTIFY_URL="";















//    //_input_charset 商户网站使用的编码格式，固定为UTF-8。
//    public static final String ALI_INPUT_CHAT="UTF-8";
//
//    //sign_type 签名类型，目前仅支持RSA。
//    public static final String ALI_SIGN_TYPE="RSA";
//
//    //sign 请参见签名。
//    public static String ALI_SIGN="UTF-8";
//
//    //notify_url 支付宝服务器主动通知商户网站里指定的页面http路径。
//    public static final String ALI_NOTIFY_URL="";
//
//    //app_id 标识客户端。 可空
//    public static final String ALI_APP_ID="";
//
//    //appenv 标识客户端来源。参数值内容约定如下：appenv=”system=客户端平台名^version=业务系统版本”。 可空
//    public static final String ALI_APPENV="";
//
//    //out_trade_no 支付宝合作商户网站唯一订单号。
//    public static final String ALI_OUT_TRADE_NO="";
//
//    //subject 商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字。
//    public static final String ALI_SUBJECT="";
//
//    //payment_type 支付类型。默认值为：1（商品购买）。
//    public static final String ALI_PAYMENT_TYPE="1";
//
//
//
//    //total_fee 该笔订单的资金总额，单位为RMB-Yuan。
//    public static final String ALI_TOTAL_FEE="";
//
//    //body 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
//    public static final String ALI_BODY="";
//
//    //goods_type 具体区分本地交易的商品类型。1：实物交易；0：虚拟交易。默认为1（实物交易）。 可空
//    public static final String ALI_GOODS_TYPE="1";
//
//    //hb_fq_param Json格式。 可空
//    //hb_fq_num：花呗分期数，比如分3期支付；
//    //hb_fq_seller_percent：卖家承担收费比例，比如100代表卖家承担100%。
//    //两个参数必须一起传入。
//    //具体花呗分期期数和卖家承担收费比例可传入的数值请咨询支付宝。
//    public static final String ALI_HB_FQ="";
//
//    //rn_check  T：发起实名校验  F：不发起实名校验。 可空
//    public static final String ALI_RN_CHECK="";
//
//    //it_b_pay 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。 可空
//    public static final String ALI_IT_B_PAY="1";
//
//    //extern_token  开放平台返回的包含账户信息的token  可空
//    public static final String ALI_TOKEN="1";
//
//    //promo_params 商户与支付宝约定的营销参数，为Key:Value键值对，如需使用，请联系支付宝技术人员。 可空
//    public static final String ALI_PROMP="1";

}
