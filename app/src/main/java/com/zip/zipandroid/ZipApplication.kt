package com.zip.zipandroid

//import aai.liveness.GuardianLivenessDetectionSDK
//import ai.advance.liveness.lib.Market
import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV

class ZipApplication : Application() {
    companion object {
        var instance: ZipApplication? = null
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this)
//        Logger.addLogAdapter(object : AndroidLogAdapter() {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return DeviceUtils.getAndroidID() == "38c682680c26ddb2"
//            }
//        })
        CrashReport.initCrashReport(getApplicationContext(), "bf7aa0ef33", false);
        initAdvanceLivenessDetection()
    }

    /**
     * 初始化Advance活体sdk
     */
    private fun initAdvanceLivenessDetection() {
//        GuardianLivenessDetectionSDK.init(this, Market.Mexico)
//        GuardianLivenessDetectionSDK.isDetectOcclusion(true)  // 开启遮挡检测
    }


}