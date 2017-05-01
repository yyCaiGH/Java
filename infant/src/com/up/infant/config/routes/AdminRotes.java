package com.up.infant.config.routes;

import com.jfinal.config.Routes;
import com.up.common.controller.FileController;
import com.up.common.controller.SmsController;
import com.up.infant.controller.AdminController;
import com.up.infant.controller.goods.GoodsTagController;
import com.up.infant.controller.goods.PostageGroupController;
import com.up.infant.controller.goods.PostageTplController;
import com.up.infant.controller.goods.SkuController;
import com.up.infant.controller.order.OrderController;
import com.up.infant.controller.repertory.RepertoryController;
import com.up.infant.controller.yorder.YorderController;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class AdminRotes extends Routes{
    @Override
    public void config() {
        add("/admin", AdminController.class);
        add("/sku", SkuController.class);
        add("/postage",PostageGroupController.class);
        add("/postageTpl",PostageTplController.class);
        add("/goodsTag",GoodsTagController.class);
        add("/repertory", RepertoryController.class);
        add("/yorder", YorderController.class);
        add("/sms", SmsController.class);

    }
}
