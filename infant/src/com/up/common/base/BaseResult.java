package com.up.common.base;

import java.io.Serializable;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/19 0019. 14:28
 */
public class BaseResult<T> extends BaseBean {
    private int code;
    private String msg;
    private T data;


    public BaseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
