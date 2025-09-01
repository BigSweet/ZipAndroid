package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.bean.ZipOrderListBeanItem
import com.zip.zipandroid.databinding.ActivityZipOrderDetailBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.view.formatTimestampToDate
import com.zip.zipandroid.view.toN

class ZipOrderDetailActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipOrderDetailBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, data: ZipOrderListBeanItem?) {
            val starter = Intent(context, ZipOrderDetailActivity::class.java)
                .putExtra("data", data)
            context.startActivity(starter)
        }

    }

    var orderData: ZipOrderListBeanItem? = null
    override fun initView(savedInstanceState: Bundle?) {
        orderData = intent.getParcelableExtra("data")
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Order Details")
        val status = orderData?.status ?: ""

        mViewBind.detailTopOverTv.hide()

        mViewBind.amountUpdateTv.visible = status == "PARTIAL"
        orderData?.let {
            mViewBind.detailRepaidTv.setText(it?.amountDue?.toInt()?.toN())
            mViewBind.detailInstallTv.setText("Installment" + it.period.toString() + "/" + it.stageCount.toString())
            mViewBind.detailInterTv.setText(it.interest.toInt().toN())
            var allFee = 0
            it.fees.forEach {
                allFee = allFee + it
            }
            mViewBind.detailManagerTv.setText(allFee.toInt().toN())
            mViewBind.detailInterReduceTv.setText(it.subtractInterest.toInt().toN())


            mViewBind.detailTotalAmountTv.setText(it.allAmountDue.toInt().toN())
            mViewBind.detailTotalTermsTv.setText(it.stageCount)
            mViewBind.detailOrderNoTv.setText(it.bizId)
            mViewBind.detailApplicationTimeTv.setText(it.applyTime.formatTimestampToDate())
            mViewBind.detailLoadDisTimeTv.setText(it.releaseTime)
            mViewBind.detailReceAccountTv.setText(it.bankId)


            mViewBind.detailPenInterTv.hide()
            mViewBind.detailPenInterReduceTv.hide()

            if (status == "OVERDUE") {
                mViewBind.detailPenInterTv.setText(it.penalty.toInt().toN())
                mViewBind.detailPenInterReduceTv.setText(it.subtractFine.toInt().toN())
                mViewBind.detailPenInterTv.show()
                mViewBind.detailPenInterReduceTv.show()
            }

        }
        mViewBind.detailSettleNowTv.hide()
        mViewBind.detailSettleNowTv.setOnDelayClickListener {

        }

        if(true){
            //如果有下一期 没下一期就隐藏

            orderData?.let {
                it.approveTime
            }
//            mViewBind.detailDueTimeTv.setText()
        }
        if (status == "OVERDUE") {
            mViewBind.detailSettleNowTv.show()
            mViewBind.detailSettleNowTv.setBackground(Color.parseColor("#FFFF4343"))
            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FFFF4343"))
            //逾期
            mViewBind.detailTopOverTv.setText("You are ${orderData?.overdueDays.toString()} days overdue. Please repay as soon as possible.")
            mViewBind.detailTopOverTv.show()

        }

        if (status == "NOTREPAID" || status == "PARTIAL" || status == "LENDING" || status == "PASSED") {
            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FF3667F0"))
            mViewBind.detailSettleNowTv.setBackground(Color.parseColor("#FF3667F0"))

        }

        mViewBind.detailCompleteTimeTv.hide()

        if (status == "FINISH") {
            mViewBind.detailCompleteTimeTv.show()
//            mViewBind.detailCompleteTimeTv.setText()

            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FF9F9F9F"))
        }

    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}