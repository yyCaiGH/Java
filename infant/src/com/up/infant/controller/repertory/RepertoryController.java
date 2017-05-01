package com.up.infant.controller.repertory;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.*;
import com.up.infant.model.params.RepertoryParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yyCai on 2016/12/3 0003.
 */
public class RepertoryController extends BaseController {

    public void getList() {
        try {
            RepertoryParams params = getBean(RepertoryParams.class, "");
            if (!params.isPageParamsOK()) {
                res(ResCode.PAGE_PARAM_ERROR);
                return;
            }
            Page<SkuGoods> skuGoodsPage = SkuGoods.dao.getRepertoryList(params);
            resSuccess(skuGoodsPage);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 更新skuGoods库存，添加采购记录
     */
    public void updateRepertory() {
        String skuRepertoryJson = getPara("sukGoodsJson");
        String remark = getPara("remark");
        int totalCount = 0;//总添加库存
        //新增采购入库记录
        StoragePurchase storagePurchase = new StoragePurchase();
        storagePurchase.setRemark(remark);
        storagePurchase.setCount(totalCount);
        storagePurchase.setTime(new Date());
        storagePurchase.save();

        //更新skuGoods库存,对应的goods总库存，以及对应的品牌总库存，添加skuGoods库存记录
        Object obj = JSONValue.parse(skuRepertoryJson);
        JSONArray array = (JSONArray) obj;
        List<SkuGoods> skuList = new ArrayList<SkuGoods>();
        List<PurchaseDetails> purchaseList = new ArrayList<PurchaseDetails>();//采购记录
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = (JSONObject) array.get(i);
            int skuGoodsId = Integer.parseInt(json.get("id").toString());
            SkuGoods newSku = SkuGoods.dao.findById(skuGoodsId);
            int repertory = Integer.parseInt(json.get("repertory").toString());
            totalCount+=repertory;
            newSku.setRepertory(newSku.getRepertory()+repertory);
            skuList.add(newSku);

            Goods.dao.updateSkuRepertory(true,newSku.getGoodsId(),repertory);//更新goods总库存，和品牌库存

            PurchaseDetails purchaseDetails = new PurchaseDetails();
            purchaseDetails.setCount(repertory);
            purchaseDetails.setSkuGoodsId(skuGoodsId);
            purchaseDetails.setPurchaseId(storagePurchase.getId());
            purchaseList.add(purchaseDetails);
        }
        storagePurchase.setCount(totalCount);
        storagePurchase.update();

        SkuGoods.dao.updateBatch(skuList);//批量更新sku商品的总库存

        PurchaseDetails.dao.addBatch(purchaseList);//添加采购记录
        res(ResCode.UPDATE_SUCCESS);
    }

    //采购列表
    public void getPurchaseLog(){
        try {
            ComParams params = getBean(ComParams.class,"");
            Page<StoragePurchase> page = StoragePurchase.dao.getList(params);
            resSuccess(page);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getPurchaseDetailByPage(){
        try {
            RepertoryParams params = getBean(RepertoryParams.class, "");
            if (!params.isPageParamsOK()) {
                res(ResCode.PAGE_PARAM_ERROR);
                return;
            }
            Page<SkuGoods> skuGoodsPage = SkuGoods.dao.getPurchaseDetailByPage(params);
            resSuccess(skuGoodsPage);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getPurchaseDetail(){
        try {
            String purchaseId = getPara("purchaseId");
            List<SkuGoods> skuGoodsList = SkuGoods.dao.getPurchaseDetail(purchaseId);
            resSuccess(skuGoodsList);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }
}
