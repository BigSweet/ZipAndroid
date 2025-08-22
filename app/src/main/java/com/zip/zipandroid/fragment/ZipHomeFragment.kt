package com.zip.zipandroid.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    }



    private fun checkAndRequestReadSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果没有权限，向用户申请
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_SMS),
                REQUEST_CODE_READ_SMS // 自定义请求码（如 100）
            )
        } else {
            // 已经有权限，直接执行读取短信操作
            readSmsMessages()
        }
    }

    private fun readSmsMessages() {

    }

    var REQUEST_CODE_READ_SMS = 999
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意授权，读取短信
                    readSmsMessages()
                } else {
                    // 用户拒绝权限，提示或禁用功能
                    Toast.makeText(requireActivity(), "需要短信权限才能查看短信", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }
}