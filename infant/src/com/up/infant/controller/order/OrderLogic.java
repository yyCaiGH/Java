package com.up.infant.controller.order;

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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/10 0010. 22:49
 */
public class OrderLogic {
    Cart cartDao = new Cart();
    SkuGoods skuGoodsDao = new SkuGoods();
    Member memberDao = new Member();
    Order orderDao = new Order();
    OrderGoods orderGoodsDao = new OrderGoods();
    VipGrade vipDao = new VipGrade();
    MemberCoupon memberCouponDao = new MemberCoupon();

    /**
     * 添加到购物车
     *
     * @param cart
     */
    public ResCode addToCart(Cart cart) {
        Cart oldCart = cartDao.getByMemberIdAndSkuId(cart.getMemberId(), cart.getSkuGoodsId());
        SkuGoods skuGoods = skuGoodsDao.findById(cart.getSkuGoodsId());
        Member member = memberDao.findById(cart.getMemberId());
        if (null == member) {//用户不存在
            return ResCode.MEMBER_NO;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            return ResCode.STATUS_ERROR;
        }
        if (null == skuGoods) {//对应属性不存在
            return ResCode.CART_SKU_NULL;
        }
        if (skuGoods.getPutaway() == 2) {//已下架了
            return ResCode.CART_SKU_SHELVES;
        }
        if (null == oldCart ?
                (skuGoods.getRepertory() < cart.getCount()) : (skuGoods.getRepertory() < (cart.getCount() + oldCart.getCount()))) {
            return ResCode.CART_SKU_REPERTORY;
        }
        if (null == oldCart) {//购物车的对应商品不存在
            cart.setCreateTime(new Date());
            cart.save();
        } else {//购物车的对应商品已存在
            oldCart.setCount(cart.getCount() + oldCart.getCount());
            oldCart.update();
        }
        return ResCode.CART_ADD;
    }

    public List<Cart> getCart(int memberId) {
        List<Cart> cartList = cartDao.getCart(memberId);
        return cartList;
    }

    public ResCode delCart(int memberId, int cartId) {
        Member member = memberDao.findById(memberId);
        if (null == member) {//用户不存在
            return ResCode.MEMBER_NO;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            return ResCode.STATUS_ERROR;
        }
        Cart cart = cartDao.getByMemberIdAndCartId(memberId, cartId);
        if (null == cart) {
            return ResCode.CART_NULL;
        }
        cart.delete();
        return ResCode.SUCCESS;
    }

    public ResCode updateCart(Cart cart) {
        Member member = memberDao.findById(cart.getMemberId());
        if (null == member) {//用户不存在
            return ResCode.MEMBER_NO;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            return ResCode.STATUS_ERROR;
        }
        Cart oldCart = cartDao.getByMemberIdAndCartId(cart.getMemberId(), cart.getId());
        if (null == oldCart) {
            return ResCode.CART_NULL;
        }
        SkuGoods skuGoods = skuGoodsDao.findById(oldCart.getSkuGoodsId());
        if (skuGoods.getPutaway() == 2) {//已下架了
            return ResCode.CART_SKU_SHELVES;
        }
        if (skuGoods.getRepertory() < cart.getCount()) {
            return ResCode.CART_SKU_REPERTORY;
        }
        oldCart.setCount(cart.getCount());
        cart.update();
        return ResCode.SUCCESS;
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

    /**
     * 订单提交
     *
     * @param submit
     * @return
     */
    public Map<String, Object> submitOrder(String submit) {
        Type type = new TypeToken<OrderSubmit>() {
        }.getType();
        Map<String, Object> map = new HashMap<String, Object>();
        OrderSubmit orderSubmit = new Gson().fromJson(submit, type);
        Member member = memberDao.findById(orderSubmit.getMemberId());

        if (null == member) {//用户不存在
            map.put("res", ResCode.MEMBER_NO);
            return map;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            map.put("res", ResCode.STATUS_ERROR);
            return map;
        }
        List<OrderSubmit.CartItem> cart = orderSubmit.getCart();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        double totalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            Cart oldCart = cartDao.getByMemberIdAndCartId(orderSubmit.getMemberId(), cart.get(i).getCartId());
            if (null == oldCart) {
                map.put("res", ResCode.CART_NULL);
                return map;
            }
            SkuGoods skuGoods = skuGoodsDao.getSkuGoodsInfo(oldCart.getSkuGoodsId());
            if (null==skuGoods){
                map.put("res", ResCode.CART_SKU_SHELVES);
                return map;
            }
            if (skuGoods.getPutaway() == 2) {//已下架了
                map.put("res", ResCode.CART_SKU_SHELVES);
                return map;
            }
            if (skuGoods.getRepertory() < cart.get(i).getCount()) {//库存不足
                map.put("res", ResCode.CART_SKU_REPERTORY);
                return map;
            }
            String skuInfo = "";
            if (!TextUtils.isEmpty(skuGoods.getStr("type_name1"))) {
                skuInfo += skuGoods.getStr("name1");
            }
            if (!TextUtils.isEmpty(skuGoods.getStr("type_name2"))) {
                String name2 = skuGoods.getStr("name2");
                if (!TextUtils.isEmpty(skuInfo)) {
                    name2 = "/" + name2;
                }
                skuInfo += name2;
            }
            if (!TextUtils.isEmpty(skuGoods.getStr("type_name3"))) {
                String name3 = skuGoods.getStr("name3");
                if (!TextUtils.isEmpty(skuInfo)) {
                    name3 = "/" + name3;
                }
                skuInfo += name3;
            }
            if (!TextUtils.isEmpty(skuGoods.getStr("type_name4"))) {
                String name4 = skuGoods.getStr("name4");
                if (!TextUtils.isEmpty(skuInfo)) {
                    name4 = "/" + name4;
                }
                skuInfo += name4;
            }
            if (!TextUtils.isEmpty(skuGoods.getStr("type_name5"))) {
                String name5 = skuGoods.getStr("name5");
                if (!TextUtils.isEmpty(skuInfo)) {
                    name5 = "/" + name5;
                }
                skuInfo += name5;
            }
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setSkuGoodsId(oldCart.getSkuGoodsId());
            orderGoods.setCount(cart.get(i).getCount());
            orderGoods.setPrice(skuGoods.getMarketPrice());
            orderGoods.setDisPrice(skuGoods.getPromotePrice());
            orderGoods.setSkuInfo(skuInfo);
            orderGoods.setGoodsSn(skuGoods.getGoodsNumber());
            totalPrice += (skuGoods.getPromotePrice() * cart.get(i).getCount());
            orderGoodsList.add(orderGoods);
        }

        Order order = new Order();
        order.setSerialNumber(createOrderSn());//订单编号
        order.setMemberId(member.getId());//用户ID
        order.setPrice(totalPrice);//总价
        VipGrade vipGrade = vipDao.findById(member.getVipGradeId());
        if (null != vipGrade) {
            order.setVipDiscounts(vipGrade.getDiscount());//会员折扣
        }
        order.setCount(cart.size());
        order.setType(2);//2、自主下单
        order.setFrom(orderSubmit.getFrom());
        order.setStatus(1);//1、已下单待付款
        order.setTimeCreate(new Date());//下单时间
        order.save();
        for (int i = 0; i < orderGoodsList.size(); i++) {
            orderGoodsList.get(i).setOrderId(order.getId());
        }
        try {
            Db.batchSave(orderGoodsList, orderGoodsList.size());
            List<OrderGoods> list = orderGoodsDao.getList3(order.getId());
            order.put("goods", list);
            order.put("memberRebates", getMemberRebates(member.getId()));
            map.put("res", ResCode.SUCCESS);
            map.put("order", order);
            return map;
        } catch (Exception e) {
            order.delete();
            map.put("res", ResCode.ERROR);
            return map;
        }
    }

    public Map<String, Object> buyNow(int memberId,int skuGoodsId,int count,int from){
        Map<String, Object> map = new HashMap<String, Object>();
        Member member = memberDao.findById(memberId);

        if (null == member) {//用户不存在
            map.put("res", ResCode.MEMBER_NO);
            return map;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            map.put("res", ResCode.STATUS_ERROR);
            return map;
        }
        SkuGoods skuGoods = skuGoodsDao.getSkuGoodsInfo(skuGoodsId);
        if (null==skuGoods){
            map.put("res", ResCode.CART_SKU_SHELVES);
            return map;
        }
        if (skuGoods.getPutaway() == 2) {//已下架了
            map.put("res", ResCode.CART_SKU_SHELVES);
            return map;
        }
        if (skuGoods.getRepertory() < count) {//库存不足
            map.put("res", ResCode.CART_SKU_REPERTORY);
            return map;
        }
        String skuInfo = "";
        if (!TextUtils.isEmpty(skuGoods.getStr("type_name1"))) {
            skuInfo += skuGoods.getStr("name1");
        }
        if (!TextUtils.isEmpty(skuGoods.getStr("type_name2"))) {
            String name2 = skuGoods.getStr("name2");
            if (!TextUtils.isEmpty(skuInfo)) {
                name2 = "/" + name2;
            }
            skuInfo += name2;
        }
        if (!TextUtils.isEmpty(skuGoods.getStr("type_name3"))) {
            String name3 = skuGoods.getStr("name3");
            if (!TextUtils.isEmpty(skuInfo)) {
                name3 = "/" + name3;
            }
            skuInfo += name3;
        }
        if (!TextUtils.isEmpty(skuGoods.getStr("type_name4"))) {
            String name4 = skuGoods.getStr("name4");
            if (!TextUtils.isEmpty(skuInfo)) {
                name4 = "/" + name4;
            }
            skuInfo += name4;
        }
        if (!TextUtils.isEmpty(skuGoods.getStr("type_name5"))) {
            String name5 = skuGoods.getStr("name5");
            if (!TextUtils.isEmpty(skuInfo)) {
                name5 = "/" + name5;
            }
            skuInfo += name5;
        }
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setSkuGoodsId(skuGoodsId);
        orderGoods.setCount(count);
        orderGoods.setPrice(skuGoods.getMarketPrice());
        orderGoods.setDisPrice(skuGoods.getPromotePrice());
        orderGoods.setSkuInfo(skuInfo);
        orderGoods.setGoodsSn(skuGoods.getGoodsNumber());
        double totalPrice = (skuGoods.getPromotePrice() * count);


        Order order = new Order();
        order.setSerialNumber(createOrderSn());//订单编号
        order.setMemberId(member.getId());//用户ID
        order.setPrice(totalPrice);//总价
        VipGrade vipGrade = vipDao.findById(member.getVipGradeId());
        if (null != vipGrade) {
            order.setVipDiscounts(vipGrade.getDiscount());//会员折扣
        }
        order.setCount(1);
        order.setType(2);//2、自主下单
        order.setFrom(from);
        order.setStatus(1);//1、已下单待付款
        order.setTimeCreate(new Date());//下单时间
        order.save();
        orderGoods.setOrderId(order.getId());
        orderGoods.save();
        List<OrderGoods> list = orderGoodsDao.getList3(order.getId());
        order.put("goods", list);
        order.put("memberRebates", getMemberRebates(member.getId()));
        map.put("res", ResCode.SUCCESS);
        map.put("order", order);
        return map;
    }



    public double getMemberRebates(int memberId) {
        String sql = "SELECT t_member.`rebate_count` FROM t_member WHERE id=" + memberId;
        Member member = memberDao.findFirst(sql);
        return member.getRebateCount();
    }

    public Map<String, Object> orderPay(Order order) {
        Map<String, Object> map = new HashMap<String, Object>();
        Order oldOrder = orderDao.findById(order.getId());
        Member member = memberDao.findById(order.getMemberId());
        if (null == member) {//用户不存在
            map.put("res", ResCode.MEMBER_NO);
            return map;
        }
        if (member.getStatus() == 1) {//用户被拉黑
            map.put("res", ResCode.STATUS_ERROR);
            return map;
        }
        VipGrade vipGrade = vipDao.findById(member.getVipGradeId());
        double total = oldOrder.getPrice();//原始价
        double payPrice = oldOrder.getPrice() * vipGrade.getDiscount();//会员价
        boolean isPinkage = false;
        if (null != order.getMemberCouponId()) {//使用优惠券
            MemberCoupon memberCoupon = memberCouponDao.getById(order.getMemberCouponId());
            if (null == memberCoupon) {
                map.put("res", ResCode.COUPON_NULL2);
                return map;
            }
            String typeInfo = memberCoupon.getStr("type_info");//
            if (memberCoupon.getInt("type") == 0) {//优惠券类型，0折扣券，1抵用券，2满减券，3免运费
                //优惠券类型扩展字段,type=0(表示折扣数)1(表示抵用金额)2(表示满？减？，格式100-10)3(为空)
                double couponDiscount = Double.parseDouble(typeInfo);
                payPrice = payPrice * (couponDiscount);//乘上折扣数
            } else if (memberCoupon.getInt("type") == 1) {
                payPrice = payPrice - Double.parseDouble(typeInfo);//减去抵用金额
            } else if (memberCoupon.getInt("type") == 2) {//满减
                String full = typeInfo.split("-")[0];
                String sub = typeInfo.split("-")[1];
                double fullPrice = Double.parseDouble(full);
                double subPrice = Double.parseDouble(sub);
                if (fullPrice <= total) {//满足满减条件
                    payPrice = payPrice - subPrice;
                } else {
                    map.put("res", ResCode.COUPON_CANNOT);
                    return map;
                }
            } else if (memberCoupon.getInt("type") == 3) {
                isPinkage = true;//包邮费
            }
            oldOrder.setMemberCouponId(memberCoupon.getInt("member_coupon_id"));//订单的用户优惠券ID
        } else {
            if (null != order.getRebate()) {//使用返点
                if (order.getRebate() > member.getRebateCount()) {//返点数大于用户剩余返点数
                    map.put("res", ResCode.REBATE_INSUFFICIENT);
                    return map;
                } else {
                    payPrice = payPrice - order.getRebate();//进去使用的返点金额
                    oldOrder.setRebate(order.getRebate());
                }
            }
        }
        if (isPinkage) {//包邮
            oldOrder.setPostagePrice(0.00);
        } else {//邮费
            oldOrder.setPostagePrice(order.getPostagePrice());
        }
        //地址
        oldOrder.setContact(order.getContact());
        oldOrder.setPhone(order.getPhone());
        oldOrder.setAddress(order.getAddress());
        oldOrder.setProvince(order.getProvince());
        oldOrder.setCity(order.getCity());
        oldOrder.setArea(order.getArea());
        oldOrder.setExpressage(order.getExpressage());
        if (!TextUtils.isEmpty(order.getDeliverReamrk())){
            oldOrder.setDeliverReamrk(order.getDeliverReamrk());
        }

        //实付金额
        oldOrder.setRealPrice(payPrice);
        oldOrder.update();


        //payPrice 测试金额1分钱
        payPrice = 0.01;
        if (order.getPayType() == 1) {//1支付宝
            //oldOrder.setPayType(1);
//            public static String orderInfo(String notifyUrl, double totalAmount, String outTradeNo, String subject) {
            String payInfo = AliPay.orderInfo(PayConstant.ALI_NOTIFY_URL,payPrice,oldOrder.getSerialNumber(), "母婴商品");
//            String payInfo = AliPay.payInfo(oldOrder.getSerialNumber(), "母婴商品", "母婴商品", payPrice , PayConstant.ALI_NOTIFY_URL);
            map.put("res", ResCode.SUCCESS);
            map.put("payInfo", payInfo);
            return map;
        } else {//微信
            //oldOrder.setPayType(2);
            int wxPayPrice = (int) (payPrice * 100);
            try {
                Map<String, String> resMap = WxPay.payInfo(oldOrder.getSerialNumber(), "母婴商品", wxPayPrice + "", PayConstant.WX_NOTIFY_URL);
                map.put("res", ResCode.SUCCESS);
                map.put("payInfo", resMap);
                return map;
            } catch (Exception e) {
                map.put("res", ResCode.ERROR);
                return map;
            }
        }
    }

    public Page<Order> getOrderPage(int pageNo, int pageSize, Integer status, int memberId) {
        Page<Order> page = orderDao.getPage(pageNo, pageSize, memberId, status);
        List<Order> orderList = page.getList();
        Member member = memberDao.findById(memberId);
        for (int i = 0; i < orderList.size(); i++) {
//            List<OrderGoods> orderGoodsList = orderGoodsDao.getList2(orderList.get(i).getId());
//            orderList.get(i).put("goodsList", orderGoodsList);
            List<OrderGoods> list = orderGoodsDao.getList3(orderList.get(i).getId());
            orderList.get(i).put("goods", list);
            orderList.get(i).put("memberRebates", getMemberRebates(member.getId()));
        }
        Page<Order> pageNew = new Page<Order>(orderList, page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return pageNew;
    }

    public Order getOrderInfo(int memberId,int orderId){
        Order order=orderDao.getOrderInfo(orderId,memberId);
        if (null==order){
            return null;
        }
        List<OrderGoods> list = orderGoodsDao.getList3(order.getId());
        order.put("goods",list);
        order.put("memberRebates", getMemberRebates(memberId));
        return order;
    }

    public List<PostageTpl> getExpressage(String province, String postages) {
        String[] pasIds = postages.split(",");
        List<PostageTpl> postageTplList = PostageTpl.dao.getMax(pasIds, province);
        List<PostageTpl> list = new ArrayList<PostageTpl>();
        for (int i = 0; i < postageTplList.size(); i++) {
            boolean isAdd = false;
            for (Iterator<PostageTpl> iterator = list.iterator(); iterator.hasNext(); ) {
                PostageTpl postageTpl = iterator.next();
                if (postageTpl.get("value").equals(postageTplList.get(i).get("value"))) {
                    if (postageTpl.getDouble("postage") >= postageTplList.get(i).getDouble("postage")) {
                        isAdd = true;
                    } else {
                        iterator.remove();
                        list.add(postageTplList.get(i));
                        isAdd = true;
                    }
                }
            }
            if (!isAdd) {
                list.add(postageTplList.get(i));
            }
        }
        return list;
    }

    public ResCode updateOrderStatus(int status, int orderId) {
        Order order = orderDao.findById(orderId);
        //1、已下单待付款，2、已付款待发货，3、已发货待收货、4、交易成功，5、退款中，6、退货中、7、订单关闭，）
        if (order.getStatus() == 1) {//待付款订单只允许取消
            if (status != 7) {
                return ResCode.PARAM_FORMAT;
            }
            order.setStatus(7);
            order.update();
            return ResCode.SUCCESS;
        }
        if (order.getStatus() == 3) {//已发货，只能确认收货
            if (status != 4) {
                return ResCode.PARAM_FORMAT;
            }
            order.setStatus(4);
            order.setTimeReceiving(new Date());
            order.update();
            return ResCode.SUCCESS;
        }
        return ResCode.PARAM_FORMAT;
    }


    public Page<OrderGoods> getBoughtGoods(int pageNo,int pageSize,int memberId){
        return orderGoodsDao.getBoughtGoods(pageNo,pageSize,memberId);
    }

    public ResCode salesReturn(int memberId,int orderId,String value,String returnSn){
        Order order=orderDao.getOrderInfo(orderId,memberId);
        if (null==order){//找不到该用户的订单
            return ResCode.PARAM_ERROR;
        }
        if (order.getStatus()!=6){//订单状态不为退货中
            return ResCode.PARAM_ERROR;
        }
        if (order.getStatusReturns()!=2){//卖家还没同意退货
            return ResCode.PARAM_ERROR;
        }
        order.setStatusReturns(3);//设置退货状态买家已发货
        order.setTimeReturnsDeliver(new Date());//买家发货时间
        order.setReturnsExpressage(value);
        order.setReturnsLogisticsNumber(returnSn);
        order.update();
        return ResCode.SUCCESS;
    }

    public ResCode isPay(int orderId){
        Order order=orderDao.findById(orderId);
        if (order.getStatus()==2){
            return ResCode.PAY_SUCCESS;
        }else {
            return ResCode.PAY_NO;
        }
    }
}
