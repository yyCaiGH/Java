package com.up.infant.model.params;

import com.google.gson.Gson;
import com.up.common.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 13:41
 */
public class OrderSubmit extends BaseBean{
    private int memberId;
    private int from;
    private List<CartItem> cart;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public  class CartItem extends BaseBean{
        private int cartId;
        private int count;
//        private int skuId;
//        private double price;

//        public double getPrice() {
//            return price;
//        }
//
//        public void setPrice(double price) {
//            this.price = price;
//        }

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

//        public int getSkuId() {
//            return skuId;
//        }
//
//        public void setSkuId(int skuId) {
//            this.skuId = skuId;
//        }
    }

    public static void main(String[] strings){
//        OrderSubmit orderSubmit=new OrderSubmit();
//        orderSubmit.setFrom(1);
//        orderSubmit.setMemberId(1);
//        CartItem cartItem=new CartItem();
//        cartItem.setCartId(2);
//        cartItem.setCount(5);
//        List<CartItem> list=new ArrayList<CartItem>();
//        list.add(cartItem);
//        orderSubmit.setCart(list);
//        String s=new Gson().toJson(orderSubmit);
//        System.out.println(s);


    }
}
