package com.zip.zipandroid.activity

import android.os.Bundle
import com.blankj.utilcode.util.PermissionUtils
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipAboutUsBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.ZipAllPerPop
import com.zip.zipandroid.pop.ZipRatePop
import com.zip.zipandroid.utils.AllPerUtils
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.viewmodel.ZipHomeViewModel

class ZipAboutUsActivity : ZipBaseBindingActivity<ZipHomeViewModel, ActivityZipAboutUsBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("About Us")

        mViewBind.zipAboutCustomSl.setOnDelayClickListener {
            startActivity(ZipCustomServiceActivity::class.java)
        }
        mViewBind.zipAboutPrivateSl.setOnDelayClickListener {
            ZipWebActivity.start(this, Constants.commonPrivateUrl)
        }
        mViewBind.zipAboutUserTeamSl.setOnDelayClickListener {
            ZipWebActivity.start(this, Constants.commonServiceUrl)
        }
        mViewBind.zipAboutVersionSl.setOnDelayClickListener {
            startActivity(ZipVersionInfoActivity::class.java)
        }
        mViewBind.zipAboutRateSl.setOnDelayClickListener {

            val pop = ZipRatePop(this)
            XPopup.Builder(this).asCustom(pop).show()
        }

        mViewBind.zipAboutAccessPerSl.setOnDelayClickListener {
            val pop = ZipAllPerPop(getContext(),true)
            pop.allPerFail = {
            }
            pop.allPerSuccess = {
                val list = AllPerUtils.getAllPer()
                PermissionUtils.permission(*list.toTypedArray())
                    .callback(object : PermissionUtils.FullCallback {
                        override fun onGranted(permissionsGranted: List<String>) {
                        }

                        override fun onDenied(
                            permissionsDeniedForever: List<String>,
                            permissionsDenied: List<String>,
                        ) {

                        }
                    })
                    .request()
            }
            XPopup.Builder(getContext()).asCustom(pop).show()
        }
    }

    override fun createObserver() {
    }

    override fun getData() {
    }
}