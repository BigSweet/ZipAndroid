package com.zip.zipandroid.activity

import android.os.Bundle
import com.blankj.utilcode.util.ThreadUtils
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.base.UserInfo
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipSplashBinding


class ZipSlashActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipSplashBinding>() {


    var time = 1000L

    override fun initView(savedInstanceState: Bundle?) {
        ThreadUtils.runOnUiThreadDelayed({
            if (!UserInfo.getInstance().token.isNullOrEmpty()) {
                startActivity(ZipMainActivity::class.java)
            } else {
                startActivity(ZipLoginActivity::class.java)
            }
            finish()
        }, time)
    }

    override fun createObserver() {
    }

    override fun getData() {
    }

}