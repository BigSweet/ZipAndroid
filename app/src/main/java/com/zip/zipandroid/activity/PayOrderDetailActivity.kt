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
        fun start(context: Context, data: ZipOrderListBeanItem?) {
            val starter = Intent(context, PayOrderDetailActivity::class.java)
                .putExtra("data", data)
            context.startActivity(starter)
        }

    }

    var data: ZipOrderListBeanItem? = null
    override fun initView(savedInstanceState: Bundle?) {
        data = intent.getParcelableExtra<ZipOrderListBeanItem>("data")
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Repayment")

        mViewBind.detailOrderTv.setText(data?.bizId.toString())
        mViewBind.detailRepaidTv.setText(data?.amountDue?.toInt()?.toN())

        mViewBind.payAccountNumberCopy.setOnDelayClickListener {
            ClipboardUtils.copyText(mViewBind.accountNumberTv.text.toString())
            ToastUtils.showShort("copy success")
        }
        mViewBind.detailInstallTv.setText("Installment" + data?.period.toString() + "/" + data?.stageCount.toString())
        mViewBind.bankNameTv.setText(data?.bankName.toString())
        mViewBind.accountNameTv.setText(data?.bankId.toString())
        mViewBind.accountNumberTv.setText(data?.custName.toString())
        mViewBind.payOrderNameCopy.setOnDelayClickListener {
            ClipboardUtils.copyText(mViewBind.bankNameTv.text.toString())
            ToastUtils.showShort("copy success")
        }
    }


    override fun createObserver() {
    }

    override fun getData() {
    }
}