package com.zip.zipandroid.activity

import android.os.Bundle
import com.blankj.utilcode.util.ThreadUtils
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipSplashBinding
import com.zip.zipandroid.utils.UserInfoUtils


class ZipSplashActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipSplashBinding>() {


    var time = 1000L

    override fun initView(savedInstanceState: Bundle?) {
        ThreadUtils.runOnUiThreadDelayed({
            if (!UserInfoUtils.getSignKey().isNullOrEmpty()) {
//                startActivity(ZipMainActivity::class.java)
                    startActivity(ZipQuestionActivity::class.java)

            } else {
                if (MMKV.defaultMMKV()?.decodeString("app_per").isNullOrEmpty()) {
                    //没接受隐私的
                    ZipPerActivity.start(this)
                } else {
                    startActivity(ZipLoginActivity::class.java)

                }

            }
            finish()
        }, time)
    }


    override fun createObserver() {
    }

    override fun getData() {
    }

}