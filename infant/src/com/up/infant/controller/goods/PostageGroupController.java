package com.up.infant.controller.goods;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.*;

import java.util.List;

/**
 * TODO:运费管理
 * Created by 蔡跃勇
 * on 2016/11/4 0004. 21:28
 * postage
 */
public class PostageGroupController extends BaseController{

    public void add(){
        try{
            PostageGroup postageGroup=getModel(PostageGroup.class,"");
            postageGroup.save();
            res(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getList(){
        try{
            PostageGroup postageGroup=getModel(PostageGroup.class,"");
            List<PostageGroup> postageGroups=PostageGroup.dao.getList();
            resSuccess(postageGroups);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    //删除
    public void delete(){
        PostageGroup postageGroup=getModel(PostageGroup.class,"");
        if(postageGroup.getId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            //批量删除该运费模板下的运费值设置
            PostageTpl.dao.deleteBatch(postageGroup.getId());
            //删除该运费组
            PostageGroup.dao.deleteById(postageGroup.getId());
            res(ResCode.DEL_SUCCESS);
        }

    }

    /**
     * 更新
     */
    public void update(){
        PostageGroup postageGroup=getModel(PostageGroup.class,"");
        postageGroup.update();
        res(ResCode.UPDATE_SUCCESS);
    }
}
