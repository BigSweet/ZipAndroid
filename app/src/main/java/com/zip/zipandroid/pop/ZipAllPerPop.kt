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

            val list = AllPerUtils.getAllPer()
            PermissionUtils.permission(*list.toTypedArray())
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: List<String>) {
                        //拿数据吗
                        allPerSuccess?.invoke()
                        dismiss()
                    }

                    override fun onDenied(
                        permissionsDeniedForever: List<String>,
                        permissionsDenied: List<String>,
                    ) {
                        allPerFail?.invoke()
                        dismiss()
                    }
                })
                .request()

        }
        mBinding.privateCancelTv.setOnDelayClickListener {
            dismiss()
        }
    }

}