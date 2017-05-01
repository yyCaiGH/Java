package com.up.infant.controller.banner;

import com.jfinal.plugin.activerecord.Page;
import com.up.common.base.PageParams;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.config.MIConfig;
import com.up.infant.model.Banner;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * TODO:轮播
 * Created by 王剑洪
 * on 2016/10/19 0019. 12:59
 */
public class BannerController extends BaseController {

    private BannerLogic logic = new BannerLogic();

    /**
     * 前端banner列表获取
     */
    public void getBanners() {
        Banner banner = new Banner();
        List<Banner> banners = banner.getBanners();
        resSuccess(banners);
    }

    public void getBooks(){
        Banner banner=new Banner();
        resSuccess(banner.getBook());
    }

    public void add() {
        try {
            Banner banner = getModel(Banner.class, "");
            ResCode code = logic.banner(banner,true);
            res(code);
        } catch (Exception e) {
           logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }

    public void update() {
        try {
            Banner banner = getModel(Banner.class, "");
            ResCode code = logic.banner(banner,false);
            res(code);
        } catch (Exception e) {
            logger.error(e);
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }
    }

    public void getById(){
        Banner banner = getModel(Banner.class,"");
        if (banner.getId()==null){
            res(ResCode.PARAM_ERROR);
        }
        else {
            banner = Banner.dao.findById(banner.getId());
            resSuccess(banner);
        }


    }
    /**
     * 后台banner列表获取
     */
    public void getList(){
        PageParams pageParams = getBean(PageParams.class,"");
        if (!pageParams.isPageParamsOK()){
            res(ResCode.PAGE_PARAM_ERROR);
        }
        Page<Banner> banners = Banner.dao.getBanners(pageParams.getPageNo(),pageParams.getPageSize());
        resSuccess(banners);
    }






}
