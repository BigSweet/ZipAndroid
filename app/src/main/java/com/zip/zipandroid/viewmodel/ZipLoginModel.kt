package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.BuildConfig
import com.zip.zipandroid.base.ResponseSubscriber
import com.zip.zipandroid.base.RetrofitHelper
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.bean.ZipCodeBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipLoginModel : ZipBaseViewModel() {

    var codeLiveData = MutableLiveData<ZipCodeBean?>()

//    var loginLiveData = MutableLiveData<LoginResponse?>()

    fun macawLogin(phone: String, code: String) {

    }


    fun getCode(phone: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("mobile", phone)
        treeMap.putAll(api)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
//        api.sign = SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey())
        RetrofitHelper.createApi(ZipApi::class.java).getCode(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ResponseSubscriber<ZipCodeBean>() {
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
                    codeLiveData.postValue(null)
                }
            })


    }


    var APP_REGISTER_AGREEMENT: String = ""//服务条款
    var APP_PRIVACY_AGREEMENT: String = ""//隐私申明


}