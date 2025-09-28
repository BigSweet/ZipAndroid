package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.ZipRiskLevelBean
import com.zip.zipandroid.databinding.ActivityZipOrderReviewBinding
import com.zip.zipandroid.event.ZipRefreshHomeEvent
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipEventBusUtils
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
        showLoading()
        mViewModel.getUserInfo()

    }

    override fun createObserver() {
        mViewModel.userInfoLiveData.observe(this) {
            UserInfoUtils.saveUserInfo(Gson().toJson(it).toString())
            orderAdmission()
        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.preOrderLiveData.observe(this) {
            it ?: return@observe
            preBizId = it.bizId ?: ""
            UserInfoUtils.savePreBizId(preBizId)

            getRiskLevel(it.bizId ?: "")
        }
        mViewModel.admissionLiveData.observe(this) {
            if (it.admission) {
                preOrder()
            } else {
                ToastUtils.showShort("Entry admission failed")
                finish()
            }
        }
        mViewModel.uploadUserInfoLiveData.observe(this) {
//            val gson = GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//                .create()
            val gson = GsonBuilder()
                .setFieldNamingStrategy { field ->
                    val annotation = field.getAnnotation(SerializedName::class.java)
                    annotation?.value ?: field.name
                }
                .create()

            UserInfoUtils.saveUploadUserInfo(gson.toJson(it).toString())
            mViewModel.preOrder(callInfo, installAppInfo, zipSmsMessageInfos, calendarInfo, it)
        }
        mViewModel.riskLevelLiveData.observe(this) {
            levelBean = it
            amount = it.grantAmount.toString()
            UserInfoUtils.saveLevelData(Gson().toJson(levelBean))
            getPidProduct(it.riskLevel ?: "")
        }
    }

    fun preOrder() {
        mViewModel.getUploadUserInfo()


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
//        mViewModel.getPidProduct()
        dismissLoading()
        ZipSureOrderActivity.start(this, amount, levelBean?.riskLevel
            ?: "", levelBean?.productDay?:0, preBizId)
        ZipEventBusUtils.post(ZipRefreshHomeEvent())
        finish()
    }


    private fun orderAdmission() {
        mViewModel.admission()

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