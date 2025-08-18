package com.zip.zipandroid.utils;

import com.blankj.utilcode.util.AppUtils;

import java.util.HashMap;

public class FormReq extends HashMap<String, Object> {

    public FormReq() {
    }

    public static FormReq create() {
        FormReq req = new FormReq();
        req.put("appPackage", AppUtils.getAppPackageName());
        req.put("version", AppUtils.getAppVersionName());
        req.put("source", "ANDROID");
        req.put("mid", "");
        req.put("userNo", "");
        req.put("clientId", "32f614b2-4a69-43e2-8ee9-e1f26f0a744a");
        return req;
    }

    public FormReq addParam(String key, Object value) {
        if (key == null) {
            return this;
        }
        if (value == null) {
            remove(key);
        } else {
            put(key, value);
        }
        return this;
    }

    public FormReq addParamCheckNull(String key, Object value) {
        if (value != null && key != null) {
            put(key, value);
        }
        return this;
    }

}
