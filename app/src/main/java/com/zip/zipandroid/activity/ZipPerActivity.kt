package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.widget.TextView
import com.tencent.mmkv.MMKV
import com.zip.zipandroid.R
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipPrivateBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants

class ZipPerActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipPrivateBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPerActivity::class.java)
            context.startActivity(starter)
        }

    }


    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Privacy Policy")
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        val url = Constants.commonPrivateUrl
        //设置不支持字体缩放
        mViewBind.webView.settings.setSupportZoom(true)
        mViewBind.webView.settings.loadWithOverviewMode = (true)
        mViewBind.webView.settings.setDomStorageEnabled(true)
        mViewBind.webView.settings.builtInZoomControls = true
        mViewBind.webView.settings.useWideViewPort = true
        mViewBind.webView.settings.javaScriptEnabled = true
        mViewBind.webView.settings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
        val tvCenter = findViewById<TextView>(R.id.title_bar_title_tv)
        mViewBind.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: android.webkit.WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                try {
                    if (newProgress == 100) {
                        dismissLoading()
                        val title = view.title ?: ""
                        if (!TextUtils.isEmpty(title) && !title.startsWith("http")) {
//                            tvCenter.setText(view.title)
                        } else {
//                            tvCenter.setText(resources.getString(R.string.app_name))
                        }
                    }
                } catch (e: Exception) {
                    dismissLoading()
                    e.printStackTrace()
                }
            }
        }
        showLoading()


        mViewBind.privateSureTv.setOnDelayClickListener {
            MMKV.defaultMMKV()?.putString("app_per", "done")
            //去登录页面
            startActivity(ZipLoginActivity::class.java)
            finish()
        }
        mViewBind.privateCancelTv.setOnDelayClickListener {
            finish()
        }
        mViewBind.webView.loadUrl(url)
    }


    override fun createObserver() {
    }

    override fun getData() {
    }
}