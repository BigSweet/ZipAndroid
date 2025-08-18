package com.zip.zipandroid.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.blankj.utilcode.util.KeyboardUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.databinding.ActivityZipLoginBinding
import com.zip.zipandroid.event.FinishLoginEvent
import com.zip.zipandroid.utils.EventBusUtils
import com.zip.zipandroid.viewmodel.ZipLoginModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ZipLoginActivity : ZipBaseBindingActivity<ZipLoginModel, ActivityZipLoginBinding>() {


    var loginSrc1Select = true
    var loginSrc2Select = true


    var phone = ""
    override fun initView(savedInstanceState: Bundle?) {
        EventBusUtils.register(this)
        mViewModel.getCode("8002233445")
//        val common_title_rl = findViewById<RelativeLayout>(R.id.common_title_rl)
//        updateToolbarTopMargin(common_title_rl)
//        mViewBind.maLoginGetCode.setOnDelayClickListener {
//            if (mViewBind.maGetPhone.text.isNullOrEmpty()) {
//                ToastUtils.showShort("Por favor introduce el número de teléfono móvil")
//                return@setOnDelayClickListener
//            }
//            if (!isTimerRunning) {
//                showLoading()
//                phone = mViewBind.maGetPhone.text.toString()
//                mViewModel.getCode(phone)
//
//                KeyboardUtils.hideSoftInput(this)
//            }
//        }
//        mViewBind.maGetPhone.setMaxLength(10)
//        mViewBind.maGetPhone.filters = arrayOf<InputFilter>(NoSpaceInputFilter())
//        mViewBind.maGetPhone.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (s.length < 10) {
//                    mViewBind.maGetCodePlace.isEnabled = false
//                    mViewBind.maLoginGetCode.setBackgroundResource(R.drawable.bg_ma_have_get_code)
//                } else {
//                    mViewBind.maGetCodePlace.isEnabled = true
//                    mViewBind.maLoginGetCode.setBackgroundResource(R.drawable.bg_ma_get_code)
//                    mViewBind.maLoginGetCode.performClick()
//                }
//
//            }
//
//        })
//        mViewBind.maGetCodePlace.isEnabled = false
//        mViewBind.maLoginGetCode.setBackgroundResource(R.drawable.bg_ma_have_get_code)
//        mViewBind.macawPrivateRl.setOnDelayClickListener {
//            CobeAndroidWebActivityCobe.start(this, Constants.commonWebUrl)
//        }
////        mViewBind.macawServiceRl.setOnDelayClickListener {
////            MacawAndroidWebActivityMacawMacaw.start(this, Constants.commonWebUrlPrivate)
////        }
//        mViewBind.macawLoginSecretIv1.setOnDelayClickListener {
//            if (loginSrc1Select) {
//                mViewBind.macawLoginSecretIv1.setImageResource(R.drawable.macaw_login_normal_icon)
//                loginSrc1Select = false
//            } else {
//                loginSrc1Select = true
//                mViewBind.macawLoginSecretIv1.setImageResource(R.drawable.macaw_login_select_icon)
//            }
//        }
//
//        mViewBind.maLoginCode.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
////                mViewBind.macawLoginSubmitTv.setEnabledPlus(s?.length == 6)
//            }
//
//        })
//
//        mViewBind.macawLoginSubmitTv.setOnDelayClickListener {
//            if (loginSrc1Select == false || loginSrc2Select == false) {
//                ToastUtils.showShort("Por favor acepta el acuerdo")
//                return@setOnDelayClickListener
//            }
//            if (mViewBind.maLoginCode.text.isNullOrEmpty()) {
//                ToastUtils.showShort("Por favor ingresa el código de verificación")
//                return@setOnDelayClickListener
//            }
//            login()
//        }
//
//        if (MMKV.defaultMMKV()?.decodeString("app_per").isNullOrEmpty()) {
//            CobePerActivity.start(this)
//        } else {
//            KeyboardUtils.showSoftInput(mViewBind.maGetPhone)
//            mViewBind.maGetPhone.isFocusable = true
//        }
    }

    fun login() {
        KeyboardUtils.hideSoftInput(this)
        showLoading()
//        mViewModel.macawLogin(phone, mViewBind.maLoginCode.text.toString())

    }


    private var isTimerRunning = false
    private val millTime: Long = 60000
    private val countDownTime: Long = 1000

    private var countDownTimer: CountDownTimer? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: FinishLoginEvent) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
        countDownTimer?.cancel()
    }

    private fun startTimer() {
//        mViewBind.maLoginGetCode.setBackgroundResource(R.drawable.bg_ma_have_get_code)
//
//        countDownTimer = object : CountDownTimer(millTime, countDownTime) {
//            override fun onTick(millisUntilFinished: Long) {
//                val secondsRemaining = millisUntilFinished / 1000
//                mViewBind.maGetCodePlace.text = "${secondsRemaining}s"
//                mViewBind.maGetCodePlace.isEnabled = false
//                mViewBind.maGetCodePlace.setTextColor(resources.getColor(R.color.c73000000))
//            }
//
//            override fun onFinish() {
//                mViewBind.maGetCodePlace.setTextColor(resources.getColor(R.color.cFF9B53))
//                mViewBind.maLoginGetCode.setBackgroundResource(R.drawable.bg_ma_get_code)
//                mViewBind.maGetCodePlace.text = getString(R.string.code_sms_str)
//                isTimerRunning = false
//                mViewBind.maGetCodePlace.isEnabled = true
//            }
//        }

        countDownTimer?.start()
        isTimerRunning = true
    }

    override fun createObserver() {
//        mViewModel.loginLiveData.observe(this) {
//            dismissLoading()
//            if (it != null) {
//                if (it.getafricannecessaryargument == "1") {
//                    //去一个新手页面
//                    UserInfo.getInstance().setNewUser(it.getafricannecessaryargument)
//                    startActivity(CobeNewUserGuideActivity::class.java)
//                } else {
//                    startActivity(CobeMainActivityCobe::class.java)
//                }
//                finish()
//
//            }
//
//        }
//
//        mViewModel.codeLiveData.observe(this) {
//            dismissLoading()
//            it ?: return@observe
//
//            KeyboardUtils.showSoftInput(mViewBind.maLoginCode)
//            mViewBind.maLoginCode.isFocusable = true
//
//            startTimer()
//            mViewBind.maLoginCode.setText(it.spellingbrainpole)
//        }
    }

    override fun getData() {
    }
}