package com.zip.zipandroid.fragment

import android.os.Bundle
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.databinding.FragmentZipOrderListBinding
import com.zip.zipandroid.viewmodel.ZipHomeViewModel

class ZipHomeFragment : ZipBaseBindingFragment<ZipHomeViewModel, FragmentZipOrderListBinding>() {
    companion object {
        fun newInstance(): ZipHomeFragment {
            val args = Bundle()
            val fragment = ZipHomeFragment()
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