package com.zip.zipandroid.pop

import android.content.Context
import android.text.SpannableStringBuilder
import com.blankj.utilcode.util.PermissionUtils
import com.zip.zipandroid.base.pop.ZipBaseCenterPop
import com.zip.zipandroid.databinding.PopZipDefPerBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipDefPerPop(context: Context, val noPerList: List<String>) : ZipBaseCenterPop<PopZipDefPerBinding>(context) {


    override fun onCreate() {
        super.onCreate()

        //去设置界面
        val span = SpannableStringBuilder()
        noPerList.forEach {
            span.append(it)
        }
        mBinding.perTitleTv.setText("Please grant " + span)
        mBinding.perSureTv.setOnDelayClickListener {
            PermissionUtils.launchAppDetailsSettings()
        }

    }
}