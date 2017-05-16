package com.up.train.utils;

import com.jfinal.kit.HttpKit;
import com.up.train.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class wxSdkUtils {
    private final static String appid = "wxc93df0671af5918e";
    private final static String secret = "0600d92239e320b891bc87e693068050";
    public static String getAccessToken(){
        String result = HttpKit.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxc93df0671af5918e&secret=0600d92239e320b891bc87e693068050");
        JSONParser parser = new JSONParser();
        try {
            JSONObject parseObject = (JSONObject)parser.parse(result);
            String accessToken = parseObject.get("access_token").toString();
            System.out.println("accessToken---->" + accessToken);
            return  accessToken;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public static String getTicket(String accessToken){
        String result = HttpKit.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi");
        JSONParser parser = new JSONParser();
        try {
            JSONObject parseObject = (JSONObject)parser.parse(result);
            String ticket = parseObject.get("ticket").toString();
            System.out.println("ticket---->" + ticket);
            return  ticket;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  "";
    }

    /**
     * 微信授权登陆获取openId和accessToken
     * @param code
     * @return strs[0]=accessToken,str[1]=openid
     */
    public static String[] getOpenIdAndAccessToken(String code){
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        String result = HttpKit.get(requestUrl);
        JSONParser parser = new JSONParser();
        try {
            JSONObject parseObject = (JSONObject)parser.parse(result);
            String accessToken = parseObject.get("access_token").toString();
            String openid = parseObject.get("openid").toString();
            System.out.println("access_token---->" + accessToken);
            System.out.println("openid---->" + openid);
            String strs[] ={accessToken,openid};
            return  strs;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;

    }

    /**
     * 获取微信用户信息
     * @return
     */
    public static User getWxUserInfo(String accessToken,String openid){
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN";
        String result = HttpKit.get(requestUrl);
        JSONParser parser = new JSONParser();
        try {
            JSONObject parseObject = (JSONObject)parser.parse(result);
            String nickname = parseObject.get("nickname").toString();
            String headimgurl = parseObject.get("headimgurl").toString();
            System.out.println("nickname---->" + nickname);
            User user = new User();
            user.setOpenId(openid);
            user.setName(nickname);
            user.setHeadimgurl(headimgurl);
            return  user;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
