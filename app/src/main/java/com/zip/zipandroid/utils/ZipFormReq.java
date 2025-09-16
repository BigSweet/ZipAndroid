package com.zip.zipandroid.utils;

import com.blankj.utilcode.util.AppUtils;
import com.zip.zipandroid.BuildConfig;

import java.util.HashMap;

public class ZipFormReq extends HashMap<String, Object> {

    public ZipFormReq() {
    }

    public static ZipFormReq create() {
        ZipFormReq req = new ZipFormReq();
        req.put("fakitinAiki", AppUtils.getAppPackageName());
        req.put("sigarBincike", AppUtils.getAppVersionName());
        req.put("tushen", "ANDROID");
        if (!UserInfoUtils.INSTANCE.getUserNo().isEmpty()) {
            req.put("matsakaici", UserInfoUtils.INSTANCE.getMid());
            req.put("lambarMutum", UserInfoUtils.INSTANCE.getUserNo());
        }

//        req.put("idAbokinCiniki", "3a2d3b5b-ad80-4f98-a3ff-08900913b146");
        if (BuildConfig.DEBUG && Constants.useDebug) {
//            req.put("idAbokinCiniki", Constants.INSTANCE.getRelease_client_id());
            req.put("idAbokinCiniki", Constants.INSTANCE.getClient_id());
        } else {
            req.put("idAbokinCiniki", Constants.INSTANCE.getRelease_client_id());

        }
        return req;
    }

    public ZipFormReq addParam(String key, Object value) {
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

    public ZipFormReq addParamCheckNull(String key, Object value) {
        if (value != null && key != null) {
            put(key, value);
        }
        return this;
    }

}
