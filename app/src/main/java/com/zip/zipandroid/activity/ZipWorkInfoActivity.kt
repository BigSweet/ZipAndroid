package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.databinding.ActivityZipWorkInfoBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel

class ZipWorkInfoActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipWorkInfoBinding>() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipWorkInfoActivity::class.java)
            context.startActivity(starter)
        }
    }

    //income和time必选 Freelancer

    //comcl都必选 Employee(working in a company)
    //Employer(has employees/owns acompany)
    //Self-employed individual(business license)

    //Unemployment 下面变

    //student cl

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Personal Info")
        mViewModel.getUserInfo()
        mViewModel.getPersonInfoDic()
        mViewModel.getAllAddressInfo()
    }

    var dicInfoBean: PersonalInformationDictBean? = null
    var addressPrepare = false
    override fun createObserver() {
        mViewModel.allAddressInfo.observe(this) {
            processDataAsync(it) { result ->
                when (result) {
                    is ZipPersonInfoActivity.ProcessResult.Success -> addressPrepare = true
                    is ZipPersonInfoActivity.ProcessResult.Error -> mViewModel.getAllAddressInfo()
                }
            }
        }
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it

        }

        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_WORK)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_WORK) {
                mViewModel.saveMemberBehavior(Constants.TYPE_WORK_SURE)
            }
            if (it == Constants.TYPE_WORK_SURE) {
                //下一个界面
                dismissLoading()
                ToastUtils.showShort("finish2")
            }

        }

        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.userInfoLiveData.observe(this) {

        }


    }


    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            checkAllDone()
        }

    }


    fun checkAllDone() {
//
//        val done = mViewBind.firstNameInfoView.getEditIsComplete() &&
//                mViewBind.lastNameInfoView.getEditIsComplete() &&
//                sex != -1 &&
//                mViewBind.birthdayInfoView.isTextNotEmpty() &&
//                mViewBind.bvnInfoView.getEditIsComplete() &&
//                mViewBind.eduInfoView.isTextNotEmpty() &&
//                mViewBind.maInfoView.isTextNotEmpty() &&
//                mViewBind.numberInfoView.isTextNotEmpty() &&
//                mViewBind.emailInfoView.getEditIsComplete() &&
//                mViewBind.addressInfoView.isTextNotEmpty() &&
//                mViewBind.detailAddressInfoView.isTextNotEmpty() && !language.isNullOrEmpty()
//        mViewBind.infoNextBtn.setEnabledPlus(done)
    }


    override fun getData() {
    }
}