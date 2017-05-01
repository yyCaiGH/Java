package com.up.infant.controller.goods;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:运费模板管理
 * Created by 蔡跃勇
 * on 2016/11/4 0004. 21:28
 * postageTpl
 */
public class PostageTplController extends BaseController{

    public void add(){
        try{
            String provinces = getPara("provinces");
            String pg_id = getPara("pg_id");
            String name = getPara("name");
            String expressage = getPara("expressage");
            PostageTpl postageTpl=new PostageTpl();
            postageTpl.setPgId(Integer.parseInt(pg_id));
            postageTpl.setName(name);
            postageTpl.setExpressage(expressage);
            postageTpl.save();

            int pt_id  = postageTpl.getId();
            Object obj= JSONValue.parse(provinces);
            JSONArray array=(JSONArray)obj;
            List<PostageProvince> list = new ArrayList<PostageProvince>();
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                PostageProvince postageProvince = new PostageProvince();
                String province = json.get("province").toString();
                String postage = json.get("postage").toString();
                postageProvince.setProvinceName(province);
                postageProvince.setPostage(Double.parseDouble(postage));
                postageProvince.setPtId(pt_id);
                list.add(postageProvince);
            }
            PostageProvince.dao.addBatch(list);

            res(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 获取运费组对应的运费模板列表
     */
    public void getList(){
        try{
            PostageTpl postageTpl=getModel(PostageTpl.class,"");
            List<PostageTpl> postageTpls=PostageTpl.dao.getList(postageTpl.getPgId());
            resSuccess(postageTpls);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    //删除
    public void delete(){
        PostageTpl postageTpl=getModel(PostageTpl.class,"");
        if(postageTpl.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            //批量删除该运费模板下的运费值设置
            PostageProvince.dao.deleteBatch(postageTpl.getId());
            //删除该运费模板
            PostageTpl.dao.deleteById(postageTpl.getId());
            res(ResCode.DEL_SUCCESS);
        }

    }

    /**
     * 更新
     */
    public void update(){
        try{
            String provinces = getPara("provinces");
            String id = getPara("id");
            String pg_id = getPara("pg_id");
            String name = getPara("name");
            String expressage = getPara("expressage");
            PostageTpl postageTpl=new PostageTpl();
            postageTpl.setId(Integer.parseInt(id));
            postageTpl.setPgId(Integer.parseInt(pg_id));
            postageTpl.setName(name);
            postageTpl.setExpressage(expressage);
            postageTpl.update();

            int pt_id  = postageTpl.getId();
            Object obj= JSONValue.parse(provinces);
            JSONArray array=(JSONArray)obj;
            List<PostageProvince> list = new ArrayList<PostageProvince>();
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                PostageProvince postageProvince = new PostageProvince();
                String p_id = json.get("id").toString();
                String province = json.get("province").toString();
                String postage = json.get("postage").toString();
                postageProvince.setId(Integer.parseInt(p_id));
                postageProvince.setProvinceName(province);
                postageProvince.setPostage(Double.parseDouble(postage));
                postageProvince.setPtId(pt_id);
                list.add(postageProvince);
            }
            PostageProvince.dao.updateBatch(list);

            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            res(ResCode.PARAM_ERROR);
        }
    }

    /**
     * 获得所有的省份
     */
    public void getProvinces(){
        try{
            Province province = new Province();
            List<Province> list = Province.dao.getList();
            resSuccess(list);
        }catch (Exception e){
            res(ResCode.ERROR);
        }
    }

    /**
     * 获取运费模板对应的属性，包括运费值列表
     */
    public void getById(){
        try{
            PostageTpl postageTpl=getModel(PostageTpl.class,"");
            postageTpl = PostageTpl.dao.findById(postageTpl.getId());
            List<PostageProvince> postageProvinceList=PostageProvince.dao.getList(postageTpl.getId());
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("postageTpl",postageTpl);
            data.put("postageProvinceList",postageProvinceList);
            resSuccess(data);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
    /**
     * 获取运费模板对应的运费值列表
     */
    public void getPostageProvinceList(){
        try{
            PostageTpl postageTpl=getModel(PostageTpl.class,"");
            List<PostageProvince> postageProvinceList=PostageProvince.dao.getList(postageTpl.getId());
            resSuccess(postageProvinceList);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
}
