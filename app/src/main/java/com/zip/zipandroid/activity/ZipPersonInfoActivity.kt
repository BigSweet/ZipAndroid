package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.zip.zipandroid.adapter.SingleButtonAdapter
import com.zip.zipandroid.adapter.ZipPersonInfoEmailAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.AddressUploadBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.bean.PhotoPathBean
import com.zip.zipandroid.bean.ZipIndImgBean
import com.zip.zipandroid.databinding.ActivityZipPersonInfoBinding
import com.zip.zipandroid.event.ZipFinishInfoEvent
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.UserInfoUtils
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.utils.ZipStringUtils
import com.zip.zipandroid.utils.ZipTrackUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Calendar
import java.util.Locale

class ZipPersonInfoActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipPersonInfoBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPersonInfoActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipFinishInfoEvent) {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ZipTrackUtils.track("OutPersonalInfo")
    }
    override fun onDestroy() {
        super.onDestroy()
        ZipEventBusUtils.unregister(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZipEventBusUtils.register(this)
        ZipTrackUtils.track("InPersonalInfo")
    }


    var singleButtonAdapter = SingleButtonAdapter()
    var addressUploadBean = AddressUploadBean("", "", "", "")
    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            ZipTrackUtils.track("OutPersonalInfo")
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Personal Info")


        mViewBind.recommendEmailRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        mViewBind.recommendEmailRv.adapter = emailAdapter
        emailAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as String
            mViewBind.emailInfoView.appendText("@" + item)
        }

        mViewBind.sexRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mViewBind.sexRv.adapter = singleButtonAdapter
        singleButtonAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            singleButtonAdapter.selectPosition = i
            sex = i
            singleButtonAdapter.notifyDataSetChanged()
            checkAllDone()
        }
        val systemLocale = Locale.getDefault()
        language = systemLocale.language
        if (!language?.isNullOrEmpty()) {
            mViewBind.langInfoView.hide()
        } else {
            mViewBind.langInfoView.show()
        }
//        mViewBind.bvnInfoView.setContentText("22298656042")
//        mViewModel.checkBvn("22298656042")
        mViewBind.eduInfoView.infoViewClick = {
            showSelectPop("Education", dicInfoBean?.degree, SingleCommonSelectPop.edu_type, degree, mViewBind.eduInfoView)
        }
        mViewBind.maInfoView.infoViewClick = {
            showSelectPop("Marital Status", dicInfoBean?.marry, SingleCommonSelectPop.ma_type, marry, mViewBind.maInfoView)
        }
        mViewBind.numberInfoView.infoViewClick = {
            showSelectPop("Number of Children", dicInfoBean?.childrens, SingleCommonSelectPop.child_type, childrens, mViewBind.numberInfoView)
        }
        mViewBind.langInfoView.infoViewClick = {
            showSelectPop("Languages", dicInfoBean?.language, SingleCommonSelectPop.la_type, languageIndex, mViewBind.langInfoView)
        }
        mViewBind.birthdayInfoView.infoViewClick = {
            //showbrith

            showBirthDayPickView("Date of Birth") {
                val calendar = it
                age = calculateAge(calendar)
                val day = calendar[Calendar.DATE]
                val realDay = ZipStringUtils.addZero(day)
                val month = ZipStringUtils.addZero(calendar[Calendar.MONTH] + 1)
                val year = calendar.get(Calendar.YEAR)
                brithDayStr = "$year-$month-$realDay"
                brithDay = calendar.time.time

                Log.d("选的日期", "$year-$month-$realDay" + "数字时间" + brithDay)
                mViewBind.birthdayInfoView.setContentText(brithDayStr)
                mViewBind.birthdayInfoView.setTagComplete()
            }
        }
        mViewBind.addressInfoView.infoViewClick = {
            if (addressPrepare) {
                showAddressPickerView(object : ((String, String, String) -> Unit) {
                    override fun invoke(opt1tx: String, opt2tx: String, opt3tx: String) {
                        val tx = "$opt1tx $opt2tx $opt3tx"
                        addressUploadBean.state = opt1tx
                        addressUploadBean.town = opt2tx
                        addressUploadBean.area = opt3tx
                        mViewBind.addressInfoView.setContentText(tx)
                        mViewBind.addressInfoView.setTagComplete()
                    }
                })
            } else {
                ToastUtils.showShort("Data preparationp")
            }

        }

        mViewModel.getUserInfo()
        mViewModel.getPersonInfoDic()
        mViewModel.getAllAddressInfo()

        focusChangeCheck(mViewBind.firstNameInfoView)
        focusChangeCheck(mViewBind.lastNameInfoView)
        focusChangeCheck(mViewBind.bvnInfoView)
        focusChangeCheck(mViewBind.emailInfoView)
        focusChangeCheck(mViewBind.detailAddressInfoView)
        mViewBind.emailInfoView.scrollListener = {
            // 平滑滚动到底部
            mViewBind.perInfoScroll.postDelayed({
                // 计算Y轴要滚动的目标位置：内容总高度 - ScrollView的可见高度
                mViewBind.perInfoScroll.smoothScrollBy(0, 150)
            }, 200)
        }
        mViewBind.infoNextBtn.setOnDelayClickListener {
            showLoading()
            mViewModel.checkBvn(mViewBind.bvnInfoView.getEditText())
            ZipTrackUtils.track("SubmitPersonalInfo")
        }
    }

    val emailAdapter = ZipPersonInfoEmailAdapter()

    var dicInfoBean: PersonalInformationDictBean? = null
    var currentIdeImg = ""
    var servicePath = ""


    var addressPrepare = false
    override fun createObserver() {
        mViewModel.allAddressInfo.observe(this) {
            processDataAsync(it) { result ->
                when (result) {
                    is ProcessResult.Success -> addressPrepare = true
                    is ProcessResult.Error -> mViewModel.getAllAddressInfo()
                }
            }
        }
        mViewModel.realNameInfoLiveData.observe(this) {
            //更新用户信息
            val imgBean = ZipIndImgBean(PhotoPathBean(currentIdeImg), PhotoPathBean(servicePath))
            addressUploadBean.detail = mViewBind.detailAddressInfoView.getEditText()
            mViewModel.saveUserInfo(age, brithDay, brithDayStr, mViewBind.eduInfoView.getEditText(), degree, mViewBind.bvnInfoView.getEditText(),
                imgBean, mViewBind.emailInfoView.getEditText(), UserInfoUtils.getUserPhone(), "1", mViewBind.detailAddressInfoView.getEditText(), addressUploadBean, sex, marry, childrens, language, it.custId,
                mViewBind.firstNameInfoView.getEditText(), mViewBind.middleNameInfoView.getEditText(), mViewBind.lastNameInfoView.getEditText())
        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_REAL)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_REAL) {
                mViewModel.saveMemberBehavior(Constants.TYPE_ADDRESS)
            }
            if (it == Constants.TYPE_ADDRESS) {
                //下一个界面
                dismissLoading()
                startActivity(ZipWorkInfoActivity::class.java)
//                ToastUtils.showShort("finish")
            }

        }
        mViewModel.uploadImgLiveData.observe(this) {
            Log.d("获取bvn图片成功", it)
            currentIdeImg = it
            //实名
            mViewModel.realName(mViewBind.bvnInfoView.getEditText(), brithDay, brithDayStr, mViewBind.firstNameInfoView.getEditText(), mViewBind.middleNameInfoView.getEditText(), mViewBind.lastNameInfoView.getEditText(), sex)

        }

        mViewModel.servicePathLiveData.observe(this) {
            servicePath = it
        }
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
            if (!it.emailSuffix.isNullOrEmpty()) {
                //email
                emailAdapter.setNewData(it.emailSuffix)
            }
            if (!it.gender.isNullOrEmpty()) {
                singleButtonAdapter.setNewData(it.gender)
            }
        }
        mViewModel.bvnInfoLiveData.observe(this) {
            if (!it?.photo.isNullOrEmpty()) {
                //去换成图片地址
                mViewModel.getPhotoUrlByBase(it?.photo ?: "")
            } else {
                dismissLoading()
                ToastUtils.showShort("bvn error")
            }
        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.userInfoLiveData.observe(this) {
            val span = SpannableStringBuilder()
            if (!it.firstName.isNullOrEmpty()) {
                mViewBind.firstNameInfoView.setContentText(it.firstName)
                span.append(it.firstName)
            } else {
                mViewBind.firstNameInfoView.showBoard()
            }
            if (!it.midName.isNullOrEmpty()) {
                mViewBind.middleNameInfoView.setContentText(it.midName)
                span.append(" " + it.midName)
            }
            if (!it.lastName.isNullOrEmpty()) {
                mViewBind.lastNameInfoView.setContentText(it.lastName)
                span.append(" " + it.lastName)
            }
            if (span.length > 0) {
                realName = span.toString()
            }
            //0和1
            if (!it.sex.isNullOrEmpty()) {
                //0 女 1 男
                singleButtonAdapter.selectPosition = it.sex.toInt()
                singleButtonAdapter.notifyDataSetChanged()
            }
            if (it.birthDate > 0) {
                brithDayStr = formatTimestamp(it.birthDate)
                brithDay = it.birthDate
                mViewBind.birthdayInfoView.setContentText(brithDayStr)
            }
            if (!it.identity.isNullOrEmpty()) {
                mViewBind.bvnInfoView.setContentText(it.identity)
            }
            if ((it.degree ?: -1) >= 0) {
                //学历
                dicInfoBean?.degree?.get(it.degree
                    ?: -1)?.let { it1 -> mViewBind.eduInfoView.setContentText(it1) }
                degree = it.degree ?: -1
            }
            if ((it.marry ?: -1) >= 0) {
                dicInfoBean?.marry?.get(it.marry
                    ?: -1)?.let { it1 -> mViewBind.maInfoView.setContentText(it1) }
                marry = it.marry ?: -1
            }
//            if(it.identity.isNullOrEmpty()){
//
//            }
            if ((it.childrens ?: -1) >= 0) {
                childrens = (it.childrens ?: -1)
                dicInfoBean?.childrens?.get((it.childrens
                    ?: -1))?.let { it1 -> mViewBind.numberInfoView.setContentText(it1) }
            }
            if (!it.mbEmail.isNullOrEmpty()) {
                mbEmail = it.mbEmail
                mViewBind.emailInfoView.setContentText(it.mbEmail)
            }
            if (!it.postalInfo.isNullOrEmpty()) {
                addressUploadBean = Gson().fromJson(it.postalInfo, AddressUploadBean::class.java)
                val opt1tx = addressUploadBean.state
                val opt2tx = addressUploadBean.town
                val opt3tx = addressUploadBean.area
                val tx = "$opt1tx $opt2tx $opt3tx"
                mViewBind.addressInfoView.setContentText(tx)
            }
            if (!it.nowAddress.isNullOrEmpty()) {
                mViewBind.detailAddressInfoView.setContentText(it.nowAddress)
            }
            if ((it.language ?: -1) >= 0) {
                languageIndex = (it.language ?: -1)
                dicInfoBean?.language?.get((it.language
                    ?: -1))?.let { it1 -> mViewBind.langInfoView.setContentText(it1) }
            }

            if (!it.mbPhone.isNullOrEmpty()) {
                UserInfoUtils.setUserPhone(it.mbPhone)
            }
//            if(!it.postalInfo.isNullOrEmpty()){
//
//            }
        }


    }


    fun calculateAge(birthday: Calendar): Int {
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR)

        // 检查是否已过生日
        if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH))
        ) {
            age--  // 如果今年还没过生日，年龄减1
        }
        return age
    }


    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            checkAllDone()
        }

    }

    fun showSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int, infoView: SetInfoEditView) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        showSelectPop(title, data, type, selectPosition, infoView, object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                if (type == SingleCommonSelectPop.edu_type) {
                    degree = position
                }
                if (type == SingleCommonSelectPop.ma_type) {
                    marry = position
                }
                if (type == SingleCommonSelectPop.child_type) {
                    childrens = position
                }
                if (type == SingleCommonSelectPop.la_type) {
                    languageIndex = position
                }
                infoView.setTagComplete()
                checkAllDone()
            }
        })
    }


    fun checkAllDone() {

        val done = mViewBind.firstNameInfoView.getEditIsComplete() &&
                mViewBind.lastNameInfoView.getEditIsComplete() &&
                sex != -1 &&
                mViewBind.birthdayInfoView.isTextNotEmpty() &&
                mViewBind.bvnInfoView.getEditIsComplete() &&
                mViewBind.eduInfoView.isTextNotEmpty() &&
                mViewBind.maInfoView.isTextNotEmpty() &&
                mViewBind.numberInfoView.isTextNotEmpty() &&
                mViewBind.emailInfoView.getEditIsComplete() &&
                mViewBind.addressInfoView.isTextNotEmpty() &&
                mViewBind.detailAddressInfoView.isTextNotEmpty() && !language.isNullOrEmpty()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }


    var brithDayStr = ""
    var mbEmail = ""
    var realName = ""
    var brithDay = 0L
    var sex = -1
    var degree = 0
    var age = 0
    var marry = 0
    var childrens = 0
    var language = ""
    var languageIndex = 0

    override fun getData() {
    }
}