package com.zip.zipandroid.base;


public class ZipBaseRes<T> {
    private int code;
    private String msg;
    private String sign;
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getResultCode() {
        return code;
    }

    public void setResultCode(int resultCode) {
        this.code = resultCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


}