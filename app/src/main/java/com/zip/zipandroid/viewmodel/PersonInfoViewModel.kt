package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.AddressInfoBean
import com.zip.zipandroid.bean.AddressUploadBean
import com.zip.zipandroid.bean.BvnInfoBean
import com.zip.zipandroid.bean.UploadImgBean
import com.zip.zipandroid.bean.ZipIndImgBean
import com.zip.zipandroid.bean.ZipRealNameBean
import com.zip.zipandroid.utils.FormReq
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import io.reactivex.disposables.Disposable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.TreeMap

class PersonInfoViewModel : ZipBaseViewModel() {


    var bvnInfoLiveData = MutableLiveData<BvnInfoBean>()
    var realNameInfoLiveData = MutableLiveData<ZipRealNameBean>()
    var saveInfoLiveData = MutableLiveData<Any>()
    var uploadImgLiveData = MutableLiveData<String>()
    var servicePathLiveData = MutableLiveData<String>()
    var allAddressInfo = MutableLiveData<List<AddressInfoBean>>()

    fun saveUserInfo(
        age: Int, birthDate: Long, birthDateStr: String, education: String, degree: Int, identity: String, identityImg: ZipIndImgBean,
        mbEmail: String, mbPhone: String, mbStatus: String, nowAddress: String, postalInfo: AddressUploadBean, sex: Int, marry: Int, childrens: Int, language: String,
        custId: Long, firstName: String, midName: String, lastName: String,
    ) {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
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
            .compose(RxSchedulers.io_main())
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
        val api = FormReq.create()
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
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipRealNameBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipRealNameBean) {
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
        val api = FormReq.create()
        api.put("BVN", bvn)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).checkBvnInfo(api)
            .compose(RxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<BvnInfoBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: BvnInfoBean) {
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
        val api = FormReq.create()
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
            .compose(RxSchedulers.io_main())
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
        val api = FormReq.create()
        treeMap.putAll(api)
        api.put("sunananFayiloli", arrayListOf(path))
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getImgPath(api)
            .compose(RxSchedulers.io_main())
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

    fun getAllAddressInfo() {
        val treeMap = TreeMap<String, Any?>()
        val api = FormReq.create()
        api.put("CP", "NG")
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getAllAddressInfo(api)
            .compose(RxSchedulers.io_main())
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
}