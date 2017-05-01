package com.up.infant.controller.pay;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 23:09
 */
public class PayController extends BaseController {

    PayLogic logic = new PayLogic();

    /**
     * 支付宝订单支付回掉
     */
    public void aliOrderPay() {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = getRequest().getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }


        try {
            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            //商户订单号
            String orderSn = new String(getRequest().getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String paySn = new String(getRequest().getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //交易状态
            String trade_status = new String(getRequest().getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
            String realPay = new String(getRequest().getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
            if (trade_status.equals("TRADE_SUCCESS")){//交易支付成功
                boolean res = logic.orderPay(orderSn, 1, Double.parseDouble(realPay), paySn);
                renderText("success");
                return;
            }else {
                renderText("fail");
                return;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            renderText("no notify message");
        }
    }

    public void orderPayTest() {
        String orderSn = getPara("orderSn");
        int payType = getParaToInt("payType");
        String realPayStr = getPara("realPay");
        String paySn = getPara("paySn");
//        double realPay = 20.00;
        double realPay = Double.parseDouble(realPayStr);
//        String paySn = "2016121115541066821";
        if (TextUtils.isEmpty(paySn)) {
            res(ResCode.PARAM_ERROR);
            return;
        }
        boolean res = logic.orderPay(orderSn, payType, realPay, paySn);
        resSuccess(res);
    }


}
