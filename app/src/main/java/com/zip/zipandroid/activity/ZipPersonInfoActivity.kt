package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipPersonInfoBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipPersonInfoActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipPersonInfoBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPersonInfoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Personal Info")
        mViewBind.firstNameInfoView.showBoard()
    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}