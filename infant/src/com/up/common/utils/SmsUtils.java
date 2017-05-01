package com.up.common.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.util.Random;

/**
 * 阿里大鱼 短信验证，发送(柚皮企业账户)
 * 
 * @author yy_cai
 */
public class SmsUtils {

	// 官网的URL
	private final static String url = "http://gw.api.taobao.com/router/rest";
	// 成为开发者，创建应用后系统自动生成
	/*private final static String appkey = "23396887";
	private final static String secret = "ecb6b3b9f616a3864de6cc317df9f942";
	private final static String signName = "yy测试";
	private final static String TemplateCode = "SMS_11055757";//获取验证码
	private final static String product = "母婴商城";*/
	private final static String appkey = "23578807";
	private final static String secret = "6a869969835fdc22dd6f1c44baef4940";
	private final static String signName = "注册验证";
	private final static String TemplateCode = "SMS_33965234";//获取验证码
	private final static String signName_0 = "活动验证";//
	private final static String TemplateCode_0 = "SMS_35420001";//赠送优惠券给用户
	private final static String product = "母婴商城";
	/**
	 * 发送短信获取验证码
	 * @param phone 电话号码
	 * @return 验证码
	 */
	public static String sendSmsAndGetCode(String phone) {
		String code = random(4);
		String template = "{\"code\":\"" + code + "\",\"product\":\"" + product + "\"}";

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");// 可选
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		req.setSmsParamString(template);
		req.setRecNum(phone);
		req.setSmsTemplateCode(TemplateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			System.out.println("验证码发送返回：" + rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 发送短信
	 * @param parm 短信参数
	 */
	public static void sendSms(SmsParm parm) {
		String templateCode = "";
		String template="";
		switch (parm.getSmsType()) {
			//尊敬的${userName},母婴商城赠送您优惠券${couponTitle},优惠券码${couponCode},请到平台使用。
			case 0:
				templateCode = TemplateCode_0;
				template = "{\"userName\":\"" + parm.getUserName() + "\",\"couponTitle\":\"" + parm.getCouponTitle()+ "\",\"couponCode\":\"" + parm.getCouponCode() + "\"}";
				break;
			default:
				break;
		}
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");// 可选
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName_0);
		req.setSmsParamString(template);
		req.setRecNum(parm.getPhone());
		req.setSmsTemplateCode(templateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			System.out.println("请求短信发送返回：" + rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 产生一个随机n位数
	 * @param n
	 * @return
	 */
	private  static String random(int n) {
		if (n < 1 || n > 10) {
			throw new IllegalArgumentException("cannot random " + n + " bit number");
		}
		Random ran = new Random();
		if (n == 1) {
			return String.valueOf(ran.nextInt(10));
		}
		int bitField = 0;
		char[] chs = new char[n];
		for (int i = 0; i < n; i++) {
			while(true) {
				int k = ran.nextInt(10);
				if( (bitField & (1 << k)) == 0) {
					bitField |= 1 << k;
					chs[i] = (char)(k + '0');
					break;
				}
			}
		}
		return new String(chs);
	}
}
