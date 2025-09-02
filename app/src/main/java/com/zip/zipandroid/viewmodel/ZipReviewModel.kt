package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.ProductPidBean
import com.zip.zipandroid.bean.ZipBizBean
import com.zip.zipandroid.bean.ZipOrderAdmissionBean
import com.zip.zipandroid.bean.ZipPushData
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.phonedate.applist.InstalledApp
import com.zip.zipandroid.utils.phonedate.calendar.CalendarInfos
import com.zip.zipandroid.utils.phonedate.calllog.CallLog
import com.zip.zipandroid.utils.phonedate.location.device.DeviceInfoUtil
import com.zip.zipandroid.utils.phonedate.sms.SMSMessage
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipReviewModel : ZipBaseViewModel() {
    val productLiveData = MutableLiveData<ProductPidBean>()
    val orderTrialLiveData = MutableLiveData<ZipTriaBean>()
//    var userOrderLiveData = MutableLiveData<MacawOrderPayBean?>()

    fun getPidProduct(riskGrade: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("mid", UserInfoUtils.getMid().toString())
            .addParam("userNo", UserInfoUtils.getUserNo())
            .addParam("riskGrade", riskGrade)
            .addParam("version", AppUtils.getAppVersionName())
            .addParam("pid", UserInfoUtils.getProductType().pid.toString())
        treeMap.putAll(api)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).findProductDueByPid(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ProductPidBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ProductPidBean) {
                    productLiveData.postValue(result)
                }
            })
    }

    fun orderTrial(loanAmount: String, riskGrade: String, did: Long) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("mid", UserInfoUtils.getMid().toString())
            .addParam("userNo", UserInfoUtils.getUserNo())
            .addParam("version", AppUtils.getAppVersionName())
            .addParam("loanAmount", loanAmount)
            .addParam("riskGrade", riskGrade)
            .addParam("did", did)
        treeMap.putAll(api)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).orderTrial(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipTriaBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipTriaBean) {
                    orderTrialLiveData.postValue(result)
                }
            })
    }


    val admissionLiveData = MutableLiveData<ZipOrderAdmissionBean>()
    fun admission() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
            .addParam("idCustomer", UserInfoUtils.getUserInfo().custId.toString())
            .addParam("nauInSamfur", UserInfoUtils.getProductType())
        treeMap.putAll(api)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).admission(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipOrderAdmissionBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipOrderAdmissionBean) {
                    admissionLiveData.postValue(result)
                }
            })
    }

    val riskLevelLiveData = MutableLiveData<ZipRiskLevelBean>()
    fun getRiskLevel(bizId: String) {

        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("mid", UserInfoUtils.getMid().toString())
            .addParam("userNo", UserInfoUtils.getUserNo())
            .addParam("custId", UserInfoUtils.getUserInfo().custId.toString())
            .addParam("bizId", bizId)
            .addParam("version", AppUtils.getAppVersionName())
        treeMap.putAll(api)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getRiskLevel(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipRiskLevelBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipRiskLevelBean) {
                    riskLevelLiveData.postValue(result)
                }
            })
    }


    //设备信息、短信、日历、蓝牙、Advertising ID、WIFI这些都要吗
    fun preOrder(
        callInfo: Array<CallLog?>?, installAppInfo: Array<InstalledApp?>?, smsMessageInfo: Array<SMSMessage?>?, calendarInfo: Array<CalendarInfos?>?,
        currentBizId: String,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("sourceChannel", "google-play")
        api.addParam("bizId", currentBizId)
        api.addParam("applyAmount", "")
        api.addParam("applyPeriod", "")
        api.addParam("productType", UserInfoUtils.getProductType().productType)
        api.addParam("productName", UserInfoUtils.getProductType().productName)
        api.addParam("ambushThirdKeyInfo", "")
        api.addParam("repayment", "4")
        api.addParam("custId", UserInfoUtils.getUserInfo().custId.toString())
        api.addParam("deviceId", DeviceInfoUtil(ZipApplication.instance).genaralDeviceId)
        val pushData = ZipPushData()
        pushData.setCallLogs(callInfo)
        pushData.setInstalledApps(installAppInfo)
        pushData.setMessage(smsMessageInfo)
        pushData.setCalendarInfos(calendarInfo)
        pushData.setMediaData()
        val bean = UserInfoUtils.getUserInfo()
//        bean.cardNo = UserInfoUtils.getSelectBank().cardNo.toString()
//        bean.accountName = accountName
//        bean.bankName = bankName
//        bean.bankId = bankId
//        bean.taxNumber = UserInfoUtils.getShuiNumber()
//        bean.emergentContacts = Gson().fromJson(bean.emergencyContactPerson,
//            object : TypeToken<List<MacawEmergentContactsBean>>() {}.type)
        treeMap.putAll(api)
        api.addParam("pushData", pushData)
        api.addParam("customerInfo", bean)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).creationOrderBefore(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipBizBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipBizBean) {
                    preOrderLiveData.postValue(result)
                }
            })

    }



    fun realOrder(
        applyAmount: String,
        applyPeriod: String,
        myBankName: String,
        myBankId: String,
        fullName: String,
        riskLevel: String,
        bizId: String,
        callInfo: Array<CallLog?>?, installAppInfo: Array<InstalledApp?>?, smsMessageInfo: Array<SMSMessage?>?, calendarInfo: Array<CalendarInfos?>?,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("productName", UserInfoUtils.getProductType().productName.toString())
        api
            .addParam("userNo", UserInfoUtils.getUserNo())
            .addParam("applyPeriod", applyPeriod)
            .addParam("custId", UserInfoUtils.getUserInfo().custId.toString())
            .addParam("applyAmount", applyAmount)
            .addParam("deviceId", DeviceInfoUtil(ZipApplication.instance).genaralDeviceId)
            .addParam("productType", UserInfoUtils.getProductType().productType.toString())
            .addParam("bizId", bizId)
            .addParam("source", "ANDROID")
            .addParam("mid", UserInfoUtils.getMid().toString())
            .addParam("repayment", "0")
        val pushData = ZipPushData()
        pushData.setCallLogs(callInfo)
        pushData.setInstalledApps(installAppInfo)
        pushData.setMessage(smsMessageInfo)
        pushData.setCalendarInfos(calendarInfo)
        pushData.setMediaData()
        val bean = UserInfoUtils.getUserInfo()
//        bean.cardNo = UserInfoUtils.getSelectBank().cardNo.toString()
//        bean.riskLevel = riskLevel.toString()
//        bean.bankName = myBankName
//        bean.cardName = myBankName
//        bean.bankId = myBankId
//        bean.accountName = fullName
////        bean.taxNumber = UserInfoUtils.getShuiNumber()
//        bean.emergentContacts = Gson().fromJson(bean.emergencyContactPerson,
//            object : TypeToken<List<MacawEmergentContactsBean>>() {}.type)
        treeMap.putAll(api)
        api.addParam("pushData", pushData)
        api.addParam("customerInfo", bean)
        api.addParam("sign", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).creationOrderByMx(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipBizBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipBizBean) {
                    realOrderLiveData.postValue(result)
                }
            })

    }
    val realOrderLiveData = MutableLiveData<ZipBizBean?>()

    val preOrderLiveData = MutableLiveData<ZipBizBean?>()

}