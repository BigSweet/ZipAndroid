package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.ZipOrderListBeanItem
import com.zip.zipandroid.databinding.ActivityZipOrderDetailBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.view.formatTimestampToDate
import com.zip.zipandroid.view.toN
import com.zip.zipandroid.viewmodel.OrderItemViewModel

class ZipOrderDetailActivity : ZipBaseBindingActivity<OrderItemViewModel, ActivityZipOrderDetailBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, bizId: String?, queryType: Int) {
            val starter = Intent(context, ZipOrderDetailActivity::class.java)
                .putExtra("bizId", bizId)
                .putExtra("queryType", queryType)
            context.startActivity(starter)
        }

    }

    var bizId: String = ""
    var queryType = 0
    override fun initView(savedInstanceState: Bundle?) {
        bizId = intent.getStringExtra("bizId") ?: ""
        queryType = intent.getIntExtra("queryType", 0)
        mViewModel.getOrderListInfo(queryType)
        showLoading()
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Order Details")


    }

    var orderData: ZipOrderListBeanItem? = null
    override fun createObserver() {
        mViewModel.orderListLiveData.observe(this) {
            dismissLoading()
            orderData = it.find {
                it.bizId == bizId
            }
            if (orderData != null) {
                setDataInfo()
            }
        }
    }

    private fun setDataInfo() {
        val status = orderData?.status ?: ""

        mViewBind.detailTopOverTv.hide()
        mViewBind.penInterUpdateTv.hide()
        mViewBind.delayInterCl.hide()
        mViewBind.amountUpdateTv.visible = status == "PARTIAL"
        orderData?.let {
            mViewBind.detailRepaidTv.setText(it?.amountDue?.toDouble()?.toN())
            mViewBind.detailInstallTv.setText("Installment" + it.period.toString() + "/" + it.stageCount.toString())
            mViewBind.detailInterTv.setText(it.interest.toDouble().toN())
//            var allFee = 0
//            it.fees?.forEach {
//                allFee = allFee + it
//            }
            mViewBind.detailManagerTv.setText(it.hairCutAmount.toN())
//            mViewBind.detailInterReduceTv.setText(it.subtractInterest.toDouble().toN())


            if (!it.applyAmount.isNullOrEmpty()) {
                mViewBind.detailTotalAmountTv.setText(it.applyAmount?.toDouble()?.toN())
            }
            mViewBind.detailTotalTermsTv.setText(it.stageCount.toString())
            mViewBind.detailOrderNoTv.setText(it.bizId)
            mViewBind.detailApplicationTimeTv.setText(it.applyTime.formatTimestampToDate())
            mViewBind.detailLoadDisTimeTv.setText(it.releaseTime.formatTimestampToDate())
            mViewBind.detailReceAccountTv.setText(it.cardNo)


//            mViewBind.detailPenInterTv.hide()
//            mViewBind.detailPenInterReduceTv.hide()

//            if (status == "OVERDUE") {
            mViewBind.detailPenInterTv.setText(it.penalty.toDouble().toN())
//            mViewBind.detailPenInterReduceTv.setText(it.subtractFine.toDouble().toN())
//            mViewBind.detailPenInterTv.show()
//            mViewBind.detailPenInterReduceTv.show()
//            }

        }
        mViewBind.detailSettleNowTv.hide()
        mViewBind.detailSettleNowTv.setText("Settle Now")
        mViewBind.detailSettleNowTv.setOnDelayClickListener {
            PayOrderDetailActivity.start(this, bizId, orderData?.lid.toString(), orderData?.amountDue.toString())
        }

        if (orderData?.period ?: 0 < orderData?.count ?: 0) {
            //如果有下一期 没下一期就隐藏
            mViewBind.nextInstallLl.show()
            val nextData = orderData?.repaymentResponseList?.get((orderData?.period ?: 0))

            mViewBind.detailDueTimeTv.setText(nextData?.periodTime?.formatTimestampToDate())
            mViewBind.detailOutAmountTv.setText(nextData?.amountDue?.toDouble()?.toN())
            mViewBind.detailOutInstallTv.setText(nextData?.period.toString() + "/" + orderData?.count.toString())
//            mViewBind.detailDueTimeTv.setText()
        } else {
            mViewBind.nextInstallLl.hide()
        }
        if (status == "OVERDUE") {
            mViewBind.penInterUpdateTv.show()
            mViewBind.delayInterCl.show()
            mViewBind.detailSettleNowTv.show()
            mViewBind.detailSettleNowTv.setBackground(Color.parseColor("#FFFF4343"))
            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FFFF4343"))
            //逾期
            mViewBind.detailTopOverTv.setText("You are ${orderData?.overdueDays.toString()} days overdue. Please repay as soon as possible.")
            mViewBind.detailTopOverTv.show()

        }

        if (status == "NOTREPAID" || status == "PARTIAL" || status == "LENDING" || status == "PASSED") {
            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FF3667F0"))
            mViewBind.detailSettleNowTv.show()
            mViewBind.detailSettleNowTv.setText("Repay Now")
            mViewBind.detailSettleNowTv.setBackground(Color.parseColor("#FF3667F0"))

        }

        mViewBind.orderCompleteCl.hide()

        if (status == "FINISH" || status == "OVERDUEREPAYMENT") {
            mViewBind.orderCompleteCl.show()

            mViewBind.detailRepaidTv.setText("Repaid")
//            mViewBind.detailCompleteTimeTv.setText()
            orderData?.let {
                mViewBind.detailPenInterTv.setText(it.factFine.toDouble().toN())
                mViewBind.detailInterTv.setText(it.factInterest.toDouble().toN())
            }

            mViewBind.detailRepaidTv.setTextColor(Color.parseColor("#FF9F9F9F"))
        }
    }

    override fun getData() {
    }
}