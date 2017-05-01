package com.up.infant.controller.yorder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.up.common.def.ResCode;
import com.up.common.pay.PayConstant;
import com.up.common.pay.ali.AliPay;
import com.up.common.pay.wx.WxPay;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;
import com.up.infant.model.params.OrderSubmit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * TODO:
 * Created by yyCai
 * on 2016/12/10 0010. 22:49
 */
public class YorderLogic {

    public int addOrder(String pOrderJson, String skuGoodsListJson) {
        //添加主订单
        JSONObject orderJson = (JSONObject) JSONValue.parse(pOrderJson);
        Order order = new Order();
        try{
            String member_id = orderJson.get("member_id").toString();
            order.setMemberId(Integer.parseInt(member_id));
            String member_addr_id = orderJson.get("member_addr_id").toString();
            order.setMemberAddrId(TextUtils.toInteger(member_addr_id));
            String count = orderJson.get("count").toString();
            order.setCount(Integer.parseInt(count));
            String price = orderJson.get("price").toString();
            order.setPrice(Double.parseDouble(price));
            String real_price = orderJson.get("real_price").toString();
            order.setRealPrice(Double.parseDouble(real_price));
            String vip_discounts = orderJson.get("vip_discounts").toString();
            order.setVipDiscounts(TextUtils.toDouble(vip_discounts));
            String delivery_type = orderJson.get("delivery_type").toString();
            order.setDeliveryType(TextUtils.toInteger(delivery_type));
            String coupon_amount = orderJson.get("coupon_amount").toString();
            order.setCouponAmount(TextUtils.toDouble(coupon_amount));
            String postage_price = orderJson.get("postage_price").toString();
            order.setPostagePrice(TextUtils.toDouble(postage_price));
            String pay_type = orderJson.get("pay_type").toString();
            order.setPayType(TextUtils.toInteger(pay_type));
            String type = orderJson.get("type").toString();
            order.setType(TextUtils.toInteger(type));
            String from = orderJson.get("from").toString();
            order.setFrom(TextUtils.toInteger(from));
            String expressage = orderJson.get("expressage").toString();
            order.setExpressage(expressage);
            String contact = orderJson.get("contact").toString();
            order.setContact(contact);
            String phone = orderJson.get("phone").toString();
            order.setPhone(phone);
            String province = orderJson.get("province").toString();
            order.setProvince(province);
            String city = orderJson.get("city").toString();
            order.setCity(city);
            String area = orderJson.get("area").toString();
            order.setArea(area);
            String address = orderJson.get("address").toString();
            order.setAddress(address);
            String leave_word = orderJson.get("leave_word").toString();
            order.setLeaveWord(leave_word);
            order.setStatus(1);//1、已下单待付款
            order.setTimeCreate(new Date());//下单时间
            order.setSerialNumber(createOrderSn());//订单编号
            order.save();

        }catch (Exception e){
            return -1;
        }

        int orderId = order.getId();

        try{
            //批量添加sku商品
            JSONArray array=(JSONArray)JSONValue.parse(skuGoodsListJson);
            List<OrderGoods> list = new ArrayList<OrderGoods>();
            int goodsRepertory = 0;//主商品库存
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                OrderGoods orderGoods = new OrderGoods();
                orderGoods.setOrderId(orderId);
                String id = json.get("id").toString();
                orderGoods.setSkuGoodsId(TextUtils.toInteger(id));
                String img1_url = json.get("img1_url").toString();
                orderGoods.setImgUrl(img1_url);
                String promote_price = json.get("promote_price").toString();
                orderGoods.setDisPrice(TextUtils.toDouble(promote_price));
                String market_price = json.get("market_price").toString();
                if (TextUtils.isDouble(market_price)){
                    orderGoods.setPrice(Double.parseDouble(market_price));
                }
                String goods_number = json.get("goods_number").toString();
                orderGoods.setGoodsSn(goods_number);
                String sku_info = json.get("sku_info").toString();
                orderGoods.setSkuInfo(sku_info);
                String count = json.get("count").toString();
                orderGoods.setCount(TextUtils.toInteger(count));
                list.add(orderGoods);
            }
            OrderGoods.dao.addBatch(list);

        }catch (Exception e){
            return -2;
        }

        return 0;
    }

    /**
     * 订单好生成年月日时分秒毫秒+两位随机数
     *
     * @return
     */
    public String createOrderSn() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String date = sdf.format(new Date());
        int x = 10 + (int) (Math.random() * 89);
        String sn = date + x;
        return sn;
    }
}
