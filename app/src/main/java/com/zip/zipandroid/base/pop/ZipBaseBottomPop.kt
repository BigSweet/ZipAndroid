package com.zip.zipandroid.base.pop

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.lxj.xpopup.core.BottomPopupView
import com.zip.zipandroid.utils.ReflectUtil

open class ZipBaseBottomPop<VB : ViewBinding>(context: Context):BottomPopupView(context) {

    private var _binding: VB? = null

    protected lateinit var mBinding: VB

    override fun addInnerContent() {
        _binding = ReflectUtil.inflateWithGeneric(this, this, 0)
        _binding?.let {
            mBinding = it
            bottomPopupContainer.addView(mBinding.root)
        }
    }
}