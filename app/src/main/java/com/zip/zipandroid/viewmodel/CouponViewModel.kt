package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.ZipCouponListBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class CouponViewModel : ZipBaseViewModel() {
    var couponLiveData = MutableLiveData<ZipCouponListBean>()

    fun getCouponList(couponStatus: Int) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("couponStatus", couponStatus)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getCouponList(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipCouponListBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipCouponListBean) {
                    couponLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

}