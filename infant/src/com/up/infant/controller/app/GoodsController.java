package com.up.infant.controller.app;

import com.up.common.base.ComParams;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.Brand;
import com.up.infant.model.Category;
import com.up.infant.model.Collect;
import com.up.infant.model.Goods;
import com.up.infant.model.params.Search;

import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/5 0005. 17:11
 */
public class GoodsController extends BaseController{
    GoodsLogic logic=new GoodsLogic();

    public void getClassify(){
        List<Category> list=logic.getCategoryList();
        resSuccess(list);
    }
    public void getBrands(){
        List<Brand> brands=logic.getBrandList();
        resSuccess(brands);
    }

    public void getSearchGoods(){
        Search search=getBean(Search.class,"");
        resSuccess(logic.getGoodsList(search));
    }

    public void getColorList(){
        resSuccess(logic.getColor());
    }

    public void getGoodsInfo(){
        Integer goodsId=getParaToInt("goodsId");
        Integer memberId=getParaToInt("memberId");
        Goods goods=logic.getGoodsInfo(memberId,goodsId);
        resSuccess(goods);
    }

    public void collect(){
        Collect collect=getModel(Collect.class,"");
        ResCode resCode=logic.collect(collect);
        res(resCode);
    }

    /**
     * 单品推荐
     */
    public void recommendGoods(){
        resSuccess(logic.getRecommend());
    }

    /**
     * 品牌推荐
     */
    public void recommendBrand(){
        resSuccess(logic.getRecommendBrand());
    }

    /**
     * 推荐品牌商品列表
     */
    public void recommendBrandAndGoods(){
        int pageSize=getParaToInt("pageSize");
        int pageNo=getParaToInt("pageNo");
        Integer goodsSize=getParaToInt("goodsSize");
        resSuccess(logic.recommendBrandAndGoods(pageNo,pageSize,goodsSize));
    }

    /**
     * 添加浏览记录
     */
    public void goodsBrowse(){
        int goodsId=getParaToInt("goodsId");
        res(logic.browse(goodsId));
    }
}
