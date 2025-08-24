package com.zip.zipandroid.fragment

import android.os.Bundle
import com.blankj.utilcode.util.PermissionUtils
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.databinding.FragmentZipOrderListBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.AllPerUtils
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

        mViewBind.zipHomeVerTv.setOnDelayClickListener {
            val list = AllPerUtils.getAllPer()
//                val list = getTestPerList()

            PermissionUtils.permission(*list.toTypedArray())
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(permissionsGranted: List<String>) {
                        //拿数据吗
                    }

                    override fun onDenied(
                        permissionsDeniedForever: List<String>,
                        permissionsDenied: List<String>,
                    ) {

                    }
                })
                .request()
        }
        mViewModel.zipHomeData()
        mViewModel.getZipAppConfig()
    }


    private fun readSmsMessages() {

    }


    override fun createObserver() {
        mViewModel.configLiveData.observe(this) {

            if (!it?.APP_QA_ADV.isNullOrEmpty()) {
                //获取广告信息
                mViewModel.getAdInfo(it?.APP_QA_ADV)
            }

        }
        mViewModel.zipAdLiveData.observe(this) {

        }
    }

    override fun lazyLoadData() {
    }
}