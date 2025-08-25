package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zip.zipandroid.base.RxSchedulers
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.bean.BvnInfoBean
import com.zip.zipandroid.bean.UploadImgBean
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
    var uploadImgLiveData = MutableLiveData<String>()
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
}