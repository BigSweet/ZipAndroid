package com.zip.zipandroid.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zip.zipandroid.activity.ZipBandCardActivity
import com.zip.zipandroid.activity.ZipContractActivity
import com.zip.zipandroid.activity.ZipCustomServiceActivity
import com.zip.zipandroid.activity.ZipOrderDetailActivity
import com.zip.zipandroid.activity.ZipOrderReviewActivity
import com.zip.zipandroid.activity.ZipPersonInfoActivity
import com.zip.zipandroid.activity.ZipQuestionActivity
import com.zip.zipandroid.activity.ZipSureOrderActivity
import com.zip.zipandroid.activity.ZipWebActivity
import com.zip.zipandroid.activity.ZipWorkInfoActivity
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.bean.ZipUserInfoBean
import com.zip.zipandroid.databinding.FragmentZipHomeBinding
import com.zip.zipandroid.event.ZipRefreshHomeEvent
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipTimeUtils
import com.zip.zipandroid.view.toN
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
            checkUserInfo()
        }
        mViewBind.newHomeSwpie.setOnRefreshListener {
            getAllData()
        }
        mViewBind.zipHomeCustomIv.setOnDelayClickListener {
            startActivity(ZipCustomServiceActivity::class.java)
        }
        mViewBind.viewPrivateTv.setOnDelayClickListener {
            ZipWebActivity.start(requireActivity(), Constants.commonPrivateUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllData()
    }

    fun getAllData() {
        mViewModel.zipHomeData()
        mViewModel.getZipAppConfig()
        mViewModel.getUserInfo()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipRefreshHomeEvent) {
        mViewModel.zipHomeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusUtils.unregister(this)
    }


//
//    WAITING 挂起状态，跳转到订单确认页面，等待用户确认，用户确认或取消后跳转到首页。
//


    override fun createObserver() {
        EventBusUtils.register(this)
        mViewModel.homeLiveData.observe(this) {
            if (it.userOrderStatus == 0) {
                //展示首页费率
                mViewBind.loadTeamSl.show()
                mViewBind.loadDailySl.show()
            } else {
                mViewBind.loadTeamSl.hide()
                mViewBind.loadDailySl.hide()
            }

            if (mViewBind.newHomeSwpie.isRefreshing) {
                mViewBind.newHomeSwpie.isRefreshing = false
            }
            UserInfoUtils.setProductType(Gson().toJson(it.productList))
            UserInfoUtils.saveProductDue(Gson().toJson(it.productDidInfo))
            mViewBind.zipHomeMoneyTv.setText(it.productList.limitMax)
            mViewBind.homeReviewCl.hide()
            mViewBind.homeOrderNormalCl.hide()
            mViewBind.homeDelayCl.hide()
            mViewBind.homePayingCl.hide()
            mViewBind.homeSubmitRefuseCl.hide()
            mViewBind.homeBankFailCl.hide()
            mViewBind.homeCanLoanCl.hide()
            mViewBind.homeLoaningIv.hide()

            if (it.creditOrderList?.status == "WAITING") {
                //直接去下单页面
                mViewBind.homeCanLoanCl.show()
                //授信后返回的金额？
                mViewBind.canLoanMoneyTv.setText(UserInfoUtils.getLevelData().grantAmount)
                mViewBind.canLoanNowTv.setOnDelayClickListener {
                    ZipSureOrderActivity.start(requireActivity(), UserInfoUtils.getLevelData().grantAmount, UserInfoUtils.getLevelData().riskLevel, UserInfoUtils.getPreBizId())
                }
            }

            if (it.creditOrderList?.status == "NOTREPAID" || it.creditOrderList?.status == "PARTIAL" || it.creditOrderList?.status == "LENDING" || it.creditOrderList?.status == "PASSED") {
                mViewBind.homePayingCl.show()
                mViewBind.payingDateTv.setText(it.creditOrderList?.applyAmount.toN())
                mViewBind.payingMoneyTv.setText(ZipTimeUtils.formatTimestampToDate(it.creditOrderList?.applyTime))
                val bizId = it.creditOrderList.bizId
                mViewBind.payNowTv.setOnDelayClickListener {
                    //去还款
                    ZipOrderDetailActivity.start(requireActivity(), bizId, 0)
                }
            }
            if (it.creditOrderList?.status == "CANCELED" || it.creditOrderList?.status == "FINISH") {
                mViewBind.homeOrderNormalCl.show()
                mViewBind.zipHomeMoneyTv.setText(it.productList.limitMax)
                mViewBind.zipHomeVerTv.setOnDelayClickListener {
                    //查到了第几部，在去进件
                    checkUserInfo()
                }
            }
            if (it.creditOrderList?.status == "OVERDUE") {
                mViewBind.homeDelayCl.show()
                mViewBind.delayMoneyTv.setText(it.creditOrderList?.amountDue.toN())
                mViewBind.delayDateTv.setText(ZipTimeUtils.formatTimestampToDate(it.creditOrderList?.periodTime))
                val span = SpannableStringBuilder()
                span.append("Your payment is ")
                val start = span.length
                span.append("${it.creditOrderList?.overdueDays} days")
                val end = span.length
                span.setSpan(ForegroundColorSpan(Color.parseColor("#FF6E69")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                span.append("past due, please make your repayment as soon as possible.")
                mViewBind.delayDesBottomTv.setText(span)
                val bizId = it.creditOrderList.bizId
                mViewBind.delayNowTv.setOnDelayClickListener {
                    //去还款
                    ZipOrderDetailActivity.start(requireActivity(), bizId, 0)
                }
            }
            if (it.creditOrderList?.status == "FAIL") {
                mViewBind.homeBankFailCl.show()
                mViewBind.updateBankTv.setOnDelayClickListener {
                    //跳转过去后继续授信在进件
                    ZipBandCardActivity.start(requireActivity(), false)
                }
            }
            if (it.creditOrderList?.status == "REFUSED") {
                mViewBind.homeSubmitRefuseCl.show()
            }
            if (it.creditOrderList?.status == "EXECUTING" || it.creditOrderList?.status == "WAITING") {
                mViewBind.homeReviewCl.show()
            }
            if (it.creditOrderList?.status == "TRANSACTION") {
                mViewBind.homeLoaningIv.show()
            }
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
            userInfo = it

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

    var userInfo: ZipUserInfoBean? = null
    fun checkUserInfo() {
        val it = userInfo ?: return
        if (it.mbCustId > 0) {
            UserInfoUtils.saveCusId(it.mbCustId)
        }
        UserInfoUtils.saveUserInfo(Gson().toJson(it).toString())
        if (it.doubleLoan == 1) {
            //复贷用户直接跳计算
            ZipOrderReviewActivity.start(requireActivity(), "")
            return
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

    override fun lazyLoadData() {
    }
}