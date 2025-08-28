package com.zip.zipandroid.activity

import android.os.Bundle
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipAboutUsBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.ZipRatePop
import com.zip.zipandroid.viewmodel.ZipHomeViewModel

class ZipAboutUsActivity : ZipBaseBindingActivity<ZipHomeViewModel, ActivityZipAboutUsBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("About Us")

        mViewBind.zipAboutRateSl.setOnDelayClickListener {

            val pop = ZipRatePop(this)
            XPopup.Builder(this).asCustom(pop).show()
        }
    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}