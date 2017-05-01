package com.up.common.controller;

import com.up.common.utils.MobileUtils;
import com.up.common.utils.SmsParm;
import com.up.common.utils.SmsUtils;
import com.up.infant.model.Member;

/**
 * TODO:短信控制器
 * <p>
 * Created by 王剑洪 on 2016/10/31.
 */
public class SmsController extends BaseController {


    /**
     * 发送验证码
     * parm：电话号码
     */
    public void getSmsCode() {
        Integer type = getParaToInt("type");
        String phone = getPara("phone");
        if (null == type) {//注册或者绑定用
            if (!MobileUtils.is(phone)) {
                resFailure("请输入正确的手机号！");
                return;
            }
            Member member = Member.dao.findFirst("SELECT *  FROM t_member WHERE phone=" + phone);
            if (null != member) {
                resFailure("该手机已注册！");
                return;
            }
        }
        String code = SmsUtils.sendSmsAndGetCode(phone);
        getSession().setAttribute("smsCode", code);
        getSession().setAttribute("phone", phone);
        resSuccess(code);
    }

    public void sendSmsTest() {
        SmsParm parm = new SmsParm();
        parm.setPhone("18650346869");
        parm.setCouponTitle("元旦大放血");
        parm.setCouponCode("1020");
        parm.setSmsType(0);
        parm.setUserName("cyy");
        SmsUtils.sendSms(parm);
        resSuccess("0");
    }
}
