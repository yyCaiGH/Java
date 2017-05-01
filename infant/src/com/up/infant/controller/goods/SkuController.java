package com.up.infant.controller.goods;

import com.mysql.jdbc.StringUtils;
import com.up.common.base.Verify;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.controller.banner.BannerController;
import com.up.infant.model.Sku;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/4 0004. 21:31
 */
public class SkuController extends BaseController {

    private Sku dao = new Sku();

    //批量插入数据
    public void addBatch() {
        Sku sku = new Sku();
        String skuName = getPara("skuName");
        skuName = skuName.trim();
        String skus = getPara("skus");
        logger.error(skuName + "," + skus);
        sku.setName(skuName);
        sku.setParentId(0);
        if (sku.getByName(skuName) != null) {
            resFailure(ResCode.ERROR.getCode(), "属性名不能重复！");
            return;
        }
        if (sku.save()) {
            int id = sku.getId();
            Object obj = JSONValue.parse(skus);
            JSONArray array = (JSONArray) obj;
            List<Sku> skuList = new ArrayList<Sku>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = (JSONObject) array.get(i);
                Sku newSku = new Sku();
                String name = json.get("name").toString();
                newSku.setName(name);
                newSku.setParentId(id);
                skuList.add(newSku);
            }
            sku.addBatch(skuList);
            res(ResCode.SAVE_SUCCESS);
        } else {
            res(ResCode.ERROR);
        }

    }
    //批量修改,涉及到更新，添加，和删除。直接改成全部删除，再重新添加
    /*public void updateBatch(){
        Sku sku = new Sku();
        String[] strParams={"skuId","skuName","skus","oldSkuName"};
        Verify<Map<String,String>> strVerify=strVerify(strParams);
        if (strVerify.getCode()<0){
           res(ResCode.PARAM_ERROR);return;
        }
        String skuId=strVerify.getData().get(strParams[0]);
        String skuName=strVerify.getData().get(strParams[1]);
        skuName = skuName.trim();
        String skus=strVerify.getData().get(strParams[2]);
        String oldSkuName=strVerify.getData().get(strParams[3]);
        oldSkuName = oldSkuName.trim();

        sku.setId(Integer.parseInt(skuId));
        if(!oldSkuName.equals(skuName)){
            if(dao.getByName(skuName)==null){
                sku.setName(skuName);
                sku.setParentId(0);
                sku.update();//属性名更新
            }
            else {
                resFailure(ResCode.ERROR.getCode(), "属性名不能重复！");
                return;
            }
        }

        //获取旧的属性值，批量删除
        dao.deleteBatch(sku.getId());

        //添加新的属性值
            Object obj= JSONValue.parse(skus);
            JSONArray array=(JSONArray)obj;
            List<Sku> skuList = new ArrayList<Sku>();
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                Sku newSku = new Sku();
                String name = json.get("name").toString();
                newSku.setName(name);
                newSku.setParentId(sku.getId());
                skuList.add(newSku);
            }
            sku.addBatch(skuList);
            res(ResCode.SAVE_SUCCESS);
    }*/

    /**
     * 更新
     */
    public void updateBatch() {
        Sku sku = new Sku();
        String[] strParams = {"skuId", "skuName", "skus", "oldSkuName"};
        Verify<Map<String, String>> strVerify = strVerify(strParams);
        if (strVerify.getCode() < 0) {
            res(ResCode.PARAM_ERROR);
            return;
        }
        String skuId = strVerify.getData().get(strParams[0]);
        String skuName = strVerify.getData().get(strParams[1]);
        skuName = skuName.trim();
        String skus = strVerify.getData().get(strParams[2]);
        String oldSkuName = strVerify.getData().get(strParams[3]);
        oldSkuName = oldSkuName.trim();

        sku.setId(Integer.parseInt(skuId));
        if (!oldSkuName.equals(skuName)) {
            if (dao.getByName(skuName) == null) {
                sku.setName(skuName);
                sku.setParentId(0);
                sku.update();//属性名更新
            } else {
                resFailure(ResCode.ERROR.getCode(), "属性名不能重复！");
                return;
            }
        }

        try {
            List<Sku> vipGradeList = Sku.dao.getList(sku.getId());
            boolean delete = false;
            JSONArray array = (JSONArray) JSONValue.parse(skus);
            List<Sku> updateGradeList = new ArrayList<Sku>();
            int length = vipGradeList.size();
            if (array.size() < vipGradeList.size()) {
                length = array.size();
                delete = true;
            }
            for (int i = 0; i < length; i++) {
                JSONObject json = (JSONObject) array.get(i);
                Sku grade = new Sku();
                String id = json.get("id").toString();
                grade.setId(Integer.parseInt(id));
                grade.setParentId(sku.getId());
                String name = json.get("name").toString();
                grade.setName(name);
                updateGradeList.add(grade);
            }
            Sku.dao.updateBatch(updateGradeList);////批量更新
            if (delete) {
                List<Sku> deleteGradeList = new ArrayList<Sku>();
                for (int i = length; i < vipGradeList.size(); i++) {
                    deleteGradeList.add(vipGradeList.get(i));
                }
                Sku.dao.deleteBatch1(deleteGradeList);
            } else {
                List<Sku> addGradeList = new ArrayList<Sku>();
                for (int i = length; i < array.size(); i++) {
                    JSONObject json = (JSONObject) array.get(i);
                    Sku grade = new Sku();
                    grade.setParentId(sku.getId());
                    String name = json.get("name").toString();
                    grade.setName(name);
                    addGradeList.add(grade);
                }
                Sku.dao.addBatch(addGradeList);
            }

            res(ResCode.SUCCESS);
        } catch (Exception e) {
            res(ResCode.PARAM_ERROR);
        }
    }

    public void add() {
        try {
            Sku sku = getModel(Sku.class, "");
            sku.save();
            res(ResCode.SAVE_SUCCESS);
        } catch (Exception e) {
            //Logger.e("添加Sku",e.getMessage());
            res(ResCode.PARAM_ERROR);
        }
    }

    public void update() {
        try {
            Sku sku = getModel(Sku.class, "");
            sku.update();
            res(ResCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            //Logger.e("更新Sku",e.getMessage());
            res(ResCode.PARAM_ERROR);
        }
    }

    public void delete() {
        Sku sku = getModel(Sku.class, "");
        if (sku.getId() == null) {
            res(ResCode.PARAM_ERROR);
            return;
        } else {
            //批量删除该属性名下的属性值
            //dao.deleteBatch(sku.getId());
            //换伪删除
            dao.updateBatchToDelete(sku.getId());
            //删除该属性名
            //dao.deleteById(sku.getId());
            sku = dao.findById(sku.getId());
            sku.setStatus(1);
            sku.update();
            res(ResCode.DEL_SUCCESS);
        }

    }

    public void getList() {
        try {
            Sku sku = getModel(Sku.class, "");
            List<Sku> skus = new Sku().getList(sku.getParentId());
            resSuccess(skus);
        } catch (Exception e) {
            // Logger.e("获取Sku别表",e.getMessage());
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getListAll() {
        List<Sku> skus = new Sku().getList(0);

    }

    public void getById() {
        try {
            Sku sku = getModel(Sku.class, "");
            sku = sku.findById(sku.getId());
            List<Sku> skus = new Sku().getList(sku.getId());
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("skuName", sku);
            data.put("skus", skus);
            resSuccess(data);
        } catch (Exception e) {
            // Logger.e("获取Sku",e.getMessage());
            res(ResCode.PARAM_ERROR);
        }
    }
}
