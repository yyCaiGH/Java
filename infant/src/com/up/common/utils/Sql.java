package com.up.common.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * TODO:sql语句简单封装
 * Created by 王剑洪
 * on 2016/10/19 0019. 12:43
 */
public class Sql {
    /**
     * 查询的列
     * @param strings
     * @return
     */
    public static String select(String... strings) {
        String select = "SELECT DISTINCT ";
        if (strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                select += strings[i];
                if (i == strings.length - 1) {
                    select += " ";
                } else {
                    select += ", ";
                }
            }
        } else {
            select += " * ";
        }

        return select;
    }

    /**
     * 表
     * @param table
     * @return
     */
    public static String from(String table) {
        return " FROM " + table + " ";
    }

    /**
     * 条件
     * @param map
     * @return
     */
    public static String where(Map<String, String> map) {
        String where = " WHERE 1=1 ";
        Iterator i = map.entrySet().iterator();
        while (i.hasNext()) {//只遍历一次,速度快
            Map.Entry e = (Map.Entry) i.next();
            String temp = " AND " + e.getKey() + " = '" + e.getValue()+"' ";
            where += temp;
        }
        return where;
    }

//    public static String leftJoin(String table ,Map<String, String> map){
//        String leftJoin=" LEFT JOIN "+table+" ON ";
//        Iterator i = map.entrySet().iterator();
//        while (i.hasNext()) {//只遍历一次,速度快
//            Map.Entry e = (Map.Entry) i.next();
//            String temp = " AND " + e.getKey() + " = " + e.getValue();
//            where += temp;
//        }
//    }

    /**
     * 排序
     * @param column
     * @param isAsc 是否升序
     * @return
     */
    public static String orderBy(String column,boolean isAsc){
        return " ORDER BY " +column+" " +(isAsc?"ASC":"DESC");
    }

    /**
     * 查询条数
     * @param count
     * @return
     */
    public static String limit(int count){
        return " LIMIT "+count;
    }
}
