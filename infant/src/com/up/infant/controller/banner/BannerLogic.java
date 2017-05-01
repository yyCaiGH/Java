package com.up.infant.controller.banner;

import com.up.common.def.ResCode;
import com.up.infant.model.Banner;

import java.util.Date;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/2 0002. 22:29
 */
public class BannerLogic {

    public ResCode banner(Banner banner, boolean isAdd) {
//        if (banner.hasSort(banner.getSort())) {
//            return ResCode.SORE_HAS;
//        }
        if (banner.getType() > 4 || banner.getType() < 0) {
            return ResCode.PARAM_FORMAT;
        }
        if (banner.getIsShow() != 0 && banner.getIsShow() != 1) {
            return ResCode.PARAM_ERROR;
        }
        if (isAdd) {
            banner.setCreateTime(new Date());
            banner.save();
        } else {
            banner.update();
        }
        return ResCode.SUCCESS;
    }
}
