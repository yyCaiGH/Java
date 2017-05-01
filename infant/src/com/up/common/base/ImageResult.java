package com.up.common.base;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/21 0021. 21:22
 */
public class ImageResult extends BaseBean{
    private String imgUrl;

    public ImageResult(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
