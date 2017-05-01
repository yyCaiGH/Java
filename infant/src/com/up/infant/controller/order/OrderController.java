package com.up.infant.controller.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.Cart;
import com.up.infant.model.Order;
import com.up.infant.model.Region;
import com.up.infant.model.params.OrderSubmit;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/10 0010. 22:42
 */
public class OrderController extends BaseController {
    OrderLogic logic = new OrderLogic();

    /**
     * 获取全部地址
     */
    public void getRegion() {
        Region region = new Region();
        List<Region> regions = region.find("SELECT * FROM t_region WHERE TYPE=1");
        for (int i = 0; i < regions.size(); i++) {
            List<Region> cityList = region.find("SELECT * FROM t_region WHERE TYPE=2 AND parentCode=" + regions.get(i).getCode());
            for (int j = 0; j < cityList.size(); j++) {
                List<Region> areaList = region.find("SELECT * FROM t_region WHERE TYPE=3 AND parentCode=" + cityList.get(j).getCode());
                cityList.get(j).put("city", areaList);
            }
            regions.get(i).put("province", cityList);
        }
        renderJson(regions);
    }

    /**
     * 加入购物车
     */
    public void addToCart() {
        Cart cart = getModel(Cart.class, "");
        res(logic.addToCart(cart));
    }

    /**
     * 获取购物车
     */
    public void getCart() {
        int memberId = getParaToInt("memberId");
        resSuccess(logic.getCart(memberId));
    }

    public void delCart() {
        Cart cart = getModel(Cart.class, "");
        res(logic.delCart(cart.getMemberId(), cart.getId()));
    }

    public void updateCart() {
        Cart cart = getModel(Cart.class, "");
        res(logic.updateCart(cart));
    }

    /**
     * 订单提交
     */
    public void submit() {
        String submit = getPara("submit");
        Map<String, Object> map = logic.submitOrder(submit);
        ResCode resCode = (ResCode) map.get("res");
        if (resCode.getCode() < 0) {
            res(resCode);
        } else {
            Order order = (Order) map.get("order");
            resSuccess(order);
        }
    }

    public void buyNow() {
        int memberId = getParaToInt("memberId");
        int goodsSkuId = getParaToInt("goodsSkuId");
        int count = getParaToInt("count");
        int from = getParaToInt("from");
        Map<String, Object> map = logic.buyNow(memberId, goodsSkuId, count, from);
        ResCode resCode = (ResCode) map.get("res");
        if (resCode.getCode() < 0) {
            res(resCode);
        } else {
            Order order = (Order) map.get("order");
            resSuccess(order);
        }

    }

    /**
     * 支付调起
     */
    public void orderPay() {
        Order order = getModel(Order.class, "");
        if (null == order.getExpressage() || null == order.getContact() || null == order.getPhone()
                || null == order.getAddress()
                || null == order.getProvince()
                || null == order.getCity()
                || null == order.getArea()) {
            res(ResCode.PARAM_FORMAT);
            return;
        }
        Map<String, Object> map = logic.orderPay(order);
        ResCode resCode = (ResCode) map.get("res");
        if (resCode.getCode() < 0) {
            res(resCode);
        } else {
            resSuccess(resCode.getMsg(), map.get("payInfo"));
        }
    }

    public void getOrderList() {
        Integer status = getParaToInt("status");
        int memberId = getParaToInt("memberId");
        int pageNo = getParaToInt("pageNo");
        int pageSize = getParaToInt("pageSize");
        resSuccess(logic.getOrderPage(pageNo, pageSize, status, memberId));
    }


    public void orderInfo() {
        int memberId = getParaToInt("memberId");
        int orderId = getParaToInt("orderId");
        resSuccess(logic.getOrderInfo(memberId, orderId));
    }

    /**
     * 获取运费
     */
    public void getExpressage() {
        String province = getPara("province");
        String postages = getPara("postages");
        resSuccess(logic.getExpressage(province, postages));
    }

    /**
     * 更新状态
     */
    public void update() {
        int status = getParaToInt("status");
        int orderId = getParaToInt("orderId");
        res(logic.updateOrderStatus(status, orderId));
    }

    public void getBoughtGoods() {
        int pageNo = getParaToInt("pageNo");
        int pageSize = getParaToInt("pageSize");
        int memberId = getParaToInt("memberId");
        resSuccess(logic.getBoughtGoods(pageNo, pageSize, memberId));
    }

    /**
     * 退货
     */
    public void salesReturn() {
        int orderId = getParaToInt("orderId");
        int memberId = getParaToInt("memberId");
        String value = getPara("value");
        String returnSn = getPara("returnSn");
        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(returnSn)) {
            res(ResCode.PARAM_ERROR);
            return;
        }
        res(logic.salesReturn(memberId, orderId, value, returnSn));
    }

    public void orderIsPay(){
        int orderId=getParaToInt("orderId");
        res(logic.isPay(orderId));
    }
}
