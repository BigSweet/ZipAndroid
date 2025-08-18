package com.zip.zipandroid.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.zip.zipandroid.utils.ZipLoadingUtils

abstract class ZipBaseBindingActivity<VM : BaseViewModel, VB : ViewBinding> : ZipBaseModelActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    fun showLoading() {
        ZipLoadingUtils.show(this)
    }


    open fun getContext(): Context {
        return this
    }

    fun dismissLoading() {
        ZipLoadingUtils.dismiss()
    }

    fun getPerList(): ArrayList<String> {
        val list = arrayListOf<String>()
        list.add(Manifest.permission.READ_SMS)
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        return list
    }


    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }

    fun updateToolbarTopMargin(v: View?) {
        if (v == null) {
            return
        }
        val lp: Any? = v.layoutParams
        if (lp != null && lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin = BarUtils.getStatusBarHeight()
        }
    }


    open fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
    }


}