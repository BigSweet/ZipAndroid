package com.zip.zipandroid.fragment

import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.FragmentZipOrderListBinding

class ZipMineFragment : ZipBaseBindingFragment<ZipBaseViewModel, FragmentZipOrderListBinding>() {
    companion object{
        fun newInstance(): ZipMineFragment{
            val args = Bundle()
            val fragment = ZipMineFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }
}