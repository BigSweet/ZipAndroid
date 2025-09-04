package com.zip.zipandroid.fragment

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zip.zipandroid.activity.ZipBandCardActivity
import com.zip.zipandroid.activity.ZipContractActivity
import com.zip.zipandroid.activity.ZipOrderReviewActivity
import com.zip.zipandroid.activity.ZipPersonInfoActivity
import com.zip.zipandroid.activity.ZipQuestionActivity
import com.zip.zipandroid.activity.ZipWebActivity
import com.zip.zipandroid.activity.ZipWorkInfoActivity
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.databinding.FragmentZipHomeBinding
import com.zip.zipandroid.event.ZipRefreshHomeEvent
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.viewmodel.ZipHomeViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        }
        mViewBind.zipHomePrivateSl.setOnDelayClickListener {
            ZipWebActivity.start(requireActivity(), Constants.commonPrivateUrl)
        }
        mViewModel.zipHomeData()
        mViewModel.getZipAppConfig()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipRefreshHomeEvent) {
        mViewModel.zipHomeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusUtils.unregister(this)
    }


    override fun createObserver() {
        EventBusUtils.register(this)
        mViewModel.homeLiveData.observe(this) {
            UserInfoUtils.setProductType(Gson().toJson(it.productList))
            UserInfoUtils.saveProductDue(Gson().toJson(it.productDidInfo))
            mViewBind.zipHomeMoneyTv.setText(it.productList.limitMax)
        }
        mViewModel.configLiveData.observe(this) {

            Constants.commonServiceUrl = it?.APP_REGISTER_AGREEMENT ?: "www.baidu.com"//注册协议
            Constants.commonPrivateUrl = it?.APP_PRIVACY_AGREEMENT ?: "www.baidu.com"//隐私协议
            Constants.APP_LOAN_CONTRACT = it?.APP_LOAN_CONTRACT ?: "www.baidu.com"//隐私协议
            Constants.APP_REPAYMENT_AGREEMENT = it?.APP_REPAYMENT_AGREEMENT ?: "www.baidu.com"//隐私协议

            if (!it?.APP_QA_ADV.isNullOrEmpty()) {
                //获取广告信息
                mViewModel.getAdInfo(it?.APP_QA_ADV)
            }

        }
        mViewModel.userInfoLiveData.observe(this) {

            if (it.mbCustId > 0) {
                UserInfoUtils.saveCusId(it.mbCustId)
            }
            UserInfoUtils.saveUserInfo(Gson().toJson(it).toString())
            if (it.doubleLoan == 1) {
                //复贷用户直接跳计算
                ZipOrderReviewActivity.start(requireActivity(), "")
                return@observe
            }

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
                UserInfoUtils.saveBankData(Gson().toJson(it.first()))
                ZipOrderReviewActivity.start(requireActivity(), "")
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