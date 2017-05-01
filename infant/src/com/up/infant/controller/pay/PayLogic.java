package com.up.infant.controller.pay;

import com.jfinal.plugin.activerecord.Db;
import com.up.infant.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/15 0015. 15:14
 */
public class PayLogic {
    Order orderDao = new Order();
    Member memberDao=new Member();
    VipGrade vipDao=new VipGrade();
    Brand brandDao=new Brand();
    OrderGoods orderGoodsDao=new OrderGoods();
    SkuGoods skuGoodsDao=new SkuGoods();

    public boolean orderPay(String orderSn,int payType,double realPay,String paySn){
        String sql="SELECT * FROM t_order WHERE t_order.`serial_number`="+orderSn;
        Order order=orderDao.findFirst(sql);
        order.setPayType(payType);//付款方式
        order.setTimePay(new Date());//付款时间
        order.setRealPrice(realPay);//实收金额
        order.setStatus(2);
        order.setPaySn(paySn);

        //用户的信息更新
        Member member=memberDao.findById(order.getMemberId());
        VipGrade vipGrade=vipDao.findById(member.getVipGradeId());
        member.setConsumeAmount(member.getConsumeAmount()+realPay);//消费总金额
        member.setRebateCount(member.getRebateCount()+vipGrade.getRebate());//rebate_count剩余返点数
        member.setConsumeCount(member.getConsumeCount()+1);//consume_count消费次数
        VipGrade vipGrade2=vipDao.findFirst("SELECT * FROM t_vip_grade WHERE amount <="+member.getConsumeAmount()+" ORDER BY amount DESC");
        member.setVipGradeId(vipGrade2.getId());//更新用户vip等级

        order.setSuccessRebate(vipGrade.getRebate());
        //更新品牌,
//        List<Brand> brandList=brandDao.getOrderBrand(order.getId());
//        for (int i=0;i<brandList.size();i++){
//            brandList.get(i).setTotalSales(brandList.get(i).getTotalStock()+brandList.get(i).getInt("count"));//销售数量更新
//            brandList.get(i).setTotalSaleroom(brandList.get(i).getTotalSaleroom()+brandList.get(i).getDouble("dis_price")*brandList.get(i).getInt("count"));//品牌商品总销售额更新
//        }
        //更新商品
//        List<OrderGoods> orderGoodsList=orderGoodsDao.getList(order.getId());
//        List<SkuGoods> skuGoodsList=new ArrayList<SkuGoods>();
//        for (int i=0;i<orderGoodsList.size();i++){
//            SkuGoods skuGoods=skuGoodsDao.findById(orderGoodsList.get(i).getSkuGoodsId());
//            skuGoods.setRepertory(skuGoods.getRepertory()-orderGoodsList.get(i).getCount());
//            skuGoodsList.add(skuGoods);
//        }
//        Db.batchUpdate(brandList,brandList.size());//更新品牌的销售数量跟总销售额
//        Db.batchUpdate(skuGoodsList,skuGoodsList.size());//更新sku商品的库存
        boolean res1=member.update();//更新用户
        boolean res2=order.update();//更新订单
        return res1&&res2;



    }
}
