package com.up.infant.config.routes;

import com.jfinal.config.Routes;
import com.up.common.controller.FileController;
import com.up.infant.controller.*;
import com.up.infant.controller.app.AppController;
import com.up.infant.controller.article.ArticleController;
import com.up.infant.controller.banner.BannerController;
import com.up.infant.controller.brand.BrandController;
import com.up.infant.controller.brand.CountryController;
import com.up.infant.controller.coupon.CouponController;
import com.up.infant.controller.custompage.CustomPageController;
import com.up.infant.controller.goods.CategoryController;
import com.up.infant.controller.goods.GoodsController;
import com.up.infant.controller.member.MemberController;
import com.up.infant.controller.member.VipController;
import com.up.infant.controller.message.MessageController;
import com.up.infant.controller.order.OrderController;
import com.up.infant.controller.pay.PayController;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/19 0019. 14:17
 */
public class JHRotes extends Routes{
    public void config() {
        add("/file", FileController.class);
        add("/banner", BannerController.class);
        add("/page", CustomPageController.class);
        add("/config", ConfigController.class);
        add("/message", MessageController.class);
        add("/member", MemberController.class);
        add("/coupon", CouponController.class);
        add("/brand", BrandController.class);
        add("/country", CountryController.class);
        add("/goods", GoodsController.class);
        add("/category", CategoryController.class);
        add("/article", ArticleController.class);
        add("/vip", VipController.class);
        add("/app/goods", com.up.infant.controller.app.GoodsController.class);
        add("/app", AppController.class);
        add("/order", OrderController.class);
        add("/pay", PayController.class);
    }
}
