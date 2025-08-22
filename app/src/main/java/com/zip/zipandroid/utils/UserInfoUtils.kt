package com.zip.zipandroid.utils

import com.tencent.mmkv.MMKV

object UserInfoUtils {

    private var mSignKey: String = ""
    private  var mMid: Long = 0
    private var mUserNo: String = ""

    fun setMid(mid: Long) {
        mMid = mid
        MMKV.defaultMMKV()?.putLong("zipmid", mid)
    }


    fun getMid(): Long {
        if (mMid > 0) {
            return mMid
        }
        val mid = MMKV.defaultMMKV()?.getLong("zipmid", 0) ?: 0
        if (mid > 0) {
            mMid = mid
        }
        return mid
    }

    fun setSignKey(signKey: String) {
        mSignKey = signKey
        MMKV.defaultMMKV()?.putString("zipsignKey", signKey)
    }


    fun getUserNo(): String {
        if (!mUserNo.isNullOrEmpty()) {
            return mUserNo
        }
        val userNo = MMKV.defaultMMKV()?.getString("zipuserNo", "") ?: ""
        if (!userNo.isNullOrEmpty()) {
            mUserNo = userNo
        }
        return userNo
    }

    fun setUserNo(userNo: String) {
        mUserNo = userNo
        MMKV.defaultMMKV()?.putString("zipuserNo", userNo)
    }


    fun getSignKey(): String {
        if (!mSignKey.isNullOrEmpty()) {
            return mSignKey
        }
        val signKey = MMKV.defaultMMKV()?.getString("zipsignKey", "") ?: ""
        if (!signKey.isNullOrEmpty()) {
            mSignKey = signKey
        }
        return signKey
    }

    fun clear() {
        MMKV.defaultMMKV()?.remove("zipsignKey")
        MMKV.defaultMMKV()?.remove("zipmid")
        MMKV.defaultMMKV()?.remove("zipuserNo")
        mSignKey = ""
        mUserNo = ""
        mMid = 0L
    }



}