package com.zip.zipandroid.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.LazyPagerAdapter
import com.zip.zipandroid.base.ZipBaseBindingFragment
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.FragmentZipOrderListBinding
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView

class ZipOrderListFragment : ZipBaseBindingFragment<ZipBaseViewModel, FragmentZipOrderListBinding>() {
    companion object {
        fun newInstance(): ZipOrderListFragment {
            val args = Bundle()
            val fragment = ZipOrderListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val fragments: ArrayList<Fragment?> = ArrayList()
    override fun initView(savedInstanceState: Bundle?) {
        fragments.clear()
        val titles = arrayListOf("Unpaid Orders", "Processing Orders", "Other Orders")
        initTabs(titles)
        titles.forEachIndexed { index, s ->
            fragments.add(ZipOrderItemFragment.newInstance(index))
        }
        mViewBind.vpOrder.setAdapter(LazyPagerAdapter(childFragmentManager, fragments))
        ViewPagerHelper.bind(mViewBind.ordermagicIndicator, mViewBind.vpOrder)
    }

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }

    private fun initTabs(
        titles: ArrayList<String>,
    ) {
        val padding = ConvertUtils.dp2px(10f)
        val navigator = CommonNavigator(requireActivity())
        val textColor = ContextCompat.getColor(requireActivity(), R.color.cFFA5B0CE)
        val defaultColor = ContextCompat.getColor(requireActivity(), R.color.cFF3667F0)
        val navigatorHeight = ConvertUtils.dp2px(4f).toFloat()
        val navigatorWidth = ConvertUtils.dp2px(40f).toFloat()
        navigator.isAdjustMode = true
        navigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val badgePagerTitleView = BadgePagerTitleView(context)
                badgePagerTitleView.setPadding(padding, 0, padding, 0)
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = titles[index]
                clipPagerTitleView.textSize = ConvertUtils.dp2px(12f).toFloat()
                clipPagerTitleView.textColor = textColor
                clipPagerTitleView.clipColor = defaultColor
                clipPagerTitleView.setOnClickListener { v: View? ->
                    mViewBind.vpOrder.currentItem = index
                }
                badgePagerTitleView.innerPagerTitleView = clipPagerTitleView
                return badgePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.lineHeight = navigatorHeight
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.roundRadius = ConvertUtils.dp2px(13f).toFloat()
                indicator.setColors(ContextCompat.getColor(requireActivity(), R.color.cFF3667F0))
                indicator.lineWidth = navigatorWidth
                return indicator
            }
        }
        mViewBind.ordermagicIndicator.navigator = navigator
    }
}