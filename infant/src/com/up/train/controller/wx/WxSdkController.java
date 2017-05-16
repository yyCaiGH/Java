package com.up.train.controller.wx;

import com.jfinal.kit.HttpKit;
import com.up.common.controller.BaseController;
import com.up.common.def.ResCode;
import com.up.train.model.Organization;
import com.up.train.model.User;
import com.up.train.utils.SignUtil;
import com.up.train.utils.wxSdkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class WxSdkController  extends BaseController {

    /**
     * 微信分享
     */
    public void share(){
        System.out.println("===========share=========");
        String url = getPara("url");
        boolean signatureAgain = getParaToBoolean("signatureAgain");
        if(url.contains("#")){
            url = url.substring(0,url.indexOf("#"));
        }
        String jsapi_ticket = "";
        if (signatureAgain){
            String accessToken = wxSdkUtils.getAccessToken();
            jsapi_ticket = wxSdkUtils.getTicket(accessToken);
            getSession().setAttribute("jsapi_ticket",jsapi_ticket);
        }
        else {
            jsapi_ticket =getSessionAttr("jsapi_ticket");
        }
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();
        System.out.println("jsapi_ticket="+jsapi_ticket);
        System.out.println("nonceStr="+nonceStr);
        System.out.println("timestamp="+timestamp);
        System.out.println("url="+url);
        try {
            String signature = SignUtil.getSignature(jsapi_ticket, nonceStr, timestamp, url);
            System.out.println("===========signature========="+signature);
            Map<String, String> ret = new HashMap<String, String>();
            ret.put("url", url);
            ret.put("jsapi_ticket", jsapi_ticket);
            ret.put("nonceStr", nonceStr);
            ret.put("timestamp", timestamp);
            ret.put("signature", signature);
            ret.put("appid", "wxc93df0671af5918e");
            resSuccess("获取数据成功",ret);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resSuccess("饿。。。");
        //String accessToken = wxSdkUtils.getAccessToken();

    }


    public void getWxUserInfo(){
        String code = getPara("code");
        int type = getParaToInt("type");//授权方式：1：静默授权，2：动态授权
        String[] strs = wxSdkUtils.getOpenIdAndAccessToken(code);
        if (strs==null){
            resFailure("获取openId和accessToken失败");
            return;
        }
        String accessToken = strs[0];
        String openid = strs[1];
        User user = null;
        if (type==1){
            user = User.dao.getByOpenId(openid);
            if (user==null){
                resFailure(ResCode.FIRST_LOGIN.getCode(),ResCode.FIRST_LOGIN.getMsg());
                return;
            }
        }
        else{
            user = wxSdkUtils.getWxUserInfo(accessToken,openid);
            user.save();
        }
        resSuccess(user);
    }
}
