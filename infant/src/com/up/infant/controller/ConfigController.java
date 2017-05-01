package com.up.infant.controller;

import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.controller.BaseController;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;
import com.up.infant.model.res.ConfigRes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/22 0022. 15:35
 */
public class ConfigController extends BaseController {


    /**
     * 获取所有物流
     */
    public void getExpList(){
        List<Exp> expList = Exp.dao.getList();
        resSuccess(expList);
    }
    /**
     * 获取所有配置项
     */
    public void getList() {
        Identifying identifying = new Identifying();
        List<Identifying> list = identifying.getList();
        resSuccess(list);
    }

    /**
     * 获取对应配置项Id下的配置
     */
    public void getTypeList() {
        String[] params = {"id"};
        Verify<Map<String, Integer>> verify = intVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        List<SysConfig> sysConfigs = new SysConfig().getConfigList(verify.getData().get(params[0]));
        resSuccess(sysConfigs);


    }

    /**
     * 获取所有配置
     */
    public void getAll() {
        Identifying identifying = new Identifying();
        List<Identifying> list = identifying.getList();
        List<ConfigRes> resList = new ArrayList<ConfigRes>();
        for (int i = 0; i < list.size(); i++) {
            SysConfig sysConfig = new SysConfig();
            ConfigRes res = new ConfigRes(list.get(i).getId(), list.get(i).getTitle(), list.get(i).getIdentifying(), sysConfig.getConfigList(list.get(i).getId()));
            resList.add(res);
        }
        resSuccess(resList);
    }

    /**
     * 新增配置
     */
    public void addConfig() {
        String value = getPara("value");
        if (TextUtils.isEmpty(value)) {
            resFailure(ResCode.INFO_FULL.getMsg());
            return;
        }
        String[] params = {"id"};
        Verify<Map<String, Integer>> verify = intVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setIId(verify.getData().get(params[0]));
        sysConfig.setValue(value);
        sysConfig.save();
        resSuccess(ResCode.SAVE_SUCCESS.getMsg());
    }

    /**
     * 更新配置
     */
    public void updateConfig() {
        String[] params = {"id"};
        Verify<Map<String, Integer>> verify = intVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        String value = getPara("value");
        if (TextUtils.isEmpty(value)) {
            resFailure(ResCode.INFO_FULL.getMsg());
            return;
        }
        SysConfig sysConfig = new SysConfig().getInfo(verify.getData().get(params[0]));
        sysConfig.setValue(value);
        sysConfig.update();
        resSuccess(ResCode.UPDATE_SUCCESS.getMsg());
    }

    public void delConfig() {
        String[] params = {"id"};
        Verify<Map<String, Integer>> verify = intVerify(params);
        if (verify.getCode() < 0) {
            resFailure(verify.getMsg());
            return;
        }
        SysConfig sysConfig = new SysConfig().getInfo(verify.getData().get(params[0]));
        sysConfig.delete();
        resSuccess(ResCode.DEL_SUCCESS.getMsg());
    }

    /**
     * 根据parentCode获取地区
     */
    public void getArea() {
        try {
            int parentCode = Integer.parseInt(getPara("parentCode"));
            List<Region> list = Region.dao.getList(parentCode);
            resSuccess(list);
        } catch (Exception e) {
            resFailure(ResCode.PARAM_FORMAT.getMsg());
        }

    }

    /**
     * 获取所有省
     */
    public void getAllPrivinces() {
        List<Region> list = Region.dao.getList(100000);
        resSuccess(list);
    }

    public void update() {
        try {
            String configInfo = getPara("configInfo");
            JSONObject goodsJson = (JSONObject) JSONValue.parse(configInfo);
            Identifying identifyingBean = new Identifying();
            String id = goodsJson.get("id").toString();
            identifyingBean.setId(Integer.parseInt(id));
            String title = goodsJson.get("title").toString();
            identifyingBean.setTitle(title);
            String identifying = goodsJson.get("identifying").toString();
            identifyingBean.setIdentifying(identifying);
            identifyingBean.update();

            List<SysConfig> vipGradeList = SysConfig.dao.getConfigList(identifyingBean.getId());
            boolean delete = false;

            String list = getPara("list");
            JSONArray array = (JSONArray) JSONValue.parse(list);
            List<SysConfig> updateGradeList = new ArrayList<SysConfig>();
            int length = vipGradeList.size();
            if (array.size() < vipGradeList.size()) {
                length = array.size();
                delete = true;
            }
            for (int i = 0; i < length; i++) {
                JSONObject json = (JSONObject) array.get(i);
                SysConfig grade = new SysConfig();
                String sid = json.get("id").toString();
                grade.setId(Integer.parseInt(sid));
                String key = json.get("key").toString();
                grade.setKey(Integer.parseInt(key));
                String value = json.get("value").toString();
                grade.setValue(value);
                grade.setIId(identifyingBean.getId());
                String simpleName = json.get("simple_name").toString();
                grade.setSimpleName(simpleName);
                updateGradeList.add(grade);
            }
            SysConfig.dao.updateBatch(updateGradeList);////批量更新
            if (delete) {
                List<SysConfig> deleteGradeList = new ArrayList<SysConfig>();
                for (int i = length; i < vipGradeList.size(); i++) {
                    SysConfig config = vipGradeList.get(i);
                    config.setStatus(1);
                    deleteGradeList.add(vipGradeList.get(i));
                }
                SysConfig.dao.updateBatch(deleteGradeList);
            } else {
                List<SysConfig> addGradeList = new ArrayList<SysConfig>();
                for (int i = length; i < array.size(); i++) {
                    JSONObject json = (JSONObject) array.get(i);
                    SysConfig grade = new SysConfig();
                    String key = json.get("key").toString();
                    grade.setKey(Integer.parseInt(key));
                    String value = json.get("value").toString();
                    grade.setValue(value);
                    String simpleName = json.get("simple_name").toString();
                    grade.setSimpleName(simpleName);
                    grade.setStatus(0);
                    grade.setIId(identifyingBean.getId());
                    addGradeList.add(grade);
                }
                SysConfig.dao.addBatch(addGradeList);
            }

            res(ResCode.SUCCESS);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }
}
