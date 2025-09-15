package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipPrivateBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipPerActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipPrivateBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPerActivity::class.java)
            context.startActivity(starter)
        }

    }


    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Privacy Policy")
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }

        mViewBind.privateSureTv.setOnDelayClickListener {
            MMKV.defaultMMKV()?.putString("app_per", "done")
            //去登录页面
            startActivity(ZipLoginActivity::class.java)
            finish()
        }
        mViewBind.privateCancelTv.setOnDelayClickListener {
            finish()
        }
    }


    override fun createObserver() {
    }

    override fun getData() {
    }
}