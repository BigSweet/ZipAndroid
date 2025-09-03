package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.databinding.ActivityZipOrderReviewBinding
import com.zip.zipandroid.viewmodel.ZipReviewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class ZipOrderReviewActivity : ZipBaseBindingActivity<ZipReviewModel, ActivityZipOrderReviewBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, bizId: String) {
            val starter = Intent(context, ZipOrderReviewActivity::class.java)
                .putExtra("bizId", bizId)
            context.startActivity(starter)
        }

    }


    var myBankName = ""
    var myBankId = ""
    var fullName = ""

    private fun getMyBindCard() {
//        val selectBank = UserInfoUtils.getSelectBank()
//        myBankName = selectBank.bankName
//        fullName = UserInfoUtils.getRealName()
//        myBankId = selectBank.bankId.toString()
//        getUserData()
    }

    var preBizId: String = ""

    fun getUserData() {
//        mViewModel.getUserData()
//        mViewModel.userDataLiveData.observe(this) {
//            it ?: return@observe
//            UserInfoUtils.saveUserInfo(Gson().toJson(it).toString())
//            mbEmail = it.mbEmail.toString()
//            preOrder()
//        }
    }

    var mbEmail = ""
    override fun initView(savedInstanceState: Bundle?) {
//        showLoading()
        currentBizId = intent.getStringExtra("bizId") ?: ""
        //直接轮训
//        interValRange(bizId)
//        mViewModel.or
        orderAdmission()
    }

    override fun createObserver() {
        mViewModel.preOrderLiveData.observe(this) {
            it ?: return@observe
            preBizId = it.bizId ?: ""
            getRiskLevel(it.bizId ?: "")
        }
        mViewModel.admissionLiveData.observe(this) {
            if (it.admission) {
                preOrder()
            }
        }

        mViewModel.productLiveData.observe(this) {
            if (!it.productDueList.isNullOrEmpty()) {
                did = (it.productDueList?.first()?.did ?: 0L).toString()
                ZipSureOrderActivity.start(this, amount, levelBean?.riskLevel
                    ?: "")
            }
        }

        mViewModel.realOrderLiveData.observe(this) {
            currentBizId = it?.bizId ?: ""
            interValRange(it?.bizId ?: "")
        }

        mViewModel.riskLevelLiveData.observe(this) {
            levelBean = it
            amount = it.grantAmount.toString()
            getPidProduct(it.riskLevel ?: "")
        }
//        mViewModel.userOrderLiveData.observe(this) {
//            it ?: return@observe
//            if (it.status == "WAITING") {
//                EventBusUtils.post(RefreshHomeEvent())
//                dismissLoading()
//                disposable?.dispose()
//                MacawUserSureOrderActivityMacawMacaw.start(this@ZipOrderReviewActivity, currentBizId)
//                finish()
//            }
//            if (it.status == "EXECUTING") {
//                EventBusUtils.post(RefreshHomeEvent())
//                dismissLoading()
//                disposable?.dispose()
//                finish()
//            }
//        }
    }

    fun preOrder() {
        mViewModel.preOrder(callInfo, installAppInfo, smsMessageInfo, calendarInfo)

    }

    override fun getData() {


    }

    var amount: String = ""
    var did: String = ""
    var levelBean: ZipRiskLevelBean? = null
    fun getRiskLevel(bizId: String) {
        mViewModel.getRiskLevel(bizId)

    }

    fun getPidProduct(riskGrade: String) {
        mViewModel.getPidProduct(riskGrade)

    }


    private fun orderAdmission() {
        mViewModel.admission()

    }

    fun realOrder() {
        mViewModel.realOrder(amount, did, myBankName, myBankId, fullName, levelBean?.riskLevel.toString(), preBizId, callInfo, installAppInfo, smsMessageInfo, calendarInfo)

    }

    var currentBizId = ""

    var disposable: Disposable? = null

    fun interValRange(bizId: String) {
        disposable = Observable.intervalRange(1, 20, 500, 500, TimeUnit.MILLISECONDS) // 让被观察者执行在 IO 线程
            .subscribeOn(Schedulers.io()) // 让观察者执行在主线程
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<Long> {
                override fun accept(aLong: Long?) {
                    if (aLong == 20L) {
                        dismissLoading()
                    }
                    getOrderData(bizId = bizId)
                }
            })

    }

    fun getOrderData(bizId: String) {
//        mViewModel.getUserOrder(bizId)
    }

}