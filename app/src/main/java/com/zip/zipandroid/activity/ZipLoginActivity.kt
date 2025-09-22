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
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipLoginBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.NoSpaceInputFilter
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipTrackUtils
import com.zip.zipandroid.viewmodel.ZipLoginModel


class ZipLoginActivity : ZipBaseBindingActivity<ZipLoginModel, ActivityZipLoginBinding>() {


    var loginSrc1Select = true


    override fun initView(savedInstanceState: Bundle?) {
        ZipTrackUtils.track("InLogin")
        val span = SpannableStringBuilder()
        span.append("I agree to the ")
        val start = span.length
        span.append("Terms of Service")
        val end = span.length
        span.setSpan(object : ClickableSpan() {
            override fun onClick(@NonNull widget: View) {
                ZipWebActivity.start(this@ZipLoginActivity, Constants.commonServiceUrl)
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
                ZipWebActivity.start(this@ZipLoginActivity, Constants.commonPrivateUrl)
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
                if ((mViewBind.zipLoginEdit.text?.toString()?.length ?: 0) >= 10) {
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

            override fun afterTextChanged(editable: Editable) {
                if (isFormatting) {
                    return
                }
                isFormatting = true
                val currentText = editable.toString().replace(" ", "") // 移除所有空格进行纯数字处理
                val length = currentText.length
                // 规则1：如果当前文本为空，清空lastValidText并直接返回
                if (currentText.isEmpty()) {
                    lastValidText = ""
                    isFormatting = false
                    return
                }
                // 规则2：检查首位，决定应用哪套规则
                val firstChar = currentText[0]
                // --- 10位模式 (首位为1-9) ---
                if (firstChar in '1'..'9') {
                    // 子规则2a：检查是否所有数字都相同（低效但清晰的写法，适用于10位）
                    val allDigitsSame = currentText.length == 10 && currentText.all { it == firstChar }
                    if (allDigitsSame) {
                        // 违反了“不能全部相同”的规则，回退到上一个有效文本
                        editable.replace(0, editable.length, lastValidText)
//                        mViewBind.zipLoginEdit.error = "The number cannot have all the same digits"
                        showPhoneFail()
                        isFormatting = false
                        mViewBind.zipLoginEdit.setBackgroundColor(Color.parseColor("#FFF1F1"))
                        return
                    }
                    // 子规则2b：长度不能超过10
                    if (length > 10) {
                        editable.replace(0, editable.length, lastValidText)
                        showPhoneFail()
//                        mViewBind.zipLoginEdit?.error = "enter a maximum of 10 digits."
                        isFormatting = false
                        return
                    }

                    if (editable.length > 9 && loginSrc1Select) {
                        mViewBind.zipLoginBtn.setEnabledPlus(true)
                        mViewBind.zipLoginEdit?.tag = "completed"
                        mViewBind.zipLoginEdit.setBackgroundColor(Color.parseColor("#F1F5FF"))
                    } else {
                        mViewBind.zipLoginBtn.setEnabledPlus(false)
                    }
                    val formattedText = currentText
                    // 清除错误提示（如果之前有）
                    mViewBind.zipLoginEdit?.error = null
                    lastValidText = formattedText

                } else if (firstChar == '0') {
                    // 子规则3a：检查第二位不能是0
                    if (length >= 2 && currentText[1] == '0') {
                        editable.replace(0, editable.length, lastValidText)
//                        mViewBind.zipLoginEdit.error = "The second digit cannot be 0"
                        showPhoneFail()
                        mViewBind.zipLoginEdit.setBackgroundColor(Color.parseColor("#FFF1F1"))
                        isFormatting = false
                        return
                    }
                    val formattedText = currentText
                    if (editable.toString() != formattedText) {
                        editable.replace(0, editable.length, formattedText)
                        mViewBind.zipLoginEdit.setSelection(editable.length)
                    }
                    mViewBind.zipLoginEdit.error = null
                    lastValidText = formattedText

                    if (editable.length > 10 && loginSrc1Select) {
                        mViewBind.zipLoginBtn.setEnabledPlus(true)
                        mViewBind.zipLoginEdit?.tag = "completed"
                        mViewBind.zipLoginEdit.setBackgroundColor(Color.parseColor("#F1F5FF"))
                    } else {
                        mViewBind.zipLoginBtn.setEnabledPlus(false)
                    }
                }
                // --- 无效模式 (首位既不是1-9也不是0，理论上被InputFilter阻止，此为安全备份) ---
                else {
                    editable.clear()
//                    mViewBind.zipLoginEdit.error = "The first digit must be 1-9 or 0"
                    showPhoneFail()
                    mViewBind.zipLoginEdit.setBackgroundColor(Color.parseColor("#FFF1F1"))
                    lastValidText = ""
                }
                isFormatting = false


            }

        })

        mViewBind.zipLoginBtn.setOnDelayClickListener {
//            showPickView("Date of Birth")
            if (loginSrc1Select == false) {
                ToastUtils.showShort("Please check the agreement")
                return@setOnDelayClickListener
            }
            ZipTrackUtils.track("ClickLogin")
            getZipCode()
        }
        mViewModel.getZipAppConfig()
//        show = Calendar.getInstance()
    }

    private fun showPhoneFail() {
        ToastUtils.showShort("Please enter a valid phone number")
    }

    private var isFormatting = false

    // 记录上一次有效的文本，用于在输入错误时回退
    private var lastValidText = ""

    private fun getZipCode() {
        var realPhone = getRealPhone()
        mViewModel.getCode("234" + realPhone)
        UserInfoUtils.setUserPhone("234" + realPhone)
        KeyboardUtils.hideSoftInput(this)
        showLoading()
    }

    private fun getRealPhone(): String {
        var realPhone = mViewBind.zipLoginEdit.text.toString()
        if (mViewBind.zipLoginEdit.text?.length == 11) {
            realPhone = mViewBind.zipLoginEdit.text?.substring(1, (mViewBind.zipLoginEdit.text?.length
                ?: 1)).toString()
        }
        return realPhone
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideSoftInput(this)
    }

    override fun createObserver() {
        mViewModel.configLiveData.observe(this) {
            Constants.commonServiceUrl = it?.APP_REGISTER_AGREEMENT ?: "https://www.baidu.com"//注册协议
            Constants.commonPrivateUrl = it?.APP_PRIVACY_AGREEMENT ?: "https://www.baidu.com"//隐私协议
            Constants.APP_LOAN_CONTRACT = it?.APP_LOAN_CONTRACT ?: "https://www.baidu.com"//接口协议
            Constants.APP_REPAYMENT_AGREEMENT = it?.APP_REPAYMENT_AGREEMENT
                ?: "https://www.baidu.com"//服务协议
            if (!(it?.APP_REPAYMENT_AGREEMENT
                    ?: "").isNullOrEmpty()
            ) {
                mViewModel.getProtocolBeforeLoan(it?.APP_REPAYMENT_AGREEMENT
                    ?: "", AppUtils.getAppName())
            }

        }

        mViewModel.codeLiveData.observe(this) {
            ZipCodeActivity.start(this, "234" + getRealPhone(), it?.code
                ?: "")
            dismissLoading()
        }
//        ZipCodeActivity.start(this,"2336665656656","5245")
    }

    override fun showFailToast() {
        super.showFailToast()
        dismissLoading()
        mViewBind.loginEditNumberSl.setBackground2(Color.parseColor("#FFFFECEC"))

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        ZipTrackUtils.track("OutLogin")
    }

    override fun getData() {
    }
}