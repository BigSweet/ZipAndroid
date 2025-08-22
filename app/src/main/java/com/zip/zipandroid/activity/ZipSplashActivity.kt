package com.zip.zipandroid.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.blankj.utilcode.util.ThreadUtils
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.R
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.UserInfo
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipSplashBinding
import java.util.Calendar
import java.util.Date


class ZipSplashActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipSplashBinding>() {


    var time = 1000L

    override fun initView(savedInstanceState: Bundle?) {
        ThreadUtils.runOnUiThreadDelayed({
            if (!UserInfo.getInstance().userId.isNullOrEmpty()) {
                startActivity(ZipMainActivity::class.java)
            } else {
                if (MMKV.defaultMMKV()?.decodeString("app_per").isNullOrEmpty()) {
                    //没接受隐私的
                    ZipPerActivity.start(this)
                } else {
                    startActivity(ZipLoginActivity::class.java)
//                    startActivity(ZipPersonInfoActivity::class.java)

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