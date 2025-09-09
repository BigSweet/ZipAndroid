package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.ZipOrderListBeanItem
import com.zip.zipandroid.databinding.ActivityPayOrderDetailBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.view.toN
import com.zip.zipandroid.viewmodel.OrderItemViewModel

class PayOrderDetailActivity : ZipBaseBindingActivity<OrderItemViewModel, ActivityPayOrderDetailBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, bizId: String?, lid: String, amount: String) {
            val starter = Intent(context, PayOrderDetailActivity::class.java)
                .putExtra("bizId", bizId)
                .putExtra("lid", lid)
                .putExtra("amount", amount)
            context.startActivity(starter)
        }

    }

    var bizId = ""
    var amount = ""
    var lid = ""
    override fun initView(savedInstanceState: Bundle?) {
        bizId = intent.getStringExtra("bizId") ?: ""
        lid = intent.getStringExtra("lid") ?: ""
        amount = intent.getStringExtra("amount") ?: ""
        mViewModel.getOrderListInfo(0)
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Repayment")


        mViewModel.getRepayChannelList(bizId)
        mViewBind.payAccountNumberCopy.setOnDelayClickListener {
            ClipboardUtils.copyText(mViewBind.accountNumberTv.text.toString())
            ToastUtils.showShort("copy success")
        }

        mViewBind.payOrderNameCopy.setOnDelayClickListener {
            ClipboardUtils.copyText(mViewBind.bankNameTv.text.toString())
            ToastUtils.showShort("copy success")
        }
    }

    var orderData: ZipOrderListBeanItem? = null

    override fun createObserver() {
        mViewModel.orderListLiveData.observe(this) {
            orderData = it.find {
                it.bizId == bizId
            }
            if (orderData != null) {
                setDataInfo()
            }
        }
        mViewModel.channelListLiveData.observe(this) {
            if (!it.isNullOrEmpty()) {
                mViewModel.generateOfflineRepaymentCode(bizId, lid, amount, it.first().channelId.toString())
            }
//            mViewBind.bankNameTv.setText(data?.bankName.toString())
//            mViewBind.accountNameTv.setText(data?.bankId.toString())
//            mViewBind.accountNumberTv.setText(data?.custName.toString())
        }

    }

    private fun setDataInfo() {
        val data = orderData
        mViewBind.detailOrderTv.setText(data?.bizId.toString())
        mViewBind.detailRepaidTv.setText(data?.amountDue?.toDouble()?.toN())
        mViewBind.detailInstallTv.setText("Installment" + data?.period.toString() + "/" + data?.stageCount.toString())


    }

    override fun getData() {
    }
}