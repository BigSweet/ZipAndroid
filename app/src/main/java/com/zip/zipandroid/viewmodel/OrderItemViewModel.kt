package com.zip.zipandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.zip.zipandroid.base.ZipApi
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.ZipResponseSubscriber
import com.zip.zipandroid.base.ZipRetrofitHelper
import com.zip.zipandroid.base.ZipRxSchedulers
import com.zip.zipandroid.bean.ZipOrderListBean
import com.zip.zipandroid.bean.ZipPayChannelListBean
import com.zip.zipandroid.bean.ZipPayCodeBean
import com.zip.zipandroid.utils.SignUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipFormReq
import io.reactivex.disposables.Disposable
import java.util.TreeMap

class OrderItemViewModel : ZipBaseViewModel() {

    var orderListLiveData = MutableLiveData<ZipOrderListBean>()
    var payCodeLiveData = MutableLiveData<ZipPayCodeBean>()
    var channelListLiveData = MutableLiveData<ZipPayChannelListBean>()

    fun getOrderListInfo(orderStatusGroup: Int) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("rikodinMatsayin", orderStatusGroup)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getAllOrderList(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipOrderListBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipOrderListBean) {
                    orderListLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun generateOfflineRepaymentCode(bizId: String, lid: String, amount: String, channelId: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("idKasuwancin", bizId)
        api.put("LID", lid)
        api.put("adadin", amount)
        api.put("nauInBiya", "1")
        api.put("idTashoshi", channelId)
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).generateOfflineRepaymentCode(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipPayCodeBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipPayCodeBean) {
                    payCodeLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }

    fun getRepayChannelList(bizId: String) {
        val treeMap = TreeMap<String, Any?>()
        val api = ZipFormReq.create()
        api.put("idKasuwancin", bizId)
        api.put("nauInBiya", "1")
        treeMap.putAll(api)
        api.addParam("sanyaHannu", SignUtils.signParameter(treeMap, UserInfoUtils.getSignKey()))
        ZipRetrofitHelper.createApi(ZipApi::class.java).getRepaymentRoute(api)
            .compose(ZipRxSchedulers.io_main())
            .subscribe(object : ZipResponseSubscriber<ZipPayChannelListBean>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addReqDisposable(d)
                }

                override fun onSuccess(result: ZipPayChannelListBean) {
                    channelListLiveData.postValue(result)
                }

                override fun onFailure(code: Int, message: String?) {
                    super.onFailure(code, message)
                    failLiveData.postValue(message ?: "")
                }
            })
    }


}