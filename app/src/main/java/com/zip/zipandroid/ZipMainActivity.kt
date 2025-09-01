package com.zip.zipandroid

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.PermissionUtils
import com.lxj.xpopup.XPopup
import com.zip.zipandroid.activity.ZipOrderDetailActivity
import com.zip.zipandroid.adapter.LazyPagerAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityMainBinding
import com.zip.zipandroid.event.ZipSwitchIndexEvent
import com.zip.zipandroid.fragment.ZipHomeFragment
import com.zip.zipandroid.fragment.ZipMineFragment
import com.zip.zipandroid.fragment.ZipOrderListFragment
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.pop.ZipAllPerPop
import com.zip.zipandroid.utils.AllPerUtils
import com.zip.zipandroid.utils.AnimationUtils
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.utils.OnNoDoubleClickListener
import com.zip.zipandroid.utils.UserInfoUtils
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ZipMainActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityMainBinding>() {
    private val mFragments: ArrayList<Fragment> = ArrayList()
    override fun initView(savedInstanceState: Bundle?) {
        mFragments.clear()
        val homeFragment = ZipHomeFragment.newInstance()
        val orderFragment = ZipOrderListFragment.newInstance()
        val mineFragment = ZipMineFragment.newInstance()
        mFragments.add(homeFragment)
        mFragments.add(orderFragment)
        mFragments.add(mineFragment)
//        mFragments.add(mineFragment)
        val defaultIcons = ArrayList<Int>()
        val selectedIcons = ArrayList<Int>()
        if (defaultIcons.size == 0 || selectedIcons.size == 0) {
            defaultIcons.add(R.drawable.icon_home_default)
            defaultIcons.add(R.drawable.icon_order_default)
            defaultIcons.add(R.drawable.icon_mine_default)
            selectedIcons.add(R.drawable.icon_home_selected)
            selectedIcons.add(R.drawable.icon_order_selected)
            selectedIcons.add(R.drawable.icon_mine_selected)
        }
        initTabs(selectedIcons, defaultIcons)
        mViewBind.vpMain.adapter = LazyPagerAdapter(supportFragmentManager, mFragments)
        ViewPagerHelper.bind(mViewBind.magicIndicator, mViewBind.vpMain)
        mViewBind.vpMain.setOffscreenPageLimit(mFragments.size)
        EventBusUtils.register(this)
        checkAllPer()
        mViewBind.testBtn.setOnDelayClickListener {
            ZipOrderDetailActivity.start(this,null)
        }
    }

    fun checkAllPer() {
        if (!PermissionUtils.isGranted(AllPerUtils.phoneStatusPer, AllPerUtils.netWorkStatusPer, AllPerUtils.redCalendar, AllPerUtils.wifiStatus, AllPerUtils.smsStatus)) {
//        if (!PermissionUtils.isGranted(AllPerUtils.smsStatus)) {
            //权限弹窗
            val pop = ZipAllPerPop(getContext())
            pop.allPerFail = {
                //退出登录，关闭界面
                UserInfoUtils.clear()
                finish()
            }
            pop.allPerSuccess = {
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
            XPopup.Builder(getContext()).asCustom(pop).show()

        }
    }

    fun getTestPerList(): ArrayList<String> {
        val list = arrayListOf<String>()
        list.add(Manifest.permission.READ_SMS)
        return list
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipSwitchIndexEvent) {
        mViewBind.vpMain.setCurrentItem(event.index)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun createObserver() {
    }


    override fun getData() {
    }


    private var currentIndex = 0
    private fun initTabs(
        selectedIcons: ArrayList<Int>,
        defaultIcons: ArrayList<Int>,
    ) {
        val navigator = CommonNavigator(this)
        navigator.isAdjustMode = true
        navigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return defaultIcons.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val commonPagerTitleView = CommonPagerTitleView(this@ZipMainActivity)
                commonPagerTitleView.setContentView(R.layout.item_main_tab)
                val titleImg: ImageView = commonPagerTitleView.findViewById(R.id.iv_tab_icon)

                commonPagerTitleView.onPagerTitleChangeListener =
                    object : CommonPagerTitleView.OnPagerTitleChangeListener {
                        override fun onSelected(index: Int, totalCount: Int) {
                            titleImg.setImageResource(selectedIcons[index])
                        }

                        override fun onDeselected(index: Int, totalCount: Int) {
                            titleImg.setImageResource(defaultIcons[index])
                        }

                        override fun onLeave(
                            index: Int,
                            totalCount: Int,
                            leavePercent: Float,
                            leftToRight: Boolean,
                        ) {
                        }

                        override fun onEnter(
                            index: Int,
                            totalCount: Int,
                            enterPercent: Float,
                            leftToRight: Boolean,
                        ) {
                        }
                    }
                commonPagerTitleView.setOnClickListener(object : OnNoDoubleClickListener() {
                    override fun onNoDoubleClick(v: View) {
                        AnimationUtils.tabAnim(titleImg)
                        mViewBind.vpMain.currentItem = index
                        currentIndex = index
//                        (mFragments.get(index) as MacawMacawBaseBindingFragment<BaseViewModel, ViewDataBinding>).refreshData()
                    }
                })
                return commonPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        mViewBind.magicIndicator.navigator = navigator
    }
}
