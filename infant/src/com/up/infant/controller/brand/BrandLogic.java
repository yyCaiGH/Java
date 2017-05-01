package com.up.infant.controller.brand;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.Brand;
import com.up.infant.model.Goods;
import com.up.infant.model.SkuGoods;

import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/3 0003. 21:38
 */
public class BrandLogic {

    Brand brandDao=new Brand();

    public ResCode add(Brand brand){
        if (TextUtils.isEmpty(brand.getNameEn())&&TextUtils.isEmpty(brand.getNameZh())){
            return ResCode.INFO_FULL;
        }
        try {
            brand.setCreateTime(new Date());
            brand.save();
            return ResCode.SAVE_SUCCESS;
        }catch (Exception e){
            return ResCode.PARAM_ERROR;
        }
    }

    public ResCode update(Brand brand){
        if (TextUtils.isEmpty(brand.getNameEn())&&TextUtils.isEmpty(brand.getNameZh())){
            return ResCode.INFO_FULL;
        }
        try {
            brand.update();
            if (brand.getStatus()==2){//品牌下架，则所属商品全部下架,并且商品所属的sku商品也要全部下架
                Goods goods = new Goods();
                goods.setBrandId(brand.getId());
                List<Goods> goodsList = Goods.dao.getAllList(goods);
                for (int i = 0 ; i<goodsList.size();i++){
                    goodsList.get(i).setPutaway(2);
                    List<SkuGoods> skuGoodsList = SkuGoods.dao.getList(goodsList.get(i).getId());
                    for (int j = 0;j<skuGoodsList.size();j++){
                        skuGoodsList.get(j).setPutaway(2);
                    }
                    SkuGoods.dao.updateBatch(skuGoodsList);
                }
                Goods.dao.updateBatch(goodsList);
            }
            return ResCode.UPDATE_SUCCESS;
        }catch (Exception e){
            return ResCode.PARAM_ERROR;
        }
    }

    public List<Brand> getBrandList(String search){
        return brandDao.brandList(search);
    }




}
