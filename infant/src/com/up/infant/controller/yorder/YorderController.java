package com.up.infant.controller.yorder;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.pay.PayConstant;
import com.up.common.pay.returns.AlipayConfig;
import com.up.common.pay.returns.AlipaySubmit;
import com.up.common.pay.returns.ApiUtils;
import com.up.infant.controller.order.OrderLogic;
import com.up.infant.model.*;
import com.up.infant.model.res.AliRes;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO:
 * Created by yyCai
 * on 2016/12/10 0010. 22:42
 * yorder
 */
public class YorderController extends BaseController{
    YorderLogic logic=new YorderLogic();
    /**
     * 订单提交
     */
    public void submitOrder(){
        String orderJson = getPara("order");
        String skuGoodsListJson = getPara("skuGoodsList");
        int res = logic.addOrder(orderJson,skuGoodsListJson);
        if (res==-1){
            resFailure(ResCode.ERROR.getCode(),"订单添加失败！");
        }
        else if (res==-2){
            resFailure(ResCode.ERROR.getCode(),"批量添加sku商品失败！");
        }
        else {
            res(ResCode.SAVE_SUCCESS);
        }
    }

    /**
     * 订单列表与退货入库记录混用
     */
    public void getPageList(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            String searchValue = getPara("searchValue");//包括昵称，手机号，订单编号
            Boolean isReturns = Boolean.parseBoolean(getPara("isReturns"));
            String minTime = getPara("minTime");
            String maxTime = getPara("maxTime");
            Order order=getModel(Order.class,"",true);
            Page<Order> goodsPage = Order.dao.getPageList(isReturns,order,searchValue,minTime,maxTime,pageNo,pageSize);
            resSuccess(goodsPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 获取订单信息，及订单商品列表
     */
    public void getOrderInfo(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            Order order =  Order.dao.getOrder(orderId);
            List<OrderGoods> orderGoodsList = OrderGoods.dao.getOrderGoods(orderId);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("order",order);
            map.put("orderGoodsList",orderGoodsList);
            resSuccess(map);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    //卖家发货
    public void mjfh(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            String logistics_number = getPara("logistics_number");
            String deliver_reamrk = getPara("deliver_reamrk");
            String expressage = getPara("expressage");
            Order order = Order.dao.findById(orderId);
            order.setStatus(3);
            order.setLogisticsNumber(logistics_number);
            order.setDeliverReamrk(deliver_reamrk);
            order.setExpressage(expressage);
            order.setTimeDeliver(new Date());
            order.update();
            resSuccess(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    //买家申请退货
    public void mjsqth(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            Order order = Order.dao.findById(orderId);
            order.setStatus(6);
            order.setStatusReturns(1);
            order.update();
            resSuccess(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    //卖家同意退货
    public void mjtyth(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            String returns_address = getPara("returns_address");
            String returns_remark = getPara("returns_remark");
            Order order = Order.dao.findById(orderId);
            order.setStatus(6);
            order.setStatusReturns(2);
            order.setReturnsAddress(returns_address);
            order.setReturnsRemark(returns_remark);
            order.setTimeReturnsAgree(new Date());
            order.update();

            //更新买家的退货次数
            Member member = Member.dao.getById(order.getMemberId());
            member.setRefundCount(member.getRefundCount()+1);
            member.update();

            resSuccess(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    //卖家确认收货(废弃)
    public void mjqrsh(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            String returns_receiving_ramark = getPara("returns_receiving_ramark");
            Order order = Order.dao.findById(orderId);
            order.setStatus(6);
            order.setStatusReturns(4);
            order.setReturnsReceivingRamark(returns_receiving_ramark);
            order.setTimeReturnsFinish(new Date());
            order.update();
            //退货入库
            List<OrderGoods> orderGoodsList = OrderGoods.dao.getOrderGoods(orderId);
            for (int i = 0;i<orderGoodsList.size();i++){
                SkuGoods skuGoods = SkuGoods.dao.findById(orderGoodsList.get(i).getSkuGoodsId());
                skuGoods.setRepertory(skuGoods.getRepertory()+ orderGoodsList.get(i).getCount());
                skuGoods.update();
            }
            //String html = payReturn(orderId+"");
            resSuccess("退货完成");
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 申请原路返回退款
     */
    public void payReturn(){
       /* String viewName = PathConstant.ORDERFORM + "refund.html";
        ModelAndView mv = new CustomModelAndView(viewName, request, response);*/

        String orderId = getPara("orderId");

        String returns_receiving_ramark = getPara("returns_receiving_ramark");
        Order order = Order.dao.findById(orderId);
        order.setReturnsReceivingRamark(returns_receiving_ramark);
        order.update();
        //退货入库
        List<OrderGoods> orderGoodsList = OrderGoods.dao.getOrderGoods(Integer.parseInt(orderId));
        for (int i = 0;i<orderGoodsList.size();i++){
            SkuGoods skuGoods = SkuGoods.dao.findById(orderGoodsList.get(i).getSkuGoodsId());
            skuGoods.setRepertory(skuGoods.getRepertory()+ orderGoodsList.get(i).getCount());
            skuGoods.update();
        }
        try {
//            Map<String, Object> map = orderformService.getOrderById(orderId);
            //Order order = Order.dao.findById(orderId);
//            String orderSn = (String) map.get("order_sn");
            String orderSn = order.getSerialNumber();
//            String aliSn = (String) map.get("ali");
            String aliSn = order.getPaySn();
//            String sumAmount = ((BigDecimal) map.get("refundMoney")).toString();//退款金额
            String sumAmount = order.getRealPrice().toString();

//            AliRes res = StudentNumber.getAliPayParams();
            //三个参数等待支付宝申请好
           /* AlipayConfig.private_key = res.getResult().getPrivate_key();
            String sellerId = res.getResult().getSeller_id();
            String partner = res.getResult().getPartner();*/
            AlipayConfig.private_key = PayConstant.ALI_RSA_PRIVATE;
            String sellerId = PayConstant.ALI_SELLER_ID;
            String partner = PayConstant.ALI_PARTNER;
            //批次号，必填，格式：当天日期[8位]+序列号[3至24位]，如：201603081000001
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
            String batch_no = sFormat.format(new Date()) + orderSn;
            //退款笔数，必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）
            String batch_num = "1";
            //退款详细数据(WIDdetail_data)，必填(支付宝交易号^退款金额^备注)多笔请用#隔开
            String detail_data = aliSn + "^" + sumAmount + "^" + "协商退款";
            Map<String, String> sParaTemp = ApiUtils.reFoundApi(partner, partner, batch_no, batch_num, detail_data);
            String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
            System.out.println("html："+sHtmlText);
            resSuccess("申请原路返回退款",sHtmlText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //退款通知
    public void refundNotify(){
        System.out.println("退款成功！");
        /* Map requestParams = getRequest().getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
        }*/

        try {
            //批次号，必填，格式：当天日期[8位]+序列号[3至24位]，如：201603081000001
            String batch_no = new String(getRequest().getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");
            String orderSn  = batch_no.substring(8,batch_no.length());
            Order order = Order.dao.getOrderBySn(orderSn);
            order.setStatus(6);
            order.setStatusReturns(4);
            order.setTimeReturnsFinish(new Date());
            order.update();
            renderText("success");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            renderText("no notify message");
        }
    }
    //关闭交易
    public void closeOrder(){
        try {
            int orderId = Integer.parseInt(getPara("id"));
            Order order = Order.dao.findById(orderId);
            order.setStatus(7);
            order.update();
            resSuccess(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 根据品牌id获取对应的销售明细
     */
    public void getOrderGoodsList(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            int brandId = Integer.parseInt(getPara("brandId"));
            Page<OrderGoods> orderGoodsPage = OrderGoods.dao.getPage(brandId,pageNo,pageSize);
            resSuccess(orderGoodsPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

}
