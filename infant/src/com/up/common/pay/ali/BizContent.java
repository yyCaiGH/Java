package com.up.common.pay.ali;

import com.google.gson.Gson;
import com.up.common.pay.PayConstant;

/**
 * TODO:
 * <p>
 * Created by 王剑洪 on 2016/12/29.
 */
public class BizContent {
    String timeout_express;//该笔订单允许的最晚付款时间，逾期将关闭交易
    String seller_id;//收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
    String product_code;//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
    String total_amount;//订单总金额，单位为元，
    String body;//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
    String out_trade_no;//商户网站唯一订单号
    String subject;//商品的标题/交易标题/订单标题/订单关键字等。


    public BizContent(double total_amount, String out_trade_no, String subject) {
        this.timeout_express = "30m";
        this.seller_id = PayConstant.ALI_SELLER_ID;
        this.product_code = "QUICK_MSECURITY_PAY";
        this.total_amount = total_amount+"";
        this.out_trade_no = out_trade_no;
        this.subject = subject;
        this.body="测试";
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
