package com.zip.zipandroid.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipCouponAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipCouponBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.viewmodel.CouponViewModel

class ZipCouponActivity : ZipBaseBindingActivity<CouponViewModel, ActivityZipCouponBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Coupon")
        mViewBind.privateIncludeTitle.titleRightTv.setText("Expired Coupons")
        mViewBind.privateIncludeTitle.titleRightTv.show()
        mViewBind.privateIncludeTitle.titleRightTv.setOnDelayClickListener {
            if (couponStatus == 4) {
                couponStatus = 1
            } else {
                couponStatus = 4
            }
            mViewModel.getCouponList(couponStatus)
        }
        mViewBind.zipCouponRv.layoutManager = LinearLayoutManager(this)
        mViewBind.zipCouponRv.adapter = couponAdapter
        mViewModel.getCouponList(couponStatus)

    }

    var couponStatus = 1

    val couponAdapter = ZipCouponAdapter()

    override fun createObserver() {
        mViewModel.couponLiveData.observe(this) {
            if (it.couponList.isNullOrEmpty()) {
                couponAdapter.setEmptyView(getEmptyView(this))
            } else {
                couponAdapter.couponStatus = couponStatus
                couponAdapter.setNewData(it.couponList)
            }
        }
    }

    fun getEmptyView(context: Context): View {
        val inflate: View = LayoutInflater.from(context).inflate(R.layout.coupon_empty_not_exp, null)
        val empty_coupon_icon = inflate.findViewById<ImageView>(R.id.empty_coupon_icon)
        if (couponStatus == 4) {
            empty_coupon_icon.setImageResource(R.drawable.zip_over_no_coupon)
        } else {
            empty_coupon_icon.setImageResource(R.drawable.zip_no_coupon_icon)
        }
        return inflate
    }

    override fun getData() {
    }
}