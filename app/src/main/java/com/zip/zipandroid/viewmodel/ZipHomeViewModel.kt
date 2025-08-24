package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.ZipAdBean
import com.zip.zipandroid.bean.ZipAppConfigBean
import com.zip.zipandroid.bean.ZipHomeDataBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipHomeViewModel : ZipBaseViewModel() {
    var homeLiveData = MutableLiveData<ZipHomeDataBean?>()
    var zipAdLiveData = MutableLiveData<ZipAdBean?>()



    fun zipHomeData() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getHomeData(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipHomeDataBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipHomeDataBean) {
//                    loginLiveData.postValue(result)
                    homeLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message)
                }
            })
    }

    fun getAdInfo(appQaAdv: String?) {

        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.put("idYankinTalla",appQaAdv)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))

        ZipRetrofitHelper.createApi(ZipApi::class.java).selectAdvertList(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipAdBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipAdBean) {
//                    loginLiveData.postValue(result)
                    zipAdLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message)
                }
            })
    }

}