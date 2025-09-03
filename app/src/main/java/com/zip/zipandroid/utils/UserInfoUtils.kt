package com.zip.zipandroid.utils

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.bean.ProductDidInfo
import com.zip.zipandroid.bean.ProductList
import com.zip.zipandroid.bean.ZipUploadUserInfoBean
import com.zip.zipandroid.bean.ZipUserInfoBean

object UserInfoUtils {

    private var mSignKey: String = ""
    private var mMid: Long = 0
    private var mUserNo: String = ""
    private var mUserPhone: String = ""
    private var productType: String = ""
    private var custId: Long = 0

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

    fun getCusId(): Long {
        if (custId > 0) {
            return custId
        }
        val custId = MMKV.defaultMMKV()?.getLong("custId", 0) ?: 0
        if (custId > 0) {
            this.custId = custId
        }
        return custId
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
        MMKV.defaultMMKV()?.remove("custId")
        MMKV.defaultMMKV()?.remove("zipuserNo")
        MMKV.defaultMMKV()?.remove("zipuserphone")
        MMKV.defaultMMKV()?.remove("zipproductDue")
        MMKV.defaultMMKV()?.remove("zipuserInfo")
        MMKV.defaultMMKV()?.remove("productType")
        mSignKey = ""
        mUserNo = ""
        mMid = 0L
    }

    fun setUserPhone(phone: String) {
        mUserPhone = phone
        MMKV.defaultMMKV()?.putString("zipuserphone", phone)
    }

    fun getUserPhone(): String {
        if (!mUserPhone.isNullOrEmpty()) {
            return mUserPhone
        }
        val phone = MMKV.defaultMMKV()?.getString("zipuserphone", "") ?: ""
        if (!phone.isNullOrEmpty()) {
            mUserPhone = phone
        }
        return mUserPhone
    }


    fun getUserInfo(): ZipUserInfoBean {
        val infoStr = MMKV.defaultMMKV()?.getString("zipuserInfo", "")
        info = Gson().fromJson(infoStr, ZipUserInfoBean::class.java)
        return info!!
    }

    fun getUploadUserInfo(): ZipUploadUserInfoBean {
        val infoStr = MMKV.defaultMMKV()?.getString("zipuserInfo", "")
        val info = Gson().fromJson(infoStr, ZipUploadUserInfoBean::class.java)
        return info!!
    }

    fun saveUserInfo(userInfo: String) {
        MMKV.defaultMMKV()?.putString("zipuserInfo", userInfo)
    }


    private var info: ZipUserInfoBean? = null
    private var product: ProductList? = null

    fun getProductType(): ProductList {
        val infoStr = MMKV.defaultMMKV()?.getString("productType", "")
        product = Gson().fromJson(infoStr, ProductList::class.java)
        return product!!
    }

    fun setProductType(productType: String) {
        this.productType = productType
        MMKV.defaultMMKV()?.putString("productType", productType)
    }


    fun saveProductDue(productDue: String) {
        MMKV.defaultMMKV()?.putString("zipproductDue", productDue)
    }

    var mMacawProductDue: ProductDidInfo? = null
    fun getProductDue(): ProductDidInfo {
        if (mMacawProductDue == null) {
            val infoStr = MMKV.defaultMMKV()?.getString("zipproductDue", "")
            mMacawProductDue = Gson().fromJson(infoStr, ProductDidInfo::class.java)
        }
        return mMacawProductDue!!
    }

    fun saveCusId(custId: Long) {
        this.custId = custId
        MMKV.defaultMMKV()?.putLong("custId", custId)
    }


}