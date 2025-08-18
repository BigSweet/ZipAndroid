package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.widget.TextView
import com.zip.zipandroid.R
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.databinding.ActivityZipAndroidWebBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipWebActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipAndroidWebBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, url: String) {
            val starter = Intent(context, ZipWebActivity::class.java)
                .putExtra("url", url)
            context.startActivity(starter)
        }
    }


    override fun createObserver() {
    }

    override fun getData() {
    }


    override fun initView(savedInstanceState: Bundle?) {

        val common_title_rl = mViewBind.commonReviewTitleCl.commonTitleRl
        val common_back_iv = mViewBind.commonReviewTitleCl.commonBackIv
        updateToolbarTopMargin(common_title_rl)
        common_back_iv.setOnDelayClickListener {
            finish()
        }

        val url = intent.getStringExtra("url") ?: ""
        //设置不支持字体缩放
        mViewBind.webView.settings.setSupportZoom(true)
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
                        val title = view.title ?: ""
                        if (!TextUtils.isEmpty(title) && !title.startsWith("http")) {
                            tvCenter.setText(view.title)
                        } else {
                            tvCenter.setText(resources.getString(R.string.app_name))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        mViewBind.webView.loadUrl(url)
    }
}