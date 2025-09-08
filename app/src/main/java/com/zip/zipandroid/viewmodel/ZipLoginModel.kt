package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.BuildConfig
import com.zip.zipandroid.base.ZipRxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.bean.ZipLoginResponse
import com.zip.zipandroid.utils.ZipFormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipLoginModel : ZipBaseViewModel() {

    var codeLiveData = MutableLiveData<ZipCodeBean?>()

    var loginLiveData = MutableLiveData<ZipLoginResponse>()

    fun zipLogin(phone: String, code: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.addParam("wayarHannu", phone)
        api.addParam("lambarco", code)
        api.addParam("rijistaDaga", 999)
        api.addParam("IMEI", DeviceUtils.getAndroidID())
        api.addParam("nauIn", 0)
//        api.addParam("channelUtmInfo", ZipChannelUtmInfo())
        api.addParam("tashoshi", "google-play")
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipLogin(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipLoginResponse>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipLoginResponse) {
                    loginLiveData.postValue(result)

                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message)
                }
            })
    }


    fun getCode(phone: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.addParam("wayarHannu", phone)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getCode(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipCodeBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipCodeBean) {
                    codeLiveData.postValue(result)
                    if (BuildConfig.DEBUG) {
                        ToastUtils.showShort(result?.code.toString())
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message)
                }
            })


    }


    var APP_REGISTER_AGREEMENT: String = ""//服务条款
    var APP_PRIVACY_AGREEMENT: String = ""//隐私申明


}