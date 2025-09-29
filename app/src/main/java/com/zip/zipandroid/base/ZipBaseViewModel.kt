package com.zip.zipandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.AppUtils
import com.zip.zipandroid.BuildConfig
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.WebLoanBean
import com.zip.zipandroid.bean.ZipAppConfigBean
import com.zip.zipandroid.bean.ZipBandCardBean
import com.zip.zipandroid.bean.ZipQueryCardBean
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.UserInfoUtils.getMid
import com.zip.zipandroid.utils.UserInfoUtils.getUserNo
import com.zip.zipandroid.utils.ZipFormReq
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import java.util.TreeMap

open class ZipBaseViewModel : ViewModel(), ZipIRxDisManger {

    private var disposables: CompositeDisposable? = null
    override fun onCleared() {
        if (disposables != null) {
            disposables?.clear()
        }
    }


    var agreementNameLive = MutableLiveData<ResponseBody?>()

    fun getProtocolBeforeLoan(agreementName: String, appName: String,data: WebLoanBean) {

        var clientId = Constants.client_id
        if (BuildConfig.DEBUG && Constants.useDebug) {
            clientId = Constants.client_id
        } else {
            clientId = Constants.release_client_id
        }
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.addParam("sunanAiki", appName)
        api.addParam("sunanYarjejeniya", agreementName)

        api.addParam("adadinNema", data.applyAmount)
        api.addParam("lokacinNema", System.currentTimeMillis())
        api.addParam("sunanBanki", data.bankName)
        api.addParam("idKasuwancin", data.bizId)
        api.addParam("lambarKatin", data.cardNo)
        api.addParam("lambarWaya", data.phoneNum)
        api.addParam("matakinHadariNext", data.riskLevel)
        api.addParam("nauInSamfur", data.productType)
        api.addParam("dNaUraid", System.currentTimeMillis())
        api.addParam("lambarBashi", data.creditNo)

        treeMap.putAll(api)
//        val sign = SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey())
//        return "http://loansapp.flaminghorizon.com/api/v4/ziplead/getProtocolBeforeLoan?fakitinAiki=${AppUtils.getAppPackageName()}&sigarBincike=${AppUtils.getAppVersionName()}&tushen=${"ANDROID"}&matsakaici=${UserInfoUtils.getMid().toString()}&lambarMutum=${UserInfoUtils.getUserNo().toString()}&idAbokinCiniki=${clientId}&sunanAiki=${appName}&sunanYarjejeniya=${agreementName}&sanyaHannu=${sign}"
        ZipRetrofitHelper.createApi(ZipApi::class.java).getProtocolBeforeLoan(
            data.creditNo.toString(),
            data.applyAmount.toString(),
            data.riskLevel.toString(),
            data.productType.toString(),
            data.phoneNum.toString(),
            data.did.toString(),
            data.cardNo.toString(),
            data.bizId.toString(),
            data.bankName.toString(),
            data.applyTime.toString(),




            AppUtils.getAppPackageName(),
            AppUtils.getAppVersionName(),
            "ANDROID",
            UserInfoUtils.getMid().toString(),
            UserInfoUtils.getUserNo(),
            clientId,
            appName, agreementName, SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : Consumer<ResponseBody> {

                override fun accept(t: ResponseBody?) {
                    agreementNameLive.postValue(t)
                }
            })
    }

    var failLiveData = MutableLiveData<String?>()

    var configLiveData = MutableLiveData<ZipAppConfigBean?>()
    var cardListLiveData = MutableLiveData<ZipQueryCardBean?>()
    var saveCardListLiveData = MutableLiveData<ZipQueryCardBean?>()
    var saveMemberInfoLiveData = MutableLiveData<Int>()
    var userInfoLiveData = MutableLiveData<ZipUserInfoBean>()
    var bandCardLiveData = MutableLiveData<ZipBandCardBean>()
    var changeCardLiveData = MutableLiveData<Any>()
    var personDicLiveData = MutableLiveData<PersonalInformationDictBean>()

    fun getZipAppConfig() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getConfig(api)
            .compose(ZipRxSchedulers.io_main())
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

    fun zipSaveCard() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipQueryCard(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipQueryCardBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipQueryCardBean) {
                    saveCardListLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }


    fun zipQueryCard() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipQueryCard(api)
            .compose(ZipRxSchedulers.io_main())
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
        val api = ZipFormReq.create()
        api.addParam("nauIn", type)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveMemberBehavior(api)
            .compose(ZipRxSchedulers.io_main())
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
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
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
        val api = ZipFormReq.create()
        api.addParam("idBanki", bankId)
        api.addParam("sunanBanki", bankName)
        api.addParam("lambarKatin", cardNo)
        api.addParam("nauInKatin", cardType)
        api.addParam("sunanFarko", firstName)
        api.addParam("cikakkenSunan", fullName)
        api.addParam("lambarKatinAinihin", identityCardNo)
        api.addParam("wayar", phone)
        api.addParam("tilasDauraKatin", true)
        api.addParam("sunanKarshe", lastName)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).zipBandCard(api)
            .compose(ZipRxSchedulers.io_main())
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

    fun zipChangeCard(bankId: String, bankName: String, cardNo: String, cardType: String, firstName: String, fullName: String, identityCardNo: String, lastName: String, phone: String, tiedCardId: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.addParam("idBanki", bankId)
        api.addParam("sunanBanki", bankName)
        api.addParam("lambarKatin", cardNo)
        api.addParam("nauInKatin", cardType)
        api.addParam("sunanFarko", firstName)
        api.addParam("cikakkenSunan", fullName)
        api.addParam("lambarKatinAinihin", identityCardNo)
        api.addParam("wayar", phone)
        api.addParam("sunanKarshe", lastName)
        api.addParam("matsayin", "1")
        api.addParam("ID", tiedCardId)
//        api.addParam("cvv2", cvv2)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).changeCard(api)
            .compose(ZipRxSchedulers.io_main())
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
        var clientId = Constants.client_id
        if (BuildConfig.DEBUG && Constants.useDebug) {
            clientId = Constants.client_id
        } else {
            clientId = Constants.release_client_id
        }

        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        ZipRetrofitHelper.createApi(ZipApi::class.java).getPersonalInformationDict(AppUtils.getAppPackageName(),
            AppUtils.getAppVersionName(),
            "ANDROID",
            getMid().toString(),
            getUserNo(),
            clientId,
            SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey())
        )
            .compose(ZipRxSchedulers.io_main())
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