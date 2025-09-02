package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipSureOrderBinding
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.viewmodel.ZipReviewModel

class ZipSureOrderActivity : ZipBaseBindingActivity<ZipReviewModel, ActivityZipSureOrderBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, amount: String, riskLevel: String) {
            val starter = Intent(context, ZipSureOrderActivity::class.java)
                .putExtra("amount", amount)
                .putExtra("riskLevel", riskLevel)
            context.startActivity(starter)
        }

    }

    var amount = ""
    var riskLevel = ""
    override fun initView(savedInstanceState: Bundle?) {
        mViewModel.orderTrial(amount, riskLevel, UserInfoUtils.getProductDue()?.did)
    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}