package com.zip.zipandroid.fragment

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zip.zipandroid.activity.ZipBandCardActivity
import com.zip.zipandroid.activity.ZipContractActivity
import com.zip.zipandroid.activity.ZipPersonInfoActivity
import com.zip.zipandroid.activity.ZipQuestionActivity
import com.zip.zipandroid.activity.ZipWebActivity
import com.zip.zipandroid.activity.ZipWorkInfoActivity
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.databinding.FragmentZipHomeBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.viewmodel.ZipHomeViewModel

class ZipHomeFragment : ZipBaseBindingFragment<ZipHomeViewModel, FragmentZipHomeBinding>() {
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
            //查到了第几部，在去进件
            mViewModel.getUserInfo()

//            startActivity(ZipPersonInfoActivity::class.java)
//            val list = AllPerUtils.getAllPer()
////                val list = getTestPerList()
//
//            PermissionUtils.permission(*list.toTypedArray())
//                .callback(object : PermissionUtils.FullCallback {
//                    override fun onGranted(permissionsGranted: List<String>) {
//                        //拿数据吗
//                    }
//
//                    override fun onDenied(
//                        permissionsDeniedForever: List<String>,
//                        permissionsDenied: List<String>,
//                    ) {
//
//                    }
//                })
//                .request()
        }
        mViewBind.zipHomePrivateSl.setOnDelayClickListener {
            ZipWebActivity.start(requireActivity(), Constants.commonPrivateUrl)
        }
        mViewModel.zipHomeData()
        mViewModel.getZipAppConfig()
    }


    private fun readSmsMessages() {

    }


    override fun createObserver() {
        mViewModel.homeLiveData.observe(this) {
            UserInfoUtils.setProductType(Gson().toJson(it.productList))
            UserInfoUtils.saveProductDue(Gson().toJson(it.productDidInfo))
            mViewBind.zipHomeMoneyTv.setText(it.productList.limitMax)
        }
        mViewModel.configLiveData.observe(this) {

            if (!it?.APP_QA_ADV.isNullOrEmpty()) {
                //获取广告信息
                mViewModel.getAdInfo(it?.APP_QA_ADV)
            }

        }
        mViewModel.userInfoLiveData.observe(this) {
            if(it.mbCustId>0){
                UserInfoUtils.saveCusId(it.mbCustId)
            }
            UserInfoUtils.saveUserInfo(Gson().toJson(it).toString())
            if (it.firstName.isNullOrEmpty()) {
                startActivity(ZipPersonInfoActivity::class.java)
            } else if (it.industryName.isNullOrEmpty()) {
                startActivity(ZipWorkInfoActivity::class.java)
            } else if (it.emergencyContactPerson.isNullOrEmpty()) {
                startActivity(ZipContractActivity::class.java)
            } else if (it.questions.isNullOrEmpty()) {
                startActivity(ZipQuestionActivity::class.java)
            } else if (it.bankId.isNullOrEmpty()) {
                mViewModel.zipQueryCard()

            }

        }
        mViewModel.cardListLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                startActivity(ZipBandCardActivity::class.java)
            } else {
                //去额度计算页面
            }
        }

        mViewModel.zipAdLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                //只显示下面的
                mViewBind.noAdShowSl.show()
                mViewBind.zipHomeZipSl.hide()
            } else {
                mViewBind.noAdShowSl.hide()
                mViewBind.zipHomeZipSl.show()
                mViewBind.zipHomeZipSl.setOnDelayClickListener {
                    ToastUtils.showShort("Preparations for the event are underway. Please look forward to it!")
                }
                mViewBind.homeFirstAdTv.setText(it.first().advertContent)
                Glide.with(requireActivity()).load(it.first().imgUrl).into(mViewBind.homeFirstAdIv)
            }
        }
    }

    override fun lazyLoadData() {
    }
}