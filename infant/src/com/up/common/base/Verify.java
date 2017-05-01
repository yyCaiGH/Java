package com.up.common.base;

import com.up.common.def.ResCode;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/10/21 0021. 22:44
 */
public class Verify<T> extends BaseBean {



    private T data;
    private ResCode resCode;
    private int code;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResCode getResCode() {
        return resCode;
    }

    public void setResCode(ResCode resCode) {
        this.resCode = resCode;
        this.code=resCode.getCode();
        this.msg=resCode.getMsg();
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
}
