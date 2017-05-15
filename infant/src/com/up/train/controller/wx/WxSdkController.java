package com.up.train.controller.wx;

import com.jfinal.kit.HttpKit;
import com.up.common.controller.BaseController;
import com.up.train.model.Organization;
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
        if(url.contains("#")){
            url = url.substring(0,url.indexOf("#"));
        }
        String jsapi_ticket = "kgt8ON7yVITDhtdwci0qeda7a9B-Vz29as7SsLu3SuJYv0IhkuAowOuARVM5RX2nffTMKF_c5-_mQClFCC2nlw";
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
}
