package com.zip.zipandroid.utils

import com.tencent.mmkv.MMKV

object UserInfoUtils {

    var mSignKey: String = ""
    var mMid: Long = 0
    var mUserNo: String = ""
    var mRealName: String = ""

    fun getCardNo(): String {
        val cardNo = MMKV.defaultMMKV()?.getString("macawcardNo", "") ?: ""
        return cardNo
    }

    fun setCardNo(cardNo: String) {
        MMKV.defaultMMKV()?.putString("macawcardNo", cardNo)
    }

    fun setIdCardPath(idCardPath: String) {
        MMKV.defaultMMKV()?.putString("idcardPath", idCardPath)
    }

    fun getIdCardPath(): String {
        return MMKV.defaultMMKV()?.getString("idcardPath", "") ?: ""
    }

    fun setSignKey(signKey: String) {
        mSignKey = signKey
        MMKV.defaultMMKV()?.putString("zipsignKey", signKey)
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
        mSignKey = ""
        mUserNo = ""
        mMid = 0L
    }



}