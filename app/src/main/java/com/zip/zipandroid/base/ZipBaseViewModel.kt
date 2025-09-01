package com.zip.zipandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.AppUtils
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.ZipAppConfigBean
import com.zip.zipandroid.bean.ZipBandCardBean
import com.zip.zipandroid.bean.ZipQueryCardBean
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.utils.Constants.client_id
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.UserInfoUtils.getMid
import com.zip.zipandroid.utils.UserInfoUtils.getUserNo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.TreeMap

open class ZipBaseViewModel : ViewModel(), IRxDisManger {

    private var disposables: CompositeDisposable? = null
    override fun onCleared() {
        if (disposables != null) {
            disposables?.clear()
        }
    }

    var failLiveData = MutableLiveData<String>()

    var configLiveData = MutableLiveData<ZipAppConfigBean?>()
    var cardListLiveData = MutableLiveData<ZipQueryCardBean?>()
    var saveMemberInfoLiveData = MutableLiveData<Int>()
    var userInfoLiveData = MutableLiveData<ZipUserInfoBean>()
    var bandCardLiveData = MutableLiveData<ZipBandCardBean>()
    var changeCardLiveData = MutableLiveData<Any>()
    var personDicLiveData = MutableLiveData<PersonalInformationDictBean>()

    fun getZipAppConfig() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getConfig(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipAppConfigBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipAppConfigBean) {
                    configLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun zipQueryCard() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipQueryCard(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipQueryCardBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipQueryCardBean) {
                    cardListLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun saveMemberBehavior(type: Int) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("nauIn", type)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveMemberBehavior(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                }

                override fun onSuccess(result: Any) {
                    saveMemberInfoLiveData.postValue(type)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getUserInfo() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getUserInfo(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipUserInfoBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipUserInfoBean) {
                    userInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun zipBandCard(bankId: String, bankName: String, cardNo: String, cardType: String, firstName: String, fullName: String, identityCardNo: String, lastName: String, phone: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("idBanki", bankId)
        api.addParam("sunanBanki", bankName)
        api.addParam("lambarKatin", cardNo)
        api.addParam("nauInKatin", cardType)
        api.addParam("sunanFarko", firstName)
        api.addParam("cikakkenSunan", fullName)
        api.addParam("lambarKatinAinihin", identityCardNo)
        api.addParam("wayar", phone)
        api.addParam("sunanKarshe", lastName)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipBandCard(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipBandCardBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipBandCardBean) {
                    bandCardLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun zipChangeCard(bankId: String, bankName: String, cardNo: String, cardType: String, firstName: String, fullName: String, identityCardNo: String, lastName: String, phone: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("idBanki", bankId)
        api.addParam("sunanBanki", bankName)
        api.addParam("lambarKatin", cardNo)
        api.addParam("nauInKatin", cardType)
        api.addParam("sunanFarko", firstName)
        api.addParam("cikakkenSunan", fullName)
        api.addParam("lambarKatinAinihin", identityCardNo)
        api.addParam("wayar", phone)
        api.addParam("sunanKarshe", lastName)
        api.addParam("status", "1")
        api.addParam("id", id)
        api.addParam("cvv2", cvv2)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).changeCard(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    changeCardLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getPersonInfoDic() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        ZipRetrofitHelper.createApi(ZipApi::class.java).getPersonalInformationDict(AppUtils.getAppPackageName(),
            AppUtils.getAppVersionName(),
            "ANDROID",
            getMid().toString(),
            getUserNo(),
            client_id,
            SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey())
        )
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<PersonalInformationDictBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: PersonalInformationDictBean) {
                    personDicLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun updateUserInfo() {

    }


    override fun addReqDisposable(disposable: Disposable) {
        if (null == disposables) {
            disposables = CompositeDisposable()
        }
        disposables?.add(disposable)
    }

    override fun clear() {

    }
}