package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.bean.ZipLoginResponse
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipHomeViewModel: ZipBaseViewModel() {
    var codeLiveData = MutableLiveData<ZipCodeBean?>()


    fun getZipAppConfig(){

    }
    fun zipHomeData(phone: String, code: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("cxmnmtyyytobile", phone)
        api.addParam("ctytyxmnctyode", code)
        api.addParam("jkjjkjkjkd", 999)
        treeMap.putAll(api)
        api.addParam("idlad", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getHomeData(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipLoginResponse>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipLoginResponse) {
//                    loginLiveData.postValue(result)

                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message)
                }
            })
    }

}