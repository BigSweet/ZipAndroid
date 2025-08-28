package com.zip.zipandroid.activity

import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityVersionInfoBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipVersionInfoActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityVersionInfoBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Version Info")
        mViewBind.versionInfoNameTv.setText(AppUtils.getAppName())
        mViewBind.versionInfoVersionTv.setText(AppUtils.getAppVersionName())

    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}