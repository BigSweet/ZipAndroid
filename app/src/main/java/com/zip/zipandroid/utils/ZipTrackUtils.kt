package com.zip.zipandroid.utils

import com.blankj.utilcode.util.DeviceUtils
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.base.ZipRxSchedulers
import com.zip.zipandroid.utils.phonedate.location.device.ZipDeviceInfoUtil
import io.reactivex.disposables.Disposable
import java.util.TreeMap

object ZipTrackUtils {


    fun track(eventName: String,bizId: String? = null) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.put("idNaUra", ZipDeviceInfoUtil(ZipApplication.instance).genaralDeviceId)
        api.put("darajarLamari", eventName)
        if (!Constants.currentPid.isNullOrEmpty()) {
            api.put("idprd", Constants.currentPid)
        }
        if (!bizId.isNullOrEmpty()) {
            api.put("idKasuwancin", bizId)
        }
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).trackEvent(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)

                }

                override fun onSuccess(result: Any) {
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                }
            })
    }
}