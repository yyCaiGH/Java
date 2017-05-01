package com.up.train.utils;

import com.jfinal.kit.HttpKit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class wxSdkUtils {
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
}
