package com.up.infant.controller.brand;

import com.jfinal.upload.UploadFile;
import com.up.common.base.ImageResult;
import com.up.common.controller.BaseController;
import com.up.common.def.FileType;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.Country;
import com.up.infant.model.Sku;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/3 0003. 21:44
 */
public class CountryController extends BaseController{

    private static String dbUrl = "upload/country/";
    /**
     * 新增国家
     */
    public void add(){
        /*UploadFile  file = getFile("imgFile");
        Country country=getModel(Country.class,"");
        if (TextUtils.isEmpty(country.getName())){
            res(ResCode.INFO_FULL);
            return;
        }
        if (file!=null&&!TextUtils.isEmpty(file.getFileName())) {
            String temp[] = file.getFileName().split("\\.");
            long now = new Date().getTime();
            String suffix = now + "." + temp[temp.length - 1];
            boolean isSuccess = file.getFile().renameTo(new File(rootPath + "/" + dbUrl + suffix));
            if (isSuccess) {
                country.setImgUrl(dbUrl + suffix);
            } else {
                res(ResCode.IMG_ERROR);
                return;
            }
        }*/
        try {
            Country country = getModel(Country.class,"");
            country.save();
            resSuccess(ResCode.SUCCESS.getMsg(),country);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 更新国家信息
     */
    public void update(){
       /* UploadFile  file = getFile("imgFile");
        Country country=getModel(Country.class,"");
        if (country.getId()==null){
            res(ResCode.INFO_FULL);
            return;
        }
        if (file!=null&&!TextUtils.isEmpty(file.getFileName())) {
            String temp[] = file.getFileName().split("\\.");
            long now = new Date().getTime();
            //String imgName = country.getId() + "_country_img";
            String suffix = now + "." + temp[temp.length - 1];
            boolean isSuccess = file.getFile().renameTo(new File(rootPath + "/" + dbUrl + suffix));
            if (isSuccess) {
                country.setImgUrl(dbUrl + suffix);
            } else {
                res(ResCode.IMG_ERROR);
                return;
            }
        }*/
        try {
            Country country = getModel(Country.class,"");
            country.update();
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 获取国家列表
     */
    public void getList(){
        List<Country> countries=Country.dao.getList();
        resSuccess(countries);
    }

    //删除国家
    public void delete(){
        Country country=getModel(Country.class,"");
        if(country.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            //删除该属性名
            country = Country.dao.findById(country.getId());
            country.setStatus(1);
            country.update();
            res(ResCode.DEL_SUCCESS);
        }

    }

    public void getById(){
        Country country=getModel(Country.class,"");
        if(country.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            country = Country.dao.findById(country.getId());
            resSuccess(ResCode.SUCCESS.getMsg(),country);
        }
    }
}
