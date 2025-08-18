package com.zip.zipandroid.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding


abstract class ZipBaseBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : ZipBaseModelFragment<VM>() {

    override fun layoutId() = 0

    //该类绑定的ViewDataBinding
    private var _binding: DB? = null
    val mViewBind: DB get() = _binding!!


    open fun refreshData(){

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = inflateBindingWithGeneric(inflater,container,false)
        return mViewBind.root
    }

    open fun startActivity(clz: Class<*>?) {
        startActivity(Intent(requireActivity(), clz))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}