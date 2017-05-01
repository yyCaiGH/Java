package com.up.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * TODO:随机工具
 * Created by 王剑洪
 * on 2016/11/1 0001. 22:56
 */
public class RandomUtils {

    /**
     * 获取随机码UUID
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        String u=uuid.toString().replace("-","");
        return u;
    }

    /**
     * 随机生成指定位数的字符串
     * @param length
     * @return
     */
    public static String randomStr(int length){
        String base = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
