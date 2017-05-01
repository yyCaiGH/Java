package com.up.infant.controller.app;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;
import com.up.infant.model.params.Search;

import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/5 0005. 17:15
 */
public class GoodsLogic {
    Category categoryDao = new Category();
    Brand brandDao = new Brand();
    Goods goodsDao = new Goods();
    GoodsPoints pointsDao = new GoodsPoints();
    Sku skuDao = new Sku();
    SkuGoods skuGoodsDao = new SkuGoods();
    Collect collectDao=new Collect();

    public List<Category> getCategoryList() {
        List<Category> categories = categoryDao.getList(0);
        for (int i = 0; i < categories.size(); i++) {
            List<Category> childList = categoryDao.getList(categories.get(i).getId());
            categories.get(i).put("childCategory", childList);
        }
        return categories;
    }

    public List<Brand> getBrandList() {
        List<Brand> list = brandDao.getBrands();
        return list;
    }

    public Page<Goods> getGoodsList( Search search) {
        if (TextUtils.isEmpty(search.getSearch())){
            return goodsDao.getGoodsList(search.getPageNo(), search.getPageSize(), search);
        }else {
            return goodsDao.getGoodsList1(search.getPageNo(), search.getPageSize(), search);
        }
    }

    public List<Sku> getColor(){
        return skuDao.getColor();
    }

    public Goods getGoodsInfo(Integer memberId,Integer goodsId){
        Goods goods=goodsDao.getInfo(goodsId,memberId);
        goods.setViewCount(goods.getViewCount()+1);
        goods.update();
        List<GoodsPoints> points=pointsDao.points(goodsId);
        List<SkuGoods> skuGoodsList=skuGoodsDao.goodsSku(goodsId);
        Brand brand=brandDao.findById(goods.getBrandId());
        goods.put("points",points);
        goods.put("skuList",skuGoodsList);
        goods.put("brand",brand);
        return goods;
    }

    public ResCode collect(Collect collect){
        Collect oldCollect=collectDao.hasCollect(collect.getMemberId(),collect.getGoodsId());
        if (null!=oldCollect){//已收藏，取消收藏
            oldCollect.delete();
            return ResCode.GOODS_COLLECT_CANCEL;
        }else {//未收藏，收藏
            collect.setCreateTime(new Date());
            collect.save();
            return ResCode.GOODS_COLLECT;
        }
    }

    public List<Goods> getRecommend(){
        return goodsDao.getRecommendGoods();
    }
    public List<Brand> getRecommendBrand(){
        return brandDao.getRecommendGoods();
    }

    public Page<Brand> recommendBrandAndGoods(int pageNo,int pageSize,Integer goodsSize){
        Page<Brand> page=brandDao.getList(pageNo,pageSize);
        List<Brand> brandList=page.getList();
        for (int i=0;i<brandList.size();i++){
            List<Goods> goodsList=goodsDao.getBrandGoods(brandList.get(i).getId(),goodsSize);
            brandList.get(i).put("goodsList",goodsList);
        }
        Page<Brand> newPage=new Page<Brand>(brandList,page.getPageNumber(),page.getPageSize(),page.getTotalPage(),page.getTotalRow());
        return newPage;
    }

    public ResCode browse(int goodsId){
        Goods goods=goodsDao.findById(goodsId);
        if (null==goods){//商品不存在
            return ResCode.GOODS_NULL;
        }
        goods.setViewCount(goods.getViewCount()+1);
        goods.update();
        return ResCode.GOODS_BROWSE;
    }




}
