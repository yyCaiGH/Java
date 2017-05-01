package com.up.infant.model.gen;

/**
 * TODO:
 * Created by 王剑洪
 * on 2017/1/9 0009.
 */

public class LogisticsBean {
    private int showapi_res_code;//showapi平台返回码,0为成功,其他为失败
    private String showapi_res_error;//showapi平台返回的错误信息
    private LogisticsBody showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public LogisticsBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(LogisticsBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
