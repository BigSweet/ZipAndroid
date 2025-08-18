package com.zip.zipandroid.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ktx.immersionBar
import com.zip.zipandroid.utils.ActivityCollector

abstract class ZipBaseModelActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBind().notNull({
            setContentView(it)
        }, {
            setContentView(layoutId())
        })
        init(savedInstanceState)
        immersionBar {
            statusBarDarkFont(true)
//            statusBarColor(R.color.tranlate)
//            autoStatusBarDarkModeEnable(true)
//            fitsSystemWindows(true)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 竖屏
        ActivityCollector.addActivity(this)

    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        getData()
    }


    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    abstract fun createObserver()
    abstract fun getData()

    open fun initDataBind(): View? {
        return null
    }
}