package com.zip.zipandroid.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.zip.zipandroid.ZipApplication;


public class PreferencesHelper {
    /**
     * 字段
     */
    // 用户配置
    public static final String TB_USER = "TB_USER";

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;
    Context context;
    private static final String TAG = "PreferencesHelper";


    public PreferencesHelper(Context c, String tbName) {
        context = c;
        mPreferences = context.getSharedPreferences(tbName, 0);
        mEditor = mPreferences.edit();
    }

    public PreferencesHelper(String tbName) {
        context = ZipApplication.Companion.getInstance();
        mPreferences = context.getSharedPreferences(tbName, 0);
        mEditor = mPreferences.edit();
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }


    /**
     * 设置参数
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        mEditor = mPreferences.edit();
        if (value == null) {
            value = "";
//            mEditor.remove(key);
        }
        mEditor.putString(key, value);
        mEditor.commit();

    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        return mPreferences.getString(key, null);
    }


    public void setIntValue(String key, int value) {
        mEditor = mPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getIntValue(String key) {
        return mPreferences.getInt(key, 0);
    }

    public int getIntValue(String key, int def) {
        return mPreferences.getInt(key, def);
    }


    public void setLongValue(String key, long value) {
        mEditor = mPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public long getLongValue(String key) {
        return mPreferences.getLong(key, 0);
    }

    /**
     * 设置boolean值
     *
     * @param key
     * @param value
     */
    public void setBooleanValue(String key, boolean value) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(key, value);
        boolean commit = mEditor.commit();
//        DebugLog.d(TAG, "setBooleanValue: " + commit);
    }

    /**
     * 获取boolean值
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    /**
     * 带默认值的获取参数
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getValue(String key, String defaultValue) {
        if (!mPreferences.contains(key)) {
            return defaultValue;
        }
        return mPreferences.getString(key, defaultValue);
    }

    public void remove(String name) {
        mEditor.remove(name);

    }

    public void clearHelper() {
        mEditor = mPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }
}
