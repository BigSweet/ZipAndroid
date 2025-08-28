package com.zip.zipandroid.pop

import android.content.Context
import com.zip.zipandroid.base.pop.ZipBaseFullScreenPopupView
import com.zip.zipandroid.databinding.PopZipAllPerBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.visible

class ZipAllPerPop(context: Context, val fromAbout: Boolean? = false) : ZipBaseFullScreenPopupView<PopZipAllPerBinding>(context) {


    var allPerSuccess: (() -> Unit)? = null
    var allPerFail: (() -> Unit)? = null
    override fun onCreate() {
        super.onCreate()

        mBinding.fromAboutTv.visible = fromAbout == true
        mBinding.privateSureTv.visible = fromAbout == false
        mBinding.privateCancelTv.visible = fromAbout == false
        mBinding.fromAboutTv.setOnDelayClickListener {
            dismiss()
        }

        mBinding.privateSureTv.setOnDelayClickListener {
            allPerSuccess?.invoke()
            dismiss()

        }
        mBinding.privateCancelTv.setOnDelayClickListener {
            allPerFail?.invoke()
            dismiss()
        }
    }

}