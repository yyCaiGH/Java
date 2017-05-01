package com.up.infant.controller.brand;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.ComParams;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.Brand;
import com.up.infant.model.Country;

import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/3 0003. 21:29
 */
public class BrandController extends BaseController{
    private BrandLogic logic=new BrandLogic();

    /**
     * 添加品牌
     */
    public void add(){
        Brand brand=getModel(Brand.class,"");
        ResCode resCode=logic.add(brand);
        res(resCode);
    }

    /**
     * 更新品牌
     */
    public void update(){
        Brand brand=getModel(Brand.class,"");
        ResCode resCode=logic.update(brand);
        res(resCode);
    }

    public void getPutawayBrands(){
        List<Brand> brandList = Brand.dao.getPutawayBrands();
        resSuccess(brandList);
    }
    /**
     * 获取品牌列表
     */
    public void getList(){
        ComParams page=getBean(ComParams.class,"");
        try{
            Page<Brand> brandPage=new Brand().getList(page.getPageNo(),page.getPageSize(),page.is());
            resSuccess(brandPage);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getById(){
        try{
            String id = getPara("brandId");
            Brand goods =  Brand.dao.getById(Long.parseLong(id));
            resSuccess(goods);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void search(){
        String search=getPara("search");
        resSuccess(logic.getBrandList(search));
    }

    public void info(){
        int brandId=getParaToInt("brandId");
        Brand brand=new Brand().findById(brandId);
        if (null!=brand){
//            country_name,country_id
            Country country=new Country().findById(brand.getCountryId());
            brand.put("country_name",country.getName());
            brand.put("country_id",country.getId());
            brand.put("country_url",country.getImgUrl());
        }
        resSuccess(brand);
    }

}
