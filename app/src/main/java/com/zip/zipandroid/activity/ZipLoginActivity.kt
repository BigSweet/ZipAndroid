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
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipLoginBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.NoSpaceInputFilter
import com.zip.zipandroid.utils.ZipStringUtils
import com.zip.zipandroid.viewmodel.ZipLoginModel
import java.util.Calendar
import java.util.Date


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

            override fun afterTextChanged(s: Editable) {
                if (s.length > 10 && loginSrc1Select) {
                    mViewBind.zipLoginBtn.setEnabledPlus(true)
                } else {
                    mViewBind.zipLoginBtn.setEnabledPlus(false)
                }

            }

        })

        mViewBind.zipLoginBtn.setOnDelayClickListener {
//            showPickView("Date of Birth")
            if (loginSrc1Select == false) {
                ToastUtils.showShort("Please check the agreement")
                return@setOnDelayClickListener
            }
            getZipCode()
        }
//        show = Calendar.getInstance()
    }

    var show: Calendar? = null
    fun showPickView(title: String) {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start[Calendar.YEAR] = end[Calendar.YEAR] - 50
        end[Calendar.YEAR] = end[Calendar.YEAR]
        if (show == null) {
            show = Calendar.getInstance()
            show!![Calendar.YEAR] = end[Calendar.YEAR] - 30
        }

        var pickerView = TimePickerBuilder(getContext(), object : OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val result = Calendar.getInstance()
                result.time = date
                show = result
                val calendar = Calendar.getInstance()
                calendar.time = date
                val day = calendar[Calendar.DATE]
                val realDay = ZipStringUtils.addZero(day)
                val month = ZipStringUtils.addZero(calendar[Calendar.MONTH] + 1)
                val year = calendar.get(java.util.Calendar.YEAR)
                Log.d("选的日期", "$year-$month-$realDay")
            }

        })
            .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setCancelText("Cancel") //取消按钮文字
            .setSubmitText("Confirm") //确认按钮文字
            //                .setContentSize(18)//滚轮文字大小
            .setTitleText(title) //标题文字
            .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(false) //是否循环滚动
            .setTitleColor(getResources().getColor(R.color.black)) //标题文字颜色
            .setSubmitColor(getResources().getColor(R.color.white)) //确定按钮文字颜色
            .setCancelColor(getResources().getColor(R.color.cFF3667F0)) //取消按钮文字颜色
            .setTitleBgColor(Color.WHITE) //标题背景颜色 Night mode
            .setBgColor(Color.WHITE) //滚轮背景颜色 Night mode
            .setDate(show) // 如果不设置的话，默认是系统时间*/
            .setRangDate(start, end) //起始终止年月日设定
            .setLabel("", "", "", "", "", "") //默认设置为年月日时分秒
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .isDialog(false) //是否显示为对话框样式
            .build()
        pickerView.show()
    }

    private fun getZipCode() {
        mViewModel.getCode("234" + mViewBind.zipLoginEdit.text.toString())
        KeyboardUtils.hideSoftInput(this)
        showLoading()
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideSoftInput(this)
    }

    override fun createObserver() {
        mViewModel.configLiveData.observe(this) {
            Constants.commonServiceUrl = it?.APP_REGISTER_AGREEMENT ?: "www.baidu.com"//注册协议
            Constants.commonPrivateUrl = it?.APP_PRIVACY_AGREEMENT ?: "www.baidu.com"//隐私协议
        }
        mViewModel.codeLiveData.observe(this) {
            ZipCodeActivity.start(this, "234" + mViewBind.zipLoginEdit.text.toString(), it?.code
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

    override fun getData() {
    }
}