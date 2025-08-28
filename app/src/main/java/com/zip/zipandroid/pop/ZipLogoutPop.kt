package com.zip.zipandroid.pop

import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import com.zip.zipandroid.base.pop.ZipBaseCenterPop
import com.zip.zipandroid.databinding.PopZipLogoutBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipLogoutPop(context: Context) : ZipBaseCenterPop<PopZipLogoutBinding>(context) {

    var sureLogoutClick :(()->Unit)?=null


    override fun onCreate() {
        super.onCreate()
        mBinding.logoutCancelTv.setOnDelayClickListener {
            dismiss()
        }
        mBinding.logoutSureTv.setOnDelayClickListener {
            sureLogoutClick?.invoke()
            dismiss()
        }
    }
}