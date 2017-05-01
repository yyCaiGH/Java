package com.up.common.pay.returns;
/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_user_id = partner;

	// 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIIBOwIBAAJBANq2+/DnjTr4x2gMy+8xkEVaieNaevyGWR8IMPyEeVL7Ctml7Htk" +
			"dmut/EszWE2uIH5DV9ns6y7yxTp527js85kCAwEAAQJAF7YY0EA6Nnd8HtKsVU05" +
			"R9uN8WEAzlm+Vbfsn6O5B37Og3r5GvenEvIUMlLYkWJY94KStHkD20Gr2miSzF6c" +
			"JQIhAPsp4yFH081LR7E266er59Y2Xd07zOf+kvMq4ICLjqTbAiEA3u0j4EgciAdR" +
			"B6aZqhDlqRVcjbZiTC2LfHLQHaNtWZsCIQDOOJxHDSS4oSDiPsfKyfj3B3iWV61s" +
			"preddQ0UPoIGlQIgSBsQNaVdhSpIrV7X6UhZ4J0GONg7Z0R8D57bbhrEbe0CIQD5" +
			"ogXW/+/miungoQZ6bJqM7cPIKnCUl/aVd9Hk1Ite3w==";
	
	// 支付宝公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm 
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "C:\\";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
		
	 //退款日期 时间格式 yyyy-MM-dd HH:mm:ss
    public static String refund_date = UtilDate.getDateFormatter();
		
	// 调用的接口名，无需修改
	public static String service = "refund_fastpay_by_platform_pwd";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}

