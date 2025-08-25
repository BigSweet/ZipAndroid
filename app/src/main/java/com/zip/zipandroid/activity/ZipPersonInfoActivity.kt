package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zip.zipandroid.adapter.ZipPersonInfoEmailAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.base.ZipBaseViewModel
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.databinding.ActivityZipPersonInfoBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import java.util.Locale

class ZipPersonInfoActivity : ZipBaseBindingActivity<ZipBaseViewModel, ActivityZipPersonInfoBinding>() {
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
        val language = systemLocale.language
        if (!language.isNullOrEmpty()) {
            mViewBind.langInfoView.hide()
        } else {
            mViewBind.langInfoView.show()
        }
    }

    val emailAdapter = ZipPersonInfoEmailAdapter()

    var dicInfoBean: PersonalInformationDictBean? = null
    override fun createObserver() {
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
            if (!it.emailSuffix.isNullOrEmpty()) {
                //email
                emailAdapter.setNewData(it.emailSuffix)
            }
        }
        mViewModel.userInfoLiveData.observe(this) {
            if (!it.firstName.isNullOrEmpty()) {
                mViewBind.firstNameInfoView.setContentText(it.firstName)
            }
            if (!it.midName.isNullOrEmpty()) {
                mViewBind.middleNameInfoView.setContentText(it.midName)
            }
            if (!it.lastName.isNullOrEmpty()) {
                mViewBind.lastNameInfoView.setContentText(it.lastName)
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
                dicInfoBean?.degree?.get(it.marry - 1)?.let { it1 -> mViewBind.maInfoView.setContentText(it1) }
                marry = it.marry
            }
            if (it.childrens > 0) {
                childrens = it.childrens
                dicInfoBean?.degree?.get(it.childrens - 1)?.let { it1 -> mViewBind.numberInfoView.setContentText(it1) }
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
                language = it.language
                dicInfoBean?.relation?.get(it.language - 1)?.let { it1 -> mViewBind.langInfoView.setContentText(it1) }
            }

        }
        mViewBind.infoFemaleTv.setOnDelayClickListener {
            clickFeMale()
        }
        mViewBind.infoMaleTv.setOnDelayClickListener {
            clickMale()
        }
    }

    fun clickFeMale() {
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 0

    }

    fun clickMale() {
        mViewBind.infoMaleTv.setBackground(Color.parseColor("#F1F5FF"))
        mViewBind.infoMaleTv.setTextColor(Color.parseColor("#3667F0"))
        mViewBind.infoFemaleTv.setBackground(Color.parseColor("#F7F7F7"))
        mViewBind.infoFemaleTv.setTextColor(Color.parseColor("#000000"))
        sex = 1
    }

    var brithDayStr = ""
    var mbEmail = ""
    var brithDay = 0L
    var sex = 0
    var degree = 0
    var marry = 0
    var childrens = 0
    var language = 0

    override fun getData() {
    }
}