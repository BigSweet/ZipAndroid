package com.zip.zipandroid.utils;

import com.blankj.utilcode.util.AppUtils;

import java.util.HashMap;

public class FormReq extends HashMap<String, Object> {

    public FormReq() {
    }

    public static FormReq create() {
        FormReq req = new FormReq();
        req.put("fakitinAiki", AppUtils.getAppPackageName());
        req.put("sigarBincike", AppUtils.getAppVersionName());
        req.put("tushen", "ANDROID");
        req.put("matsakaici", "");
        req.put("lambarMutum", "");
        req.put("idAbokinCiniki", "3a2d3b5b-ad80-4f98-a3ff-08900913b146");
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
