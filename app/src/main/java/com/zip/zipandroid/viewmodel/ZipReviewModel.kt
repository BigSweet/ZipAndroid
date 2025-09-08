package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zip.zipandroid.ZipApplication
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.OriginUploadContractBean
import com.zip.zipandroid.bean.RealUploadUserBean
import com.zip.zipandroid.bean.UploadContractBean
import com.zip.zipandroid.bean.ZipBizBean
import com.zip.zipandroid.bean.ZipCouponListBean
import com.zip.zipandroid.bean.ZipHomeDataBean
import com.zip.zipandroid.bean.ZipIndImgBean
import com.zip.zipandroid.bean.ZipOrderAdmissionBean
import com.zip.zipandroid.bean.ZipOrderStatusBean
import com.zip.zipandroid.bean.ZipPushData
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.phonedate.applist.ZipInstalledApp
import com.zip.zipandroid.utils.phonedate.calendar.ZipCalendarInfos
import com.zip.zipandroid.utils.phonedate.calllog.ZipCallLog
import com.zip.zipandroid.utils.phonedate.location.device.ZipDeviceInfoUtil
import com.zip.zipandroid.utils.phonedate.sms.ZipSMSMessage
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class ZipReviewModel : ZipBaseViewModel() {
    //    val productLiveData = MutableLiveData<List<ProductDidInfo>>()
    val orderTrialLiveData = MutableLiveData<ZipTriaBean>()

    var userOrderLiveData = MutableLiveData<ZipOrderStatusBean?>()

    var homeLiveData = MutableLiveData<ZipHomeDataBean>()


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


    fun getUserOrder(bizId: String) {

        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api
            .addParam("idKasuwancin", bizId)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getCreditxStatus(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipOrderStatusBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipOrderStatusBean) {
                    userOrderLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    userOrderLiveData.postValue(null)
                }
            })

    }


    var uploadUserInfoLiveData = MutableLiveData<RealUploadUserBean>()
//    fun getPidProduct() {
//        val treeMap = TreeMap<String, Any?>()
//        val api = FormReq.create()
//        api
//            .addParam("idprd", UserInfoUtils.getProductType().pid.toString())
//        treeMap.putAll(api)
//        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
//        ZipRetrofitHelper.createApi(ZipApi::class.java).findProductDueByPid(api)
//            .compose(RxSchedulers.io_main())
//            .subscribe(object : ZipResponseSubscriber<List<ProductDidInfo>>() {
//                override fun onSubscribe(d: Disposable) {
//                    super.onSubscribe(d)
//                    addReqDisposable(d)
//                }
//
//                override fun onSuccess(result: List<ProductDidInfo>) {
//                    productLiveData.postValue(result)
//                }
//            })
//    }

    fun orderTrial(realAmount: Int, riskGrade: String, did: String, couponId: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        if (!couponId.isNullOrEmpty()) {
            api
                .addParam("idKatinTalla", couponId)
        }
        api
            .addParam("adadinBashi", realAmount)
            .addParam("matakinHadari", riskGrade)
            .addParam("dNaUraid", did)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
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
        callInfo: Array<ZipCallLog?>?, installAppInfo: Array<ZipInstalledApp?>?, zipSmsMessageInfo: Array<ZipSMSMessage?>?, calendarInfo: Array<ZipCalendarInfos?>?,
        info: RealUploadUserBean,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("tasharTushe", "google-play")
        api.addParam("nauInSamfur", UserInfoUtils.getProductType().productType)//productType
        api.addParam("sunanSamfur", UserInfoUtils.getProductType().productName)//productName
        api.addParam("bayaninMakulliNaUku", "") //ambushThirdKeyInfo
        api.addParam("idCustomer", UserInfoUtils.getUserInfo().custId.toString())
        api.addParam("idNaUra", ZipDeviceInfoUtil(ZipApplication.instance).genaralDeviceId)//deviceid
        val pushData = ZipPushData()
        pushData.setCallLogs(callInfo)
        pushData.setInstalledApps(installAppInfo)
        pushData.setMessage(zipSmsMessageInfo)
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
                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
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
        callInfo: Array<ZipCallLog?>?, installAppInfo: Array<ZipInstalledApp?>?, zipSmsMessageInfo: Array<ZipSMSMessage?>?, calendarInfo: Array<ZipCalendarInfos?>?,
        info: RealUploadUserBean,
        realAmount: Int, currentPaidType: String, did: String, couponId: String, applyPeriod:String, preBizId:String, riskGrade:String
    ) {
        //要不要加上一笔订单的id
        //repayment//paytype
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.addParam("tasharTushe", "google-play")
        api.addParam("idKasuwancin", preBizId)
        if (!couponId.isNullOrEmpty()) {
            api.addParam("idKatinTalla", couponId)
        }
        api.addParam("nauInSamfur", UserInfoUtils.getProductType().productType)//productType
        api.addParam("sunanSamfur", UserInfoUtils.getProductType().productName)//productName
        api.addParam("bayaninMakulliNaUku", "") //ambushThirdKeyInfo
        api.addParam("idCustomer", UserInfoUtils.getUserInfo().custId.toString())
        api.addParam("idNaUra", ZipDeviceInfoUtil(ZipApplication.instance).genaralDeviceId)//deviceid
//        api.addParam("adadinBashi", realAmount)
        api.addParam("adadinNema", realAmount)
        api.addParam("dNaUraid", did)
        api.addParam("biyanBashi", currentPaidType)
        api.addParam("lokacinNemadid", did)
        val pushData = ZipPushData()
        pushData.setCallLogs(callInfo)
        pushData.setInstalledApps(installAppInfo)
        pushData.setMessage(zipSmsMessageInfo)
        pushData.setCalendarInfos(calendarInfo)
        pushData.setMediaData()
        val imgBean = Gson().fromJson<ZipIndImgBean>(UserInfoUtils.getUserInfo().identityImg, ZipIndImgBean::class.java)
        pushData.bayaninHoto.gabanID = imgBean.serverPaths.FRONT
        val bean = UserInfoUtils.getUserInfo()
        info.cardNo = UserInfoUtils.getBankData().cardNo.toString()
        info.cardName =   UserInfoUtils.getBankData().bankName.toString()
        info.accountName = UserInfoUtils.getUserInfo().realname
        info.matakinHadariNext = riskGrade
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
                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })

    }

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

//    var topCouponLiveData = MutableLiveData<ZipCouponItemBean?>()
//    fun topPriorityCoupon() {
//        val treeMap = TreeMap<String, Any?>()
//        val api = FormReq.create()
//        treeMap.putAll(api)
//        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
//        ZipRetrofitHelper.createApi(ZipApi::class.java).topPriorityCoupon(api)
//            .compose(RxSchedulers.io_main())
//            .subscribe(object : ZipResponseSubscriber<ZipCouponItemBean?>() {
//                override fun onSubscribe(d: Disposable) {
//                    super.onSubscribe(d)
//                    addReqDisposable(d)
//                }
//
//                override fun onSuccess(result: ZipCouponItemBean?) {
//                    topCouponLiveData.postValue(result)
//                }
//
//                override fun onFailure(code: Int, message: String?) {
//                    super.onFailure(code, message)
//                    failLiveData.postValue(message ?: "")
//                }
//            })
//    }


    val realOrderLiveData = MutableLiveData<ZipBizBean?>()

    val preOrderLiveData = MutableLiveData<ZipBizBean?>()

}