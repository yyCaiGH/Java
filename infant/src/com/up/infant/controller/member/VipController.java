package com.up.infant.controller.member;

import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.model.SkuGoods;
import com.up.infant.model.VipGrade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;
import java.util.regex.Pattern;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/10 0010. 22:59
 */
public class VipController extends BaseController{
    public void edit(){
//        String json=getPara("json");
//        Object obj= JSONValue.parse(json);
//        JSONArray array=(JSONArray)obj;
//        List<VipGrade> skuList = new ArrayList<VipGrade>();
//        for (int i = 0 ;i<array.size();i++){
//            JSONObject jsonObject=(JSONObject)array.get(i);
//        }
        List<VipGrade> vipGrades=getModelList(VipGrade.class,"");
        vipGrades.size();

    }

    public void getList(){
        try{
            List<VipGrade> list = VipGrade.dao.getList();
            resSuccess(list);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }

    public void add(){
        List<VipGrade> vipGradeList = VipGrade.dao.getList();
        boolean delete = false;
        try {
            String list = getPara("list");

            JSONArray array = (JSONArray) JSONValue.parse(list);
            List<VipGrade> updateGradeList = new ArrayList<VipGrade>();
            int length = vipGradeList.size();
            if (array.size()<vipGradeList.size()){
                length = array.size();
                delete = true;
            }
            for (int i = 0; i < length; i++) {
                JSONObject json = (JSONObject) array.get(i);
                VipGrade grade = new VipGrade();
                String id = json.get("id").toString();
                grade.setId(Integer.parseInt(id));
                String name = json.get("name").toString();
                grade.setName(name);
                String amount = json.get("amount").toString();
                grade.setAmount(Double.parseDouble(amount));
                String discount = json.get("discount").toString();
                grade.setDiscount(Double.parseDouble(discount));
                String rebate = json.get("rebate").toString();
                grade.setRebate(Double.parseDouble(rebate));
                updateGradeList.add(grade);
            }
            VipGrade.dao.updateBatch(updateGradeList);////批量更新等级
            if (delete){
                List<VipGrade> deleteGradeList = new ArrayList<VipGrade>();
                for(int i = length;i<vipGradeList.size();i++){
                    deleteGradeList.add(vipGradeList.get(i));
                }
                VipGrade.dao.deleteBatch(deleteGradeList);
            }
            else{
                List<VipGrade> addGradeList = new ArrayList<VipGrade>();
                for (int i =  length; i < array.size(); i++) {
                    JSONObject json = (JSONObject) array.get(i);
                    VipGrade grade = new VipGrade();
                    String name = json.get("name").toString();
                    grade.setName(name);
                    String amount = json.get("amount").toString();
                    grade.setAmount(Double.parseDouble(amount));
                    String discount = json.get("discount").toString();
                    grade.setDiscount(Double.parseDouble(discount));
                    String rebate = json.get("rebate").toString();
                    grade.setRebate(Double.parseDouble(rebate));
                    addGradeList.add(grade);
                }
                VipGrade.dao.addBatch(addGradeList);
            }

            res(ResCode.SUCCESS);
        }catch (Exception e){
            res(ResCode.PARAM_ERROR);
        }
    }
}
