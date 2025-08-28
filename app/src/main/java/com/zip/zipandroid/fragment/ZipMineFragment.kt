package com.zip.zipandroid.fragment

import android.os.Bundle
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.activity.ZipAboutUsActivity
import com.zip.zipandroid.activity.ZipBandCardActivity
import com.zip.zipandroid.activity.ZipCouponActivity
import com.zip.zipandroid.activity.ZipLoginActivity
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.FragmentZipMineBinding
import com.zip.zipandroid.event.ZipSwitchIndexEvent
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.ZipLogoutPop
import com.zip.zipandroid.utils.ActivityCollector
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.utils.UserInfoUtils

class ZipMineFragment : ZipBaseBindingFragment<ZipBaseViewModel, FragmentZipMineBinding>() {
    companion object {
        fun newInstance(): ZipMineFragment {
            val args = Bundle()
            val fragment = ZipMineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.getUserInfo()
        mViewBind.mineOrderCl.setOnDelayClickListener {
            //历史订单页面
            EventBusUtils.post(ZipSwitchIndexEvent(1))
        }

        mViewBind.mineCopCl.setOnDelayClickListener {
            //优惠券页面
            startActivity(ZipCouponActivity::class.java)
        }

        mViewBind.zipMineBankSl.setOnDelayClickListener {
            //银行卡页面
            startActivity(ZipBandCardActivity::class.java)
        }
        mViewBind.zipMineAboutSl.setOnDelayClickListener {
            //关于我们的
            startActivity(ZipAboutUsActivity::class.java)
        }
        mViewBind.zipMineLogoutSl.setOnDelayClickListener {
            val pop = ZipLogoutPop(requireActivity())
            pop.sureLogoutClick = {
                ActivityCollector.removeAllActivity()
                startActivity(ZipLoginActivity::class.java)
                UserInfoUtils.clear()
            }
            XPopup.Builder(getContext()).asCustom(pop).show()

        }
    }

    override fun createObserver() {
        mViewModel.userInfoLiveData.observe(this) {
            if (it.firstName.isNullOrEmpty()) {
                val name = getLastFourDigits(UserInfoUtils.getUserPhone())
                mViewBind.zipMineNameTv.setText("H,User${name}")
            } else {
                mViewBind.zipMineNameTv.setText("Hi,${it.firstName} ${it.lastName}")
            }
        }
    }

    fun getLastFourDigits(input: String): String {
        return if (input.length >= 4) {
            input.takeLast(4) // 直接取后4位
        } else {
            input // 不足4位返回原字符串
        }
    }

    override fun lazyLoadData() {
    }
}