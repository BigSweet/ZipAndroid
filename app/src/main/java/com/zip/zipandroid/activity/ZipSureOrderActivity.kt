package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipDurationAdapter
import com.zip.zipandroid.adapter.ZipInstallAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.PeriodStage
import com.zip.zipandroid.bean.ZipCouponItemBean
import com.zip.zipandroid.bean.ZipHomeDataBean
import com.zip.zipandroid.bean.ZipProductPeriodItem
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.databinding.ActivityZipSureOrderBinding
import com.zip.zipandroid.event.ZipRefreshCardEvent
import com.zip.zipandroid.event.ZipSelectCouponEvent
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.pop.ZipRepaymentPlanPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.view.toN
import com.zip.zipandroid.viewmodel.ZipReviewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZipSureOrderActivity : ZipBaseBindingActivity<ZipReviewModel, ActivityZipSureOrderBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, amount: String, riskLevel: String, bizId: String) {
            val starter = Intent(context, ZipSureOrderActivity::class.java)
                .putExtra("amount", amount)
                .putExtra("riskLevel", riskLevel)
                .putExtra("bizId", bizId)
            context.startActivity(starter)
        }

    }

    var amount = ""
    var realAmount = 0
    var riskLevel = ""
    var preBizId = ""
    var realOrderBizId = ""


    override fun initView(savedInstanceState: Bundle?) {
        amount = intent.getStringExtra("amount") ?: ""
        riskLevel = intent.getStringExtra("riskLevel") ?: ""
        preBizId = intent.getStringExtra("bizId") ?: ""
        realAmount = amount.toInt()
        mViewBind.orderAddIv.setOnDelayClickListener {
            //加金额 步长默认2000
            if (realAmount + limitInterval > limitMax) {
                //超过了就默认最大
                realAmount = limitMax
            } else {
                realAmount = realAmount + limitInterval
            }
            mViewBind.sureOrderAmountTv.setText(realAmount.toN())
            //试算
            orderTrialData()
        }
        mViewBind.bankSl.setOnDelayClickListener {
            //修改银行卡
            ZipBandCardActivity.start(this, true)
//            startActivity(ZipBandCardActivity::class.java)
        }
        mViewBind.planSl.setOnDelayClickListener {
            val pop = ZipRepaymentPlanPop(this, zipTriaBean)
            XPopup.Builder(this).asCustom(pop).show()
        }

        mViewModel.uploadUserInfoLiveData.observe(this) {
            mViewModel.realOrder(callInfo, installAppInfo, zipSmsMessageInfos, calendarInfo, it,
                realAmount, currentPaidType, currentDid.toString(), currentCouponId, currentDid, preBizId, riskLevel)
        }

        mViewModel.realOrderLiveData.observe(this) {
            dismissLoading()
            realOrderBizId = it?.bizId ?: ""
            //跳转到下一个界面
            ZipOrderNextActivity.start(this,realOrderBizId)
            finish()
        }

        mViewBind.orderAcceptLoan.setOnDelayClickListener {
            showLoading()
            mViewModel.getUploadUserInfo()
        }
        mViewBind.orderSubIv.setOnDelayClickListener {
            //减金额
            //加金额 步长默认2000
            if (realAmount - limitInterval < limitMin) {
                //超过了就默认最大
                realAmount = limitMin
            } else {
                realAmount = realAmount - limitInterval
            }
            mViewBind.sureOrderAmountTv.setText(realAmount.toN())
            //试算
            orderTrialData()
        }
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.zipSureInstallRv.layoutManager = LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        mViewBind.zipSureDurationRv.layoutManager = LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        mViewBind.zipSureInstallRv.adapter = installAdapter
        mViewBind.zipSureDurationRv.adapter = duraAdapter
        duraAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as ZipProductPeriodItem
            duraAdapter.selectPosition = i
            duraAdapter.notifyDataSetChanged()
            installAdapter.setNewData(item.periodStages)
            if (!item.periodStages.isNullOrEmpty()) {
                currentDid = item.periodStages.first().did
                findPaidType()
            }
            orderTrialData()
        }
        installAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as PeriodStage
            installAdapter.selectPosition = i
            installAdapter.notifyDataSetChanged()
            currentDid = item.did
            findPaidType()
            orderTrialData()
        }

        mViewBind.sureOrderAmountTv.setText(realAmount.toN())

        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Loan Application")

        mViewModel.zipHomeData()

    }

    private fun orderTrialData() {
        mViewModel.orderTrial(realAmount, riskLevel, currentDid.toString(), currentCouponId)
    }

    var limitMax = 0
    var limitMin = 0
    var limitInterval = 0
    var currentDid = ""
    var currentPaidType = ""
    var intervalStart = ""
    var currentCouponId = ""

    var installAdapter = ZipInstallAdapter()
    var duraAdapter = ZipDurationAdapter()


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipRefreshCardEvent) {
        mViewBind.bankCardTv.setText(UserInfoUtils.getBankData()?.cardNo)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipSelectCouponEvent) {
        if (!allCouponList.isNullOrEmpty()) {
            val data = allCouponList?.find {
                it.id.toString() == event.couponId
            }
            if (data != null) {
                currentCouponId = data.id.toString()
                setCouponData(data)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        ZipEventBusUtils.unregister(this)
    }

    var allCouponList: List<ZipCouponItemBean>? = null
    override fun createObserver() {
        mViewBind.couponCenterCl.setOnDelayClickListener {
            //去选择优惠券界面
            ZipCouponActivity.start(this, true)
        }
        ZipEventBusUtils.register(this)
        mViewModel.couponLiveData.observe(this) {
            mViewBind.couponPriceTv.hide()
            mViewBind.orderCouponNameTv.hide()
            allCouponList = it.couponList
            if (it.couponList.isNullOrEmpty()) {
                //空的
                orderTrialData()
                mViewBind.couponCenterCl.setBackgroundResource(R.drawable.zip_empty_coupon_icon)

                //空
            } else {
                if (it.couponList.size == 1) {
                    //选中
                    currentCouponId = it.couponList.first().id.toString()
                    setCouponData(it.couponList.first())
                } else {
                    //空
                    mViewBind.couponCenterCl.setBackgroundResource(R.drawable.zip_empty_coupon_icon)

                    orderTrialData()
                }
            }

        }
        mViewModel.homeLiveData.observe(this) {
            var realIndex = 0
            zipHomeDataBean = it
            duraAdapter.selectPosition = realIndex
            duraAdapter.setNewData(it.productList.productPeriods)
            if (!it.productList.productPeriods.isNullOrEmpty()) {
                installAdapter.setNewData(it.productList.productPeriods.first().periodStages)
                if (!it.productList.productPeriods.first().periodStages.isNullOrEmpty()) {
                    currentDid = it.productList.productPeriods.first().periodStages.first().did
                    findPaidType()
                }
            }
            limitMax = it.productList.limitMax.toDouble().toInt()
            limitMin = it.productList.limitMin.toDouble().toInt()
            limitInterval = it.productList.limitInterval
            mViewModel.getCouponList(1)
        }

        mViewBind.orderPrivateClickView.setOnDelayClickListener {
            if (orderSrc1Select) {
                orderSrc1Select = false
                mViewBind.zipOrderSelectIv.setImageResource(R.drawable.zip_login_normal_icon)
                mViewBind.orderAcceptLoan.setEnabledPlus(false)
            } else {
                orderSrc1Select = true
                mViewBind.zipOrderSelectIv.setImageResource(R.drawable.zip_login_select_icon)
                mViewBind.orderAcceptLoan.setEnabledPlus(true)
            }
        }



        mViewModel.orderTrialLiveData.observe(this) {
            mViewBind.couponInterTv.setText(it.couponAmount)
            zipTriaBean = it
            mViewBind.realInterTv.setText(it.totalInsterst.toDouble().toN())
            mViewBind.realManagerTv.setText(it.totalFee.toDouble().toN())
            mViewBind.bankCardTv.setText(UserInfoUtils.getBankData()?.cardNo)
            mViewBind.loanBottomPriceTv.setText(it.payAmount.toInt().toN())
            mViewBind.totalAmountTv.setText(it.totalAmount.toDouble().toN())
            if (!it.repaymentList.isNullOrEmpty()) {
                val itemRepay = it.repaymentList.first()
                mViewBind.planPriceTv.setText(itemRepay.shouldAmount.toInt().toN())
                mViewBind.planTimeTv.setText(formatTimestampToDate(itemRepay.shouldTime))
            }
            if (it.couponAmount != null && it.couponAmount.toInt() > 0) {
                mViewBind.couponInterTv.show()
                mViewBind.couponLineView.show()
            } else {
                mViewBind.couponLineView.hide()
                mViewBind.couponInterTv.hide()

            }
        }


        val span = SpannableStringBuilder()
        span.append("I confirm that I have reviewed and accepted the ")
        val start = span.length
        span.append("Loan Agreement")
        val end = span.length
        span.setSpan(object : ClickableSpan() {
            override fun onClick(@NonNull widget: View) {
                ZipWebActivity.start(this@ZipSureOrderActivity, Constants.APP_LOAN_CONTRACT)
            }

            override fun updateDrawState(@NonNull ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#00000000")
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.parseColor("#FF3667F0")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        span.append(" and ")

        val star1 = span.length
        span.append("Repayment Contract")
        val end1 = span.length
        span.setSpan(object : ClickableSpan() {
            override fun onClick(@NonNull widget: View) {
                ZipWebActivity.start(this@ZipSureOrderActivity, Constants.APP_REPAYMENT_AGREEMENT)
            }

            override fun updateDrawState(@NonNull ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#00000000")
            }
        }, star1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.parseColor("#FF3667F0")), star1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        mViewBind.zipOrderPrivateTv.movementMethod = LinkMovementMethod.getInstance()
        mViewBind.zipOrderPrivateTv.highlightColor = Color.TRANSPARENT
        mViewBind.zipOrderPrivateTv.setText(span)


    }

    var zipHomeDataBean: ZipHomeDataBean? = null
    private fun findPaidType() {
        if (!zipHomeDataBean?.productList?.productDidInfos.isNullOrEmpty()) {
            val data = zipHomeDataBean?.productList?.productDidInfos?.find { it.did.toString() == currentDid }
            if (data?.paidType != null) {
                currentPaidType = data?.paidType.toString()
                intervalStart = data?.intervalStart.toString()
            }
        }
    }

    var zipTriaBean: ZipTriaBean? = null

    private fun setCouponData(data: ZipCouponItemBean) {
        mViewBind.couponPriceTv.show()
        mViewBind.orderCouponNameTv.show()
        mViewBind.couponPriceTv.setText(data.worth.toString())
        mViewBind.orderCouponNameTv.setText(data.typeStr.toString())
        mViewBind.couponCenterCl.setBackgroundResource(R.drawable.zip_bg_coupon)
        orderTrialData()
    }

    var orderSrc1Select = false
    fun splitNumber(n: Int): List<Int> {
        require(n > 0) { "n must be a positive integer" }
        return (1..n).toList()
    }

    fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // 英文月份格式
        val date = Date(timestamp)
        return "${dateFormat.format(date)}"
    }

    override fun getData() {
    }
}