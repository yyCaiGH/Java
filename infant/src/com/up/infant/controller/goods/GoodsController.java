package com.up.infant.controller.goods;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.Logger;
import com.up.infant.model.Collect;
import com.up.infant.model.Goods;
import com.up.infant.model.Sku;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * Created by yyCai
 * on 2016/11/4 0004. 21:28
 */
public class GoodsController extends BaseController{

    private GoodsLogic logic=new GoodsLogic();

    public void add(){
        String goodsSkuListJson = getPara("goodsSkuList");
        String goodsJson = getPara("goods");
        String goodsSellPointJson = getPara("goodsSellPoint");
        int res = logic.addGoods(goodsSkuListJson,goodsJson,goodsSellPointJson);
        if (res==-1){
            resFailure(ResCode.ERROR.getCode(),"主商品添加失败！");
        }
        else if (res==-2){
            resFailure(ResCode.ERROR.getCode(),"批量添加商品的sku失败！");
        }
        else if (res==-3){
            resFailure(ResCode.ERROR.getCode(),"批量添加商品的卖点失败！");
        }
        else {
            res(ResCode.SAVE_SUCCESS);
        }
    }

    public void updateGoods(){
        String goodsSkuListJson = getPara("goodsSkuList");
        String goodsJson = getPara("goods");
        String goodsSellPointJson = getPara("goodsSellPoint");
        int res = logic.updateGoods(goodsSkuListJson,goodsJson,goodsSellPointJson);
        if (res==-1){
            resFailure(ResCode.ERROR.getCode(),"主商品更新失败！");
        }
        else if (res==-2){
            resFailure(ResCode.ERROR.getCode(),"批量更新商品的sku失败！");
        }
        else if (res==-3){
            resFailure(ResCode.ERROR.getCode(),"批量更新商品的卖点失败！");
        }
        else {
            res(ResCode.SAVE_SUCCESS);
        }
    }

    public void update(){
        try{
            Goods goods=getModel(Goods.class,"");
            goods.update();
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }
    }

    public void noRecommend(){
        try{
            Goods goods=getModel(Goods.class,"");
            goods = Goods.dao.getById(goods.getId());
            goods.setRecommend(false);
            goods.update();
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 批量推荐商品
     */
    public void recommend(){
        try{
            String selectGoodsIds = getPara("selectGoodsIds");
            Object obj = JSONValue.parse(selectGoodsIds);
            JSONArray array = (JSONArray) obj;
            List<Goods> goodsList = new ArrayList<Goods>();
            for (int i = 0; i < array.size(); i++) {
                String id = array.get(i).toString();
                int goodsId = Integer.parseInt(id);
                Goods goods = Goods.dao.findById(goodsId);
                goods.setRecommend(true);
                goodsList.add(goods);
            }
            Goods.dao.updateBatch(goodsList);
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 初始化添加商品的属性
     * 1、一级分类
     * 2、品牌
     * 3、年龄段
     * 4、性别
     * 5、季节
     * 6、产地-国家
     * 7、商品tag
     * 8、SKU设置
     * 9、运费组
     */
    public void getGoodsAttr(){
        Map<String,Object> data =  logic.getGoodsAttr();
        resSuccess(data);
    }
    /**
     * 初始化添加商品的属性
     * 1、一级分类
     * 2、品牌
     */
    public void getGoodsSearchInfo(){
        Map<String,Object> data =  logic.getGoodsSearchInfo();
        resSuccess(data);
    }

    public void getList(){
        try{
            int pageNo = Integer.parseInt(getPara("pageNo"));
            int pageSize = Integer.parseInt(getPara("pageSize"));
            Goods goods=getModel(Goods.class,"",true);
            Page<Goods> goodsPage = Goods.dao.getList(goods,pageNo,pageSize);
            resSuccess(goodsPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getById(){
        try{
            String id = getPara("goodsId");
            Goods goods =  Goods.dao.getById(Long.parseLong(id));
            resSuccess(goods);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 编辑商品需要的属性和商品的信息
     */
    public void getEditGoodsAttr(){
        try{
            String goodsId = getPara("goodsId");
            Map<String,Object> data =  logic.getEditGoodsAttr(Integer.parseInt(goodsId));
            resSuccess(data);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }


}
