package com.up.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/26 0026. 23:09
 */
public class DateUtils {
    /**
     * 时间对比
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                //2016-11-13,2016-11-12
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                //2016-11-12,2016-11-13
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间格式转换
     * @param str
     * @param isMorePrecision
     * @return
     */
    public static Date format(String str,boolean isMorePrecision){
        String format="";
        if (isMorePrecision){
            format="yyyy-MM-dd hh:mm:ss" ;
        }else {
            format="yyyy-MM-dd";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 时间格式转换
     * @param str
     * @param format yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static Date format(String str,String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取某个日期之后的多少天的日期
     * @param now
     * @param day
     * @return
     */
    public static Date getNextDate(Date now,int day){
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + day);
        return c.getTime();
    }
}
