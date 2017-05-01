package com.up.common.def;

import com.up.infant.model.Coupon;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/22 0022. 10:37
 */
public enum ResCode {
    SUCCESS(0,"操作成功！"),//默认
    ERROR(-1,"操作异常！"),//默认
    INFO_FULL(-1,"请提交完整信息！"),PARAM_FORMAT(-2,"参数格式异常!"),PARAM_ERROR(-3,"参数异常!"),
    PAGE_PARAM_ERROR(-5,"分页参数异常"),IMG_ERROR(-4,"图片上传失败"),DELETE_ERROR(-6,"删除失败"),
    ERROR_IMAGE(-2,"图片上传失败！"),//默认
    SAVE_SUCCESS(0,"添加成功"),
    UPDATE_SUCCESS(0,"更新成功"),
    DEL_SUCCESS(0,"删除成功"),

    REGISTER_SUCCESS(0,"注册成功!"),//注册
    PHONE_FORMAT(-1,"手机号格式错误!"),MEMBER_HAS(-2,"该手机号已注册！"),PWD_FORMAT(-3,"密码格式错误!"),

    LOGIN_SUCCESS(0,"登陆成功!"),//登陆
    PP_ERROR(-1,"账号或密码错误!"),STATUS_ERROR(-2,"账号已被拉黑!"),MEMBER_NO(-3,"用户不存在！"),
    COUPON_SUCCESS(0,"优惠券添加成功"),//优惠券
    COUPON_EXCHANGE_SUCCESS(0,"兑换成功！"),//优惠券
    COUPON_GET_SUCCESS(0,"领取成功！"),//优惠券
    COUPON_TIME_ERROR(-1,"选择时间有误！"),COUPON_PAST_DUE(-2,"优惠券已过期！"),COUPON_FULL(-3,"已经被兑换光了！"),COUPON_NULL(-4,"找不到该优惠券！"),COUPON_HAS(-5,"已经兑换过！"),COUPON_HAS2(-5,"已经领取过！"),COUPON_NULL2(-6,"优惠券过期或已被使用！"),
    COUPON_CANNOT(-7,"优惠全不满足使用条件！"),
    SORE_HAS(-1,"该排序已存在！"),//轮播
    CANCEL_LIKE(0,"取消赞！"),PUBLISH_TEXT_SUCCESS(0,"发布成功！"),PUBLISH_SUCCESS(0,"发布成功！"),TAG_SUCCESS(0,"标签添加成功！"),PIC_SUCCESS(0,"图片添加成功！"),
    ARTICLE_NO(-1,"文章不存在"),COMMENT_NULL(-2,"评论内容为空！"),
    FOLLOW_SUCCESS(0,"关注成功！"),FOLLOW_CANCEL(0,"取消关注！"),
        GOODS_COLLECT(0,"收藏成功！"),GOODS_COLLECT_CANCEL(0,"取消收藏！"),GOODS_BROWSE(0,"浏览商品！"),
    GOODS_NULL(-1,"商品不存在！"),
    PAGE_NULL(-1,"单页不存在或被隐藏！"),
    CART_ADD(0,"成功加入购物车！"),
    CART_SKU_NULL(-1,"对应属性sku不存在！"),CART_SKU_SHELVES(-2,"对应商品已下架！"),CART_SKU_REPERTORY(-3,"库存不足！"),CART_NULL(-1,"商品不存在购物车中！"),
    REBATE_INSUFFICIENT(-1,"返点余额不足！"),
    PAY_SUCCESS(0,"支付成功！"),PAY_NO(-1,"还未支付！")

    ;


    private int code;
    private String msg;

    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
