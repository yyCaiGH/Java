package com.up.infant.model.params;

import com.up.common.base.PageParams;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/5 0005. 18:40
 */
public class Search extends PageParams{

    private String search;
    private Integer brandId;
    private Integer sex;
    private Integer age;
    private Integer minPrice;
    private Integer maxPrice;
    private String color;
    private String season;
    private Integer catId2;

    private Integer rankType;//0人气,1新品,2折扣,3价格


    public Integer getCatId2() {
        return catId2;
    }

    public void setCatId2(Integer catId2) {
        this.catId2 = catId2;
    }

    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
