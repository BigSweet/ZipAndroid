package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.blankj.utilcode.util.KeyboardUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.ZipMainActivity
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipCodeBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.view.VerificationAction
import com.zip.zipandroid.viewmodel.ZipLoginModel

class ZipCodeActivity : ZipBaseBindingActivity<ZipLoginModel, ActivityZipCodeBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context, phone: String, code: String) {
            val starter = Intent(context, ZipCodeActivity::class.java)
                .putExtra("phone", phone)
                .putExtra("code", code)
            context.startActivity(starter)
        }

    }

    var code = ""
    var phone = ""
    override fun initView(savedInstanceState: Bundle?) {
        phone = intent.getStringExtra("phone") ?: ""
        code = intent.getStringExtra("code") ?: ""
        mViewBind.codePhoneTv.setText(maskDigits("+$phone"))
        mViewBind.zipCodeEdit.setOnVerificationCodeChangedListener(onVerificationCodeChangedListener)
        startTimer()
        mViewBind.codeSendTv.setOnDelayClickListener {
            startTimer()
        }
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }

        KeyboardUtils.showSoftInput(mViewBind.zipCodeEdit)
    }

    fun maskDigits(input: String): String {
        if (input.length < 6) return input // 不足6位不处理（或抛异常）
        val chars = input.toCharArray()
        for (i in chars.size - 6 until chars.size - 2) { // 倒数6~3位（索引范围）
            chars[i] = '*'
        }
        return String(chars)
    }


    var onVerificationCodeChangedListener: VerificationAction.OnVerificationCodeChangedListener = object : VerificationAction.OnVerificationCodeChangedListener {
        override fun onVerCodeChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mViewBind.zipCodeEdit.setUnSelectedBackgroundColor(R.color.cF1F5FF)
        }

        override fun onInputCompleted(s: CharSequence) {
            //直接去首页
            showLoading()
            mViewModel.zipLogin(phone, s.toString())
        }

        override fun onInputClear() {
            mViewBind.zipCodeEdit.setUnSelectedBackgroundColor(R.color.cF1F5FF)
        }
    }


    private var isTimerRunning = false
    private val millTime: Long = 60000
    private val countDownTime: Long = 1000

    private var countDownTimer: CountDownTimer? = null

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(millTime, countDownTime) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val span = SpannableStringBuilder()
                span.append("Resend after ")
                val start = span.length
                span.append("${secondsRemaining}s")
                val end = span.length
                span.setSpan(ForegroundColorSpan(Color.parseColor("#FF3667F0")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                mViewBind.codeSendTv.text = span
                mViewBind.codeSendTv.isEnabled = false
            }

            override fun onFinish() {
                val span = SpannableStringBuilder()
                val start = span.length
                span.append("Resend")
                val end = span.length
                span.setSpan(ForegroundColorSpan(Color.parseColor("#FF3667F0")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                mViewBind.codeSendTv.text = span
                mViewBind.codeSendTv.isEnabled = true
            }
        }

        countDownTimer?.start()
        isTimerRunning = true
    }

    override fun createObserver() {
        mViewModel.loginLiveData.observe(this) {
            //登录成功
            //去首页
            KeyboardUtils.hideSoftInput(mViewBind.zipCodeEdit)
            dismissLoading()
            UserInfoUtils.setSignKey(it?.staticKey ?: "")
            UserInfoUtils.setMid(it?.mid ?: 0)
            UserInfoUtils.setUserNo(it?.userNo ?: "")
            startActivity(ZipMainActivity::class.java)
            if (it.isRegister == 1) {
                mViewModel.saveMemberBehavior(Constants.TYPE_REGISTER)
            } else {
                mViewModel.saveMemberBehavior(Constants.TYPE_LOGIN)
            }
            finish()
        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
            mViewBind.zipCodeEdit.setUnSelectedBackgroundColor(R.color.cFFECEC)
        }
    }

    override fun getData() {
    }
}