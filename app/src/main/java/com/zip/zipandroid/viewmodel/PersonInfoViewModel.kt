package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import com.zip.zipandroid.base.ZipRxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.bean.AddressUploadBean
import com.zip.zipandroid.bean.BvnInfoBean
import com.zip.zipandroid.bean.CreditListBean
import com.zip.zipandroid.bean.UploadContractBean
import com.zip.zipandroid.bean.UploadImgBean
import com.zip.zipandroid.bean.ZipBankNameListBean
import com.zip.zipandroid.bean.ZipIndImgBean
import com.zip.zipandroid.bean.ZipRealNameBean
import com.zip.zipandroid.bean.ZipUploadQuestionBean
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.ZipFormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.TreeMap

class PersonInfoViewModel : ZipBaseViewModel() {


    var bvnInfoLiveData = MutableLiveData<BvnInfoBean?>()
    var realNameInfoLiveData = MutableLiveData<ZipRealNameBean>()
    var saveInfoLiveData = MutableLiveData<Any>()
    var saveWorkNomralLiveData = MutableLiveData<Any>()
    var uploadImgLiveData = MutableLiveData<String>()
    var bankListLiveData = MutableLiveData<ZipBankNameListBean>()
    var servicePathLiveData = MutableLiveData<String>()
    var allAddressInfo = MutableLiveData<List<AddressInfoBean>>()
    var creditLiveData = MutableLiveData<CreditListBean>()

    fun saveUserInfo(
        age: Int, birthDate: Long, birthDateStr: String, education: String, degree: Int, identity: String, identityImg: ZipIndImgBean,
        mbEmail: String, mbPhone: String, mbStatus: String, nowAddress: String, postalInfo: AddressUploadBean, sex: Int, marry: Int, childrens: Int, language: String,
        custId: Long, firstName: String, midName: String, lastName: String,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("shekarun", age)
        api.put("idCustomer", custId)
        api.put("sunanFarko", firstName)
        api.put("sunanKarshe", lastName)
        val realName = lastName + " " + midName + " " + firstName
        api.put("sunanGaskiya", realName)
        api.put("kwananHaihuwa", birthDate)
        api.put("kwananHaihuwaSiga", birthDateStr)
        api.put("ilimi", education)
        api.put("digiri", degree)
        api.put("ainihin", identity)
        val json = Gson().toJson(identityImg)
        api.put("hotonAinihin", json)
        api.put("imelMB", mbEmail)
        api.put("wayarMB", mbPhone)
        api.put("matsayinMB", mbStatus)
        val realpostalInfo = Gson().toJson(postalInfo)
        api.put("adireshinYanzu", nowAddress)//这个是nowAddress
        api.put("bayaninPosta", realpostalInfo)
        api.put("jimaI", sex)
        api.put("aure", marry)
        api.put("yara", childrens)
        api.put("imelTabbaci", "1")
        api.put("yankin", "NG")
//        api.put("harshe", language)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }


    fun realName(identity: String, birthDate: Long, birthDateStr: String, firstName: String, midName: String, lastName: String, sex: Int) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("ainihin", identity)
        api.put("kwananHaihuwa", birthDate)
        api.put("kwananHaihuwaSiga", birthDateStr)
        api.put("sunanFarko", firstName)
        api.put("sunanTsakiyanext", midName)
        api.put("sunanKarshe", lastName)
        val realName = lastName + " " + midName + " " + firstName
        api.put("sunanGaskiya", realName)
        api.put("jimaI", sex)
        treeMap.putAll(api)

        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).realName(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipRealNameBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipRealNameBean) {
                    UserInfoUtils.saveCusId(result.custId)
                    realNameInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun checkBvn(bvn: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("BVN", bvn)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).checkBvnInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<BvnInfoBean?>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: BvnInfoBean?) {
                    bvnInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getPhotoUrlByBase(base64Image: String) {
//        val pureBase64 = base64Image.substringAfter("base64,")
        val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        var requestBody1: MultipartBody.Builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        api.forEach {
            requestBody1.addFormDataPart(it.key, it.value.toString())
        }

        val imgbody = RequestBody.create("image/*".toMediaType(), imageBytes)
        val body = requestBody1.addFormDataPart("file", "image_${System.currentTimeMillis()}.jpg", imgbody)
            .addFormDataPart("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
            .build()
        ZipRetrofitHelper.createApi(ZipApi::class.java).uploadImg(body)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<UploadImgBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: UploadImgBean) {
                    servicePathLiveData.postValue(result.serverPath)
                    getImgPath(result.serverPath)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getImgPath(path: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.put("sunananFayiloli", arrayListOf(path))
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getImgPath(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<HashMap<Any, Any>>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: HashMap<Any, Any>) {
                    if (result.get(path) != null) {
                        //真正的图像
                        uploadImgLiveData.postValue(result.get(path).toString())

                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getBankList() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.addParam("nauInTsarawa",3)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getBankList(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipBankNameListBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipBankNameListBean) {
                    bankListLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getCreditHistoryDict() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        ZipRetrofitHelper.createApi(ZipApi::class.java).getCreditHistoryDict(AppUtils.getAppPackageName(),
            AppUtils.getAppVersionName(),
            "ANDROID",
            UserInfoUtils.getMid().toString(),
            UserInfoUtils.getUserNo(),
            Constants.client_id,
            SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey())
        )
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<CreditListBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: CreditListBean) {
                    creditLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getAllAddressInfo() {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("CP", "NG")
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getAllAddressInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<List<AddressInfoBean>>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: List<AddressInfoBean>) {
                    allAddressInfo.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    //companyDistrict 公司或者学校
    fun saveCompanyInfo(industry: Int, industryName: String, employmentStatus: Int, companyName: String, companyLocation: AddressUploadBean, companyDistrict: String, payDay: String, income: String, timeWorkBegins: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("masanaAntu", industry)//职业下标
        api.put("sunanMasanaAntu", industryName)//职业名称
        api.put("matsayinAiki", employmentStatus)//就业状态
        api.put("sunanKamfani", companyName)//公司名字
        val companyJson = Gson().toJson(companyLocation)
        api.put("wurinKamfani", companyJson)//公司地址
        api.put("gundumarKamfani", companyDistrict)//公司详细地址
        api.put("kwananBiya", payDay)//发薪日
        api.put("kudinShiga", income)//收入
        api.put("lokacinFarawaAiki", timeWorkBegins)//开始工作时间
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveWorkNomralLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    //companyDistrict 公司或者学校
    fun saveStudentInfo(industry: Int, industryName: String, employmentStatus: Int, schoolName: String, schoolAddress: AddressUploadBean, companyDistrict: String, income: String, timeWorkBegins: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("masanaAntu", industry)//职业下标
        api.put("sunanMasanaAntu", industryName)//职业名称
        api.put("matsayinAiki", employmentStatus)//就业状态
        api.put("sunanKamfani", schoolName)//学校名字
        val realSchoolAddress = Gson().toJson(schoolAddress)
        api.put("wurinKamfani", realSchoolAddress)//学校地址
        api.put("gundumarKamfani", companyDistrict)//学校详细地址
        api.put("kudinShiga", income)//收入
        api.put("lokacinFarawaAiki", timeWorkBegins)//学校工作时间
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveWorkNomralLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun saveWorkUmeInfo(industry: Int, industryName: String, employmentStatus: Int, ontherIncome: Int, lengthOfUmView: String, income: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("masanaAntu", industry)//职业下标
        api.put("sunanMasanaAntu", industryName)//职业名称
        api.put("matsayinAiki", employmentStatus)//就业状态
        api.put("sauranKudinShiga", ontherIncome)//其他收入
        api.put("tsawonRashinAiki", lengthOfUmView)//失业时长
        api.put("kudinShiga", income)//收入
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveWorkNomralLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }


    fun saveContractInfo(list: ArrayList<UploadContractBean>) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        val contractJson = Gson().toJson(list)
        api.put("mutuminGaggawa", contractJson)
        treeMap.putAll(api)
        api.put("lambobinGaggawa", list)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun saveQuestionList(list: List<ZipUploadQuestionBean>) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        treeMap.putAll(api)
        api.put("tambayoyi", list)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).saveUserInfo(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<Any>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: Any) {
                    saveInfoLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }
}