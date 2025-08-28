package com.zip.zipandroid.base.pop

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.CenterPopupView
import com.zip.zipandroid.utils.ReflectUtil

open class ZipBaseCenterPop<VB : ViewBinding>(context: Context) : CenterPopupView(context) {


    private var _binding: VB? = null

    override fun getPopupWidth(): Int {
        return ScreenUtils.getScreenWidth()
    }

    override fun getMaxWidth(): Int {
        return ScreenUtils.getScreenWidth()
    }

    protected lateinit var mBinding: VB

    override fun addInnerContent() {
        _binding = ReflectUtil.inflateWithGeneric(this, this, 0)
        _binding?.let {
            mBinding = it
            centerPopupContainer.addView(mBinding.root)
        }
    }

    var sureClick :(()->Unit)?=null

//    override fun getMaxWidth(): Int {
//        return ScreenUtils.getScreenWidth()
//    }
}