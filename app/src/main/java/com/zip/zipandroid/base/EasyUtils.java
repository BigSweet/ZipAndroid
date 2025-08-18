package com.zip.zipandroid.base;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/11/17
 *    desc   : 请求工具类
 */
public final class EasyUtils {


    /**
     * 格式化 Json 字符串
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Nullable
    public static String formatJson(@Nullable String json) {
        if (json == null) {
            return null;
        }

        try {
            if (json.startsWith("{")) {
                return unescapeJson(new JSONObject(json).toString(4));
            } else if (json.startsWith("[")) {
                return unescapeJson(new JSONArray(json).toString(4));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 去除 Json 中非必要的字符转义
     */
    @NonNull
    public static String unescapeJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return "";
        }
        // https://github.com/getActivity/EasyHttp/issues/67
        return json.replace("\\/", "/");
    }

    /**
     * 获取对象的唯一标记
     */
    @Nullable
    public static String getObjectTag(Object object) {
        if (object == null) {
            return null;
        }
        return String.valueOf(object);
    }

    /**
     * 创建文件夹
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) {
                return;
            }
            targetFolder.delete();
        }
        targetFolder.mkdirs();
    }

}