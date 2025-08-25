package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zip.zipandroid.adapter.ZipPersonInfoEmailAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.databinding.ActivityZipPersonInfoBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import java.util.Locale

class ZipPersonInfoActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipPersonInfoBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipPersonInfoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Personal Info")
        mViewBind.firstNameInfoView.showBoard()
        mViewModel.getUserInfo()
        mViewModel.getPersonInfoDic()
        mViewBind.recommendEmailRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        mViewBind.recommendEmailRv.adapter = emailAdapter
        emailAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as String
            mViewBind.emailInfoView.appendText(item)
        }

        val systemLocale = Locale.getDefault()
        language = systemLocale.language
        if (!language?.isNullOrEmpty()) {
            mViewBind.langInfoView.hide()
        } else {
            mViewBind.langInfoView.show()
        }
        mViewBind.bvnInfoView.setContentText("22298656042")
//        mViewModel.checkBvn("22298656042")
    }

    val emailAdapter = ZipPersonInfoEmailAdapter()

    var dicInfoBean: PersonalInformationDictBean? = null
    var currentIdeImg = ""
    override fun createObserver() {
        mViewModel.uploadImgLiveData.observe(this) {
            Log.d("获取bvn图片成功", it)
            currentIdeImg = it
        }
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
            if (!it.emailSuffix.isNullOrEmpty()) {
                //email
                emailAdapter.setNewData(it.emailSuffix)
            }
        }
        mViewModel.bvnInfoLiveData.observe(this) {
            if (!it.photo.isNullOrEmpty()) {
                //去换成图片地址
                mViewModel.getPhotoUrlByBase(it.photo)
            }
        }
        mViewModel.userInfoLiveData.observe(this) {
            val span = SpannableStringBuilder()
            if (!it.firstName.isNullOrEmpty()) {
                mViewBind.firstNameInfoView.setContentText(it.firstName)
                span.append(it.firstName)
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
                if (it.sex == "1") {
                    clickMale()
                } else if (it.sex == "0") {
                    clickFeMale()
                }
            }
            if (!it.birthDateStr.isNullOrEmpty()) {
                brithDayStr = it.birthDateStr
                brithDay = it.birthDate
                mViewBind.birthdayInfoView.setContentText(it.birthDateStr)
            }
            if (!it.identity.isNullOrEmpty()) {
                mViewBind.bvnInfoView.setContentText(it.identity)
            }
            if (it.degree > 0) {
                //学历
                dicInfoBean?.degree?.get(it.degree - 1)?.let { it1 -> mViewBind.eduInfoView.setContentText(it1) }
                degree = it.degree
            }
            if (it.marry > 0) {
                dicInfoBean?.marry?.get(it.marry - 1)?.let { it1 -> mViewBind.maInfoView.setContentText(it1) }
                marry = it.marry
            }
            if (it.childrens > 0) {
                childrens = it.childrens
                dicInfoBean?.childrens?.get(it.childrens - 1)?.let { it1 -> mViewBind.numberInfoView.setContentText(it1) }
            }
            if (!it.mbEmail.isNullOrEmpty()) {
                mbEmail = it.mbEmail
                mViewBind.emailInfoView.setContentText(it.mbEmail)
            }
            if (!it.idAddress.isNullOrEmpty()) {
                mViewBind.addressInfoView.setContentText(it.idAddress)
            }
            if (!it.nowAddress.isNullOrEmpty()) {
                mViewBind.detailAddressInfoView.setContentText(it.nowAddress)
            }
            if (it.language > 0) {
                languageIndex = it.language
                dicInfoBean?.language?.get(it.language - 1)?.let { it1 -> mViewBind.langInfoView.setContentText(it1) }
            }

        }
        mViewBind.infoFemaleTv.setOnDelayClickListener {
            clickFeMale()
        }
        mViewBind.infoMaleTv.setOnDelayClickListener {
            clickMale()
        }
    }

    fun focusChangeCheck(){


    }
    fun showSelectPop(){
        //选择完成后检测
    }

    fun clickFeMale() {
        checkAllDone()
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 0

    }

    fun checkAllDone() {

        val done = mViewBind.firstNameInfoView.getEditIsComplete() &&
                mViewBind.middleNameInfoView.getEditIsComplete() &&
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

    fun clickMale() {
        checkAllDone()
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 1
    }

    var brithDayStr = ""
    var mbEmail = ""
    var realName = ""
    var brithDay = 0L
    var sex = -1
    var degree = 0
    var marry = 0
    var childrens = 0
    var language = ""
    var languageIndex = 0

    override fun getData() {
    }
}