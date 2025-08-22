package com.zip.zipandroid.pop

import android.content.Context
import com.blankj.utilcode.util.PermissionUtils
import com.zip.zipandroid.base.pop.ZipBaseFullScreenPopupView
import com.zip.zipandroid.databinding.PopZipAllPerBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.AllPerUtils

class ZipAllPerPop(context: Context) : ZipBaseFullScreenPopupView<PopZipAllPerBinding>(context) {


    var allPerSuccess: (() -> Unit)? = null
    var allPerFail: (() -> Unit)? = null
    override fun onCreate() {
        super.onCreate()
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