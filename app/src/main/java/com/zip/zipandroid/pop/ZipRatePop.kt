package com.zip.zipandroid.pop

import android.content.Context
import com.zip.zipandroid.base.pop.ZipBaseCenterPop
import com.zip.zipandroid.databinding.PopZipRateBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipRatePop(context: Context) : ZipBaseCenterPop<PopZipRateBinding>(context) {


    var currentStar = 0
    override fun onCreate() {
        super.onCreate()
        mBinding.zipGoRateTv.setOnDelayClickListener {
//            ToastUtils.showShort("打开应用市场")
            dismiss()
        }
        mBinding.zipCancelTv.setOnDelayClickListener {
            dismiss()
        }
        mBinding.zipRateScore.setListener {
            currentStar = it
        }
    }
}