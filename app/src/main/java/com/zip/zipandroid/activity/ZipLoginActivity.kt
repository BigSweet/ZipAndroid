package com.zip.zipandroid.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.NonNull
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipLoginBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.NoSpaceInputFilter
import com.zip.zipandroid.viewmodel.ZipLoginModel


class ZipLoginActivity : ZipBaseBindingActivity<ZipLoginModel, ActivityZipLoginBinding>() {


    var loginSrc1Select = false


    override fun initView(savedInstanceState: Bundle?) {
        val span = SpannableStringBuilder()
        span.append("I agree to the ")
        val start = span.length
        span.append("Terms of Service")
        val end = span.length
        span.setSpan(object : ClickableSpan() {
            override fun onClick(@NonNull widget: View) {
                ZipWebActivity.start(this@ZipLoginActivity, Constants.commonWebUrl)
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
        span.append("Privacy Policy")
        val end1 = span.length
        span.setSpan(object : ClickableSpan() {
            override fun onClick(@NonNull widget: View) {
                ZipWebActivity.start(this@ZipLoginActivity, Constants.commonWebUrl)
            }

            override fun updateDrawState(@NonNull ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#00000000")
            }
        }, star1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.parseColor("#FF3667F0")), star1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        mViewBind.zipLoginPrivateTv.movementMethod = LinkMovementMethod.getInstance()
        mViewBind.zipLoginPrivateTv.highlightColor = Color.TRANSPARENT
        mViewBind.zipLoginPrivateTv.setText(span)

        mViewBind.privateClickView.setOnDelayClickListener {
            if (loginSrc1Select) {
                mViewBind.zipLoginSelectIv.setImageResource(R.drawable.zip_login_normal_icon)
                loginSrc1Select = false
                mViewBind.zipLoginBtn.setEnabledPlus(false)
            } else {
                loginSrc1Select = true
                mViewBind.zipLoginSelectIv.setImageResource(R.drawable.zip_login_select_icon)
                if ((mViewBind.zipLoginEdit.text?.toString()?.length ?: 0) > 10) {
                    mViewBind.zipLoginBtn.setEnabledPlus(true)
                }
            }
        }

        mViewBind.zipLoginEdit.setMaxLength(11)
        mViewBind.zipLoginEdit.setOnDelayClickListener {
            mViewBind.loginEditNumberSl.setBackground2(Color.parseColor("#FFF1F5FF"))
            mViewBind.zipLoginEdit.requestFocus()
        }
        KeyboardUtils.showSoftInput(mViewBind.zipLoginEdit)
        mViewBind.zipLoginEdit.filters = arrayOf<InputFilter>(NoSpaceInputFilter())
        mViewBind.zipLoginEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 10 && loginSrc1Select) {
                    mViewBind.zipLoginBtn.setEnabledPlus(true)
                } else {
                    mViewBind.zipLoginBtn.setEnabledPlus(false)
                }

            }

        })

        mViewBind.zipLoginBtn.setOnDelayClickListener {
            if (loginSrc1Select == false) {
                ToastUtils.showShort("Please check the agreement")
                return@setOnDelayClickListener
            }
            getZipCode()
        }
    }

    private fun getZipCode() {
        mViewModel.getCode(mViewBind.zipLoginEdit.text.toString())
        KeyboardUtils.hideSoftInput(this)
        showLoading()
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideSoftInput(this)
    }

    override fun createObserver() {
        mViewModel.codeLiveData.observe(this) {
            dismissLoading()
        }
        ZipCodeActivity.start(this,"2336665656656","5245")
    }

    override fun showFailToast() {
        super.showFailToast()
        dismissLoading()
        mViewBind.loginEditNumberSl.setBackground2(Color.parseColor("#FFFFECEC"))

    }

    override fun getData() {
    }
}