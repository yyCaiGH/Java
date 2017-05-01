package com.up.infant.model.params;

import com.up.common.base.BaseBean;
import com.up.common.base.PageParams;

/**
 * Created by yyCai on 2016/12/3 0003.
 * 库存Params
 */
public class RepertoryParams extends PageParams {
    private Integer putaway;
    private Integer brandsId;
    private Integer minRepertory;
    private Integer maxRepertory;
    private String goodsName;
    private String goodsNumber;
    private Integer purchaseId;//采购id

    public Integer getPutaway() {
        return putaway;
    }

    public void setPutaway(Integer putaway) {
        this.putaway = putaway;
    }

    public Integer getBrandsId() {
        return brandsId;
    }

    public void setBrandsId(Integer brandsId) {
        this.brandsId = brandsId;
    }

    public Integer getMinRepertory() {
        return minRepertory;
    }

    public void setMinRepertory(Integer minRepertory) {
        this.minRepertory = minRepertory;
    }

    public Integer getMaxRepertory() {
        return maxRepertory;
    }

    public void setMaxRepertory(Integer maxRepertory) {
        this.maxRepertory = maxRepertory;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

}
