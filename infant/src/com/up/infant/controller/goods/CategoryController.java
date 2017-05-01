package com.up.infant.controller.goods;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.infant.model.Category;
import com.up.infant.model.Country;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/4 0004. 21:31
 */
public class CategoryController extends BaseController{
    Category dao=new Category();

    public void add(){
        try{
            Category category=getModel(Category.class,"");
            category.save();
            res(ResCode.SAVE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }
    }

    public void update(){
        try{
            Category category=getModel(Category.class,"");
            category.update();
            res(ResCode.UPDATE_SUCCESS);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }
    }

    public void getList(){
        try{
            Category category=getModel(Category.class,"");
            List<Category> categories=dao.getList(category.getParentId());

            resSuccess(categories);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }

    }

    /**
     * 排序，二级紧跟一级后面
     */
    public void getListSort(){
        try{
            List<Category> allCategory = new ArrayList<Category>();

            List<Category> oneCategories=dao.getList(0);
            for (Category category:oneCategories) {
                List<Category> towCategories=dao.getList(category.getId());
                allCategory.add(category);
                allCategory.addAll(towCategories);
            }
            resSuccess(allCategory);
        }catch (Exception e){
            logger.error(e);
            res(ResCode.PARAM_ERROR);
        }

    }

    //删除分类
    public void delete(){
        Category category=getModel(Category.class,"");
        if(category.getId()==null||category.getParentId()==null) {
            res(ResCode.PARAM_ERROR);
        }
        else{
            if (category.getParentId()==0){//批次删除父类及子类
                //批量删除该属性名下的属性值
                dao.deleteBatch(category.getId());
                //删除该属性名
                category = Category.dao.findById(category.getId());
                category.setStatus(1);
                category.update();
            }
            else{
                category = Category.dao.findById(category.getId());
                category.setStatus(1);
                category.update();
            }
            res(ResCode.DEL_SUCCESS);
        }

    }
}
