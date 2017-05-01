package com.up.infant.controller.goods;

import com.jfinal.upload.UploadFile;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.Country;
import com.up.infant.model.Goods;
import com.up.infant.model.GoodsTag;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by yy_cai
 * on 2016/11/17 0003. 21:44
 */
public class GoodsTagController extends BaseController{

    /**
     * 新增商品tag
     */
    public void add(){
        GoodsTag goodsTag=getModel(GoodsTag.class,"");
        if (TextUtils.isEmpty(goodsTag.getName())||TextUtils.isEmpty(goodsTag.getImgUrl())){
            res(ResCode.INFO_FULL);
            return;
        }
        try {
            goodsTag.save();
            resSuccess(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 更新商品tag
     */
    public void update(){
        GoodsTag goodsTag=getModel(GoodsTag.class,"");
        if (goodsTag.getId()==null||TextUtils.isEmpty(goodsTag.getName())||TextUtils.isEmpty(goodsTag.getImgUrl())){
            res(ResCode.INFO_FULL);
            return;
        }
        try {
            goodsTag.update();
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 获取商品tag列表
     */
    public void getList(){
        List<GoodsTag> countries=GoodsTag.dao.getList();
        resSuccess(countries);
    }

    //删除商品tag
    public void delete(){
        GoodsTag goodsTag=getModel(GoodsTag.class,"");
        if(goodsTag.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            //删除该属性名
            long count = Goods.dao.getTagCount(goodsTag.getId());
            if (count>0){
                resFailure(ResCode.DELETE_ERROR.getCode(),"还有"+count+"个商品拥有该tag属性，不能删除");
            }
            else{
                GoodsTag.dao.deleteById(goodsTag.getId());
                res(ResCode.DEL_SUCCESS);
            }
        }

    }

    public void getById(){
        GoodsTag goodsTag=getModel(GoodsTag.class,"");
        if(goodsTag.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            goodsTag = GoodsTag.dao.findById(goodsTag.getId());
            resSuccess(ResCode.SUCCESS.getMsg(),goodsTag);
        }
    }
}
