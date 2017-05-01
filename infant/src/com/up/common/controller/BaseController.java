package com.up.common.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.up.common.base.BaseResult;
import com.up.common.base.Verify;
import com.up.common.def.ResCode;
import com.up.common.utils.TextUtils;
import com.up.infant.controller.banner.BannerController;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/19 0019. 14:34
 */
public class BaseController extends Controller {


    protected final Logger logger = Logger.getLogger(this.getClass());

    protected String rootPath = PathKit.getWebRootPath()+"/";

    protected void resFailure(int code, String msg) {
        res(code, msg, null);
    }

    protected void resFailure(String msg) {
        res(ResCode.ERROR.getCode(), msg, null);
    }

    protected void resSuccess(String msg) {
        res(ResCode.SUCCESS.getCode(), msg, null);
    }

    protected <T> void resSuccess(String msg, T t) {
        res(ResCode.SUCCESS.getCode(), msg, t);
    }

    protected <T> void resSuccess(T t) {
        res(ResCode.SUCCESS.getCode(), ResCode.SUCCESS.getMsg(), t);
    }

    protected void res(ResCode code){
        res(code.getCode(),code.getMsg(),null);
    }

    /**
     * json返回
     *
     * @param code
     * @param msg
     * @param t
     * @param <T>
     */
    protected <T> void res(int code, String msg, T t) {
        getResponse().addHeader("Access-Control-Allow-Origin", "*");
        BaseResult<T> result = new BaseResult<T>(code, msg, t);
        renderJson(result);
    }


    /**
     * 判断String型参数是否为空
     *
     * @param params
     * @return
     */
    protected Verify<Map<String, String>> strVerify(String[] params) {
        Verify<Map<String, String>> verify = new Verify<Map<String, String>>();
        boolean isInfoFull = true;
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            String temp = getPara(params[i]);
            if (TextUtils.isEmpty(temp)) {
                isInfoFull = false;
                i = params.length;
            } else {
                map.put(params[i], temp);
            }
        }
        if (isInfoFull) {
            verify.setResCode(ResCode.SUCCESS);
            verify.setData(map);
        } else {
            verify.setResCode(ResCode.INFO_FULL);
        }
        return verify;
    }

    /**
     * 判断Int型参数格式
     * @param params
     * @return
     */
    protected Verify<Map<String, Integer>> intVerify(String[] params) {
        Verify<Map<String, Integer>> verify = new Verify<Map<String, Integer>>();
        boolean isInteger = true;
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < params.length; i++) {
            String temp = getPara(params[i]);
            try {
                map.put(params[i], Integer.parseInt(temp));
            } catch (Exception e) {
                isInteger = false;
                i = params.length;
            }
        }
        if (isInteger) {
            verify.setResCode(ResCode.SUCCESS);
            verify.setData(map);
        } else {
            verify.setResCode(ResCode.PARAM_FORMAT);
        }
        return verify;
    }

    public <T> List<T> getModelList(Class<T> modelClass, String modelName){
        Pattern p = Pattern.compile(modelName + "\\[\\d\\].[a-zA-z0-9]+");
        Map<String, String[]> parasMap = getRequest().getParameterMap();
        String paraKey;
        Set<String> modelPrefix = new HashSet<String>();
        for (Map.Entry<String, String[]> e : parasMap.entrySet()) {
            paraKey = e.getKey();
            if(p.matcher(paraKey).find()){
                modelPrefix.add(paraKey.split("\\.")[0]);
            }
        }
        List<T> resultList = new ArrayList<T>();
        for (String modelName2 : modelPrefix) {
            resultList.add(getModel(modelClass,modelName2));
        }
        return resultList;
    }
}
