package com.mjj.software_project;

/**
 * @author 孟姣姣
 * @desc  接口返回封装类
 * @date 2022年03月24日 2022/3/24
 */
public class DataBean {
    int code;
    Object data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
