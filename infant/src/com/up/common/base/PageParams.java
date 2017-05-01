package com.up.common.base;

/**
 * TODO:
 * Created by yyCai
 * on 2016/12/3 0003. 22:44
 */
public class PageParams extends BaseBean{
    private Integer pageSize;
    private Integer pageNo;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isPageParamsOK(){
        if (pageNo==null||pageNo<=0||pageSize==null||pageSize<=0){
            return false;
        }
        return true;
    }
}
