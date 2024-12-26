package com.api.mhzx.entity;

import lombok.Data;

/**
 * result message object
 * Created by pine on 2016/6/6.
 */
@Data
public class ResultMessage<T>{
    public Boolean success;
    public int code;
    public String msg;
    public T data;

    public ResultMessage(){}
    public ResultMessage(Boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static <T>ResultMessage<T> success(T data){
        return new ResultMessage(true,0,"",data);
    }
    public static <T>ResultMessage<T> success(int code,T data){
        return new ResultMessage(true,code,"",data);
    }
    public static <T>ResultMessage<T> failure(String msg){
        return new ResultMessage(false,0,msg,"");
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
