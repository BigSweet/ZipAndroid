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
import com.zip.zipandroid.bean.OriginUploadContractBean
import com.zip.zipandroid.bean.ProductDidInfo
import com.zip.zipandroid.bean.ProductPidBean
import com.zip.zipandroid.bean.RealUploadUserBean
import com.zip.zipandroid.bean.UploadContractBean
import com.zip.zipandroid.bean.ZipBizBean
import com.zip.zipandroid.bean.ZipIndImgBean
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
    val productLiveData = MutableLiveData<List<ProductDidInfo>>()
    val orderTrialLiveData = MutableLiveData<ZipTriaBean>()

    //    var userOrderLiveData = MutableLiveData<MacawOrderPayBean?>()
    var uploadUserInfoLiveData = MutableLiveData<RealUploadUserBean>()
    fun getPidProduct(riskGrade: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api
            .addParam("idprd", UserInfoUtils.getProductType().pid.toString())
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).findProductDueByPid(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<List<ProductDidInfo>>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: List<ProductDidInfo>) {
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
            .addParam("nauInSamfur", UserInfoUtils.getProductType().productType)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
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
        api
            .addParam("idCustomer", UserInfoUtils.getUserInfo().custId.toString())
            .addParam("idKasuwancin", bizId)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
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


    fun getUploadUserInfo() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getUploadUserInfo(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<RealUploadUserBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: RealUploadUserBean) {
                    uploadUserInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    //设备信息、短信、日历、蓝牙、Advertising ID、WIFI这些都要吗
    fun preOrder(
        callInfo: Array<CallLog?>?, installAppInfo: Array<InstalledApp?>?, smsMessageInfo: Array<SMSMessage?>?, calendarInfo: Array<CalendarInfos?>?,
        info: RealUploadUserBean,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("tasharTushe", "google-play")
        api.addParam("nauInSamfur", UserInfoUtils.getProductType().productType)//productType
        api.addParam("sunanSamfur", UserInfoUtils.getProductType().productName)//productName
        api.addParam("bayaninMakulliNaUku", "") //ambushThirdKeyInfo
        api.addParam("idCustomer", UserInfoUtils.getUserInfo().custId.toString())
        api.addParam("idNaUra", DeviceInfoUtil(ZipApplication.instance).genaralDeviceId)//deviceid
        val pushData = ZipPushData()
        pushData.setCallLogs(callInfo)
        pushData.setInstalledApps(installAppInfo)
        pushData.setMessage(smsMessageInfo)
        pushData.setCalendarInfos(calendarInfo)
        pushData.setMediaData()
        val imgBean = Gson().fromJson<ZipIndImgBean>(UserInfoUtils.getUserInfo().identityImg, ZipIndImgBean::class.java)
        pushData.bayaninHoto.gabanID = imgBean.serverPaths.FRONT
        val bean = UserInfoUtils.getUserInfo()
        info.cardNo = UserInfoUtils.getBankData().cardNo.toString()
        info.accountName = UserInfoUtils.getUserInfo().realname
//        bean.bankName = bankName
        info.bankId = UserInfoUtils.getBankData().bankId.toString()
//        bean.taxNumber = UserInfoUtils.getShuiNumber()
        val list: List<OriginUploadContractBean> = Gson().fromJson(bean.emergencyContactPerson,
            object : TypeToken<List<OriginUploadContractBean>>() {}.type)

        val realConList = convertData(list)
        info.lambobinGaggawa = realConList
        treeMap.putAll(api)
        api.addParam("turaBayanan", pushData)
        api.addParam("bayaninAbokinCiniki", info)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
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

    private fun convertData(datas: List<OriginUploadContractBean>): ArrayList<UploadContractBean> {
        val list = arrayListOf<UploadContractBean>()
        datas.forEachIndexed { index, zipContractBean ->
            if ((zipContractBean.relation ?: -1) > -1) {
                val bean = UploadContractBean(zipContractBean.contactName, zipContractBean.contactPhone, zipContractBean.relation
                    ?: -1, zipContractBean.relationValue)
                list.add(bean)
            }
        }
        return list

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