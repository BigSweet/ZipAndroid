package com.zip.zipandroid.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipCustomServiceBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.viewmodel.ZipHomeViewModel

class ZipCustomServiceActivity : ZipBaseBindingActivity<ZipHomeViewModel, ActivityZipCustomServiceBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.zipCustomAppName.setText(AppUtils.getAppName())
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Customer Service")
        mViewModel.getZipAppConfig()
        mViewBind.zipEmailSl.setOnDelayClickListener {
            ClipboardUtils.copyText(mViewBind.zipEmailTv.text.toString())
            ToastUtils.showShort("copy success")
        }
        mViewBind.zipWhatAppSl.setOnDelayClickListener {
            val phoneNumber = mViewBind.zipWhatAppTv.text.toString()
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber))
                intent.setPackage("com.whatsapp")
                startActivity(intent);
            } catch (e: Exception) {
                //  没有安装WhatsApp
                ClipboardUtils.copyText(mViewBind.zipWhatAppTv.text.toString())
                ToastUtils.showShort("copy success")
            }
        }
        mViewBind.zipPhoneTv.setOnDelayClickListener {
            val phoneNumber = mViewBind.zipPhoneTv.text.toString()
            gotoNumber(phoneNumber)
        }
    }

    fun gotoNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    override fun createObserver() {
        mViewModel.configLiveData.observe(this) {
            val whatApp = it?.APP_CUSTOMER_SERVICE_WHATSAPP
            val phone = it?.APP_MX_SOCIAL_MS_SWITCH
            val email = it?.APP_CUSTOMER_SERVICE_EMAIL
            mViewBind.zipPhoneTv.setText(phone)
            mViewBind.zipWhatAppTv.setText(whatApp)
            mViewBind.zipEmailTv.setText(email)

        }
    }

    override fun getData() {
    }
}