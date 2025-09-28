package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zip.zipandroid.R
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipOrderNextBinding
import com.zip.zipandroid.event.ZipRefreshHomeEvent
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.viewmodel.ZipReviewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ZipOrderNextActivity : ZipBaseBindingActivity<ZipReviewModel, ActivityZipOrderNextBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context, bizId: String) {
            val starter = Intent(context, ZipOrderNextActivity::class.java)
                .putExtra("bizId", bizId)
            context.startActivity(starter)
        }

    }

    var bizId = ""
    override fun initView(savedInstanceState: Bundle?) {
        bizId = intent.getStringExtra("bizId") ?: ""
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Loan Approval")

        interValRange(bizId)
        mViewBind.nextReturnHomeTv.setOnDelayClickListener {
            finish()
        }
        ZipEventBusUtils.post(ZipRefreshHomeEvent())


    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    var disposable: Disposable? = null

    fun interValRange(bizId: String) {
        disposable = Observable.intervalRange(1, 10, 500, 3000, TimeUnit.MILLISECONDS) // 让被观察者执行在 IO 线程
            .subscribeOn(Schedulers.io()) // 让观察者执行在主线程
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<Long> {
                override fun accept(aLong: Long?) {
                    getOrderData(bizId = bizId)
                    if (aLong == 6L) {
//                        dismissLoading()
                        disposable?.dispose()
                    }
                }
            })

    }


    fun getOrderData(bizId: String) {
        showLoading()
        mViewModel.getUserOrder(bizId)
    }

    override fun createObserver() {
        mViewModel.userOrderLiveData.observe(this) {
            dismissLoading()
//

//            订单状态（300 等待 同WAITING 挂起状态，跳转到订单确认页面，等待用户确认，用户确认或取消后跳转到首页。
//            1 执行中 审核中页面
//                    372 已通过 同PASSED
//            425 已拒绝 同REFUSED,审核失败页面
//            421 已取消 同CANCELED/CANCEL）


            mViewBind.orderNextStatusIv.setImageResource(R.drawable.zip_order_next_success_icon)

            if (it?.creditxStatus == "372") {
                mViewBind.orderNextStatusIv.setImageResource(R.drawable.zip_order_next_success_icon)
                mViewBind.orderNextStatusTv.setText("Your Application Has Been Approved")
                mViewBind.orderNextStatusDesTv.setText(" We’re pleased to inform you that your application \\n has been approved. Your loan will be disbursed \\n shortly")
                ZipEventBusUtils.post(ZipRefreshHomeEvent())
                dismissLoading()
                disposable?.dispose()
            }
            if (it?.creditxStatus == "425") {
                mViewBind.orderNextStatusIv.setImageResource(R.drawable.zip_order_next_fail_icon)
                mViewBind.orderNextStatusTv.setText("Your Application Has Been Rejected")
                mViewBind.orderNextStatusDesTv.setText("We're sorry to inform you that your application\\n\n" +
                        "            has not been approved . You may reapply after one month.\\ n\n" +
                        "                    Thank you for your continued support.")
                ZipEventBusUtils.post(ZipRefreshHomeEvent())
                dismissLoading()
                disposable?.dispose()
            }
            if (it?.creditxStatus == "1") {
                mViewBind.orderNextStatusIv.setImageResource(R.drawable.zip_order_next_progress_icon)
                mViewBind.orderNextStatusTv.setText("Review in Progress")
                mViewBind.orderNextStatusDesTv.setText("You'll receive the review outcome in a few minutes.")


            }
        }
    }

    override fun getData() {
    }
}