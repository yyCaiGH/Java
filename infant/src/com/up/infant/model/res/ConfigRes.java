package com.up.infant.model.res;

import com.up.common.base.BaseBean;
import com.up.infant.model.Identifying;
import com.up.infant.model.SysConfig;

import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/24 0024. 11:13
 */
public class ConfigRes extends BaseBean{

    public int id;
    private String title;
    private String identifying;
    public List<SysConfig> sysConfigs;

    public ConfigRes(int id, String title, String identifying, List<SysConfig> sysConfigs) {
        this.id = id;
        this.title = title;
        this.identifying = identifying;
        this.sysConfigs = sysConfigs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifying() {
        return identifying;
    }

    public void setIdentifying(String identifying) {
        this.identifying = identifying;
    }

    public List<SysConfig> getSysConfigs() {
        return sysConfigs;
    }

    public void setSysConfigs(List<SysConfig> sysConfigs) {
        this.sysConfigs = sysConfigs;
    }
}
