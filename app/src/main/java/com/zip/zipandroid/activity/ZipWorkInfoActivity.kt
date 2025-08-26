package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.AddressUploadBean
import com.zip.zipandroid.bean.PersonalInformationDictBean
import com.zip.zipandroid.databinding.ActivityZipWorkInfoBinding
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.pop.SingleCommonSelectPop
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.ZipStringUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import java.util.Calendar

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

        mViewModel.getPersonInfoDic()
        mViewModel.getAllAddressInfo()
        mViewBind.occInfoInfoView.infoViewClick = {
            //职业
            showSelectPop("Occupation", dicInfoBean?.industry, SingleCommonSelectPop.occ_type, industry, mViewBind.occInfoInfoView)
        }
        mViewBind.empStatusInfoView.infoViewClick = {
            //就业状态
            showSelectPop("Employment Status", dicInfoBean?.employmentStatus, SingleCommonSelectPop.emp_status_type, emp_status, mViewBind.empStatusInfoView)

        }

        focusChangeCheck(mViewBind.incomeInfoView)
        focusChangeCheck(mViewBind.timeWorkInfoView)
        focusChangeCheck(mViewBind.occInfoInfoView)
        focusChangeCheck(mViewBind.empStatusInfoView)
        focusChangeCheck(mViewBind.companyNameInfoView)
        focusChangeCheck(mViewBind.companyAddressInfoView)
        focusChangeCheck(mViewBind.detailWorkInfoView)
        focusChangeCheck(mViewBind.payDayView)
        mViewBind.companyAddressInfoView.infoViewClick = {
            if (addressPrepare) {
                showAddressPickerView(object : ((String, String, String) -> Unit) {
                    override fun invoke(opt1tx: String, opt2tx: String, opt3tx: String) {
                        val tx = "$opt1tx $opt2tx $opt3tx"
                        addressUploadBean.state = opt1tx
                        addressUploadBean.town = opt2tx
                        addressUploadBean.area = opt3tx
                        mViewBind.companyAddressInfoView.setContentText(tx)
                        mViewBind.companyAddressInfoView.setTagComplete()
                    }
                })
            } else {
                ToastUtils.showShort("Data preparationp")
            }
        }
        mViewBind.timeWorkInfoView.infoViewClick = {
            showBirthDayPickView("The time work begins") {
                val calendar = it
                val day = calendar[Calendar.DATE]
                val realDay = ZipStringUtils.addZero(day)
                val month = ZipStringUtils.addZero(calendar[Calendar.MONTH] + 1)
                val year = calendar.get(Calendar.YEAR)
                val dateStr = "$year-$month-$realDay"
                val dateTime = calendar.time.time
                Log.d("选的日期", "$year-$month-$realDay" + "数字时间" + dateTime)
                mViewBind.timeWorkInfoView.setContentText(dateStr)
                mViewBind.timeWorkInfoView.setTagComplete()
            }
        }
        mViewBind.infoNextBtn.setOnDelayClickListener {
            if (currentType == type_company || currentType == type_free) {
                //保存com的数据
                addressUploadBean.detail = mViewBind.detailWorkInfoView.getEditText()
                mViewModel.saveCompanyInfo(industry, mViewBind.occInfoInfoView.getEditText(), emp_status, mViewBind.companyNameInfoView.getEditText(), addressUploadBean, mViewBind.detailWorkInfoView.getEditText(), mViewBind.payDayView.getEditText(), mViewBind.incomeInfoView.getRawNumericValue(), mViewBind.timeWorkInfoView.getEditText())
            }
            if (currentType == type_ume) {

            }
        }
        mViewModel.saveWorkNomralLiveData.observe(this) {
            //公司信息
        }

    }

    var addressUploadBean = AddressUploadBean("", "", "", "")
    var industry = 0//职业下标
    var industryName = ""//职业名字
    var emp_status = 0//就业状态

    fun checkFreeDone() {
        val done = mViewBind.incomeInfoView.getEditIsComplete() &&
                mViewBind.timeWorkInfoView.getEditIsComplete() &&
                mViewBind.occInfoInfoView.getEditIsComplete() &&
                mViewBind.empStatusInfoView.getEditIsComplete()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }

    fun checkNormalDone() {
        val done = mViewBind.incomeInfoView.isTextNotEmpty() &&
                mViewBind.timeWorkInfoView.isTextNotEmpty() &&
                mViewBind.occInfoInfoView.getEditIsComplete() &&
                mViewBind.empStatusInfoView.getEditIsComplete() &&
                mViewBind.companyNameInfoView.getEditIsComplete() &&
                mViewBind.companyAddressInfoView.isTextNotEmpty() &&
                mViewBind.detailWorkInfoView.getEditIsComplete() &&
                mViewBind.payDayView.getEditIsComplete()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }

    var currentType = -1
    val type_free = 1
    val type_company = 0
    val type_ume = 2
    val type_student = 3
    fun showSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int, infoView: SetInfoEditView) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        showSelectPop(title, data, type, selectPosition, infoView, object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                if (type == SingleCommonSelectPop.emp_status_type) {

                    emp_status = position
                    //检测显示哪些
                    if (tv == "Employee (working in a company)" || tv == "Freelancer/Street Vendor" || tv == "Employer (has employees/owns a company)" || tv == "Self-employed Individual (business license)") {
                        mViewBind.companyCl.show()
                        mViewBind.studentCl.hide()
                        mViewBind.umeCl.hide()
                        if (tv == "Freelancer/Street Vendor") {
                            currentType = type_free
                            mViewBind.companyNameInfoView.warSetX(false)
                            mViewBind.companyAddressInfoView.warSetX(false)
                            mViewBind.detailWorkInfoView.warSetX(false)
                            mViewBind.payDayView.warSetX(false)
                            mViewBind.incomeInfoView.warSetX(true)
                            mViewBind.timeWorkInfoView.warSetX(true)
                            // //com必填项变更
//                            Monthly Income
//                                    The time work begins
                        } else {
                            currentType = type_company
                            mViewBind.companyNameInfoView.warSetX(true)
                            mViewBind.companyAddressInfoView.warSetX(true)
                            mViewBind.detailWorkInfoView.warSetX(true)
                            mViewBind.payDayView.warSetX(true)
                            mViewBind.incomeInfoView.warSetX(true)
                            mViewBind.timeWorkInfoView.warSetX(true)
                            //com所有必选

//                            Monthly Income
//                                    The time work begins
                        }
                    }
                    if (tv == "Unemployment") {
                        currentType = type_ume
                        mViewBind.companyCl.hide()
                        mViewBind.studentCl.hide()
                        mViewBind.umeCl.show()
                    }
                    if (tv == "Student") {
                        currentType = type_student
                        mViewBind.companyCl.hide()
                        mViewBind.studentCl.show()
                        mViewBind.umeCl.hide()
                    }
                }
                if (type == SingleCommonSelectPop.occ_type) {
                    industry = position
                }

                checkAllDone()
            }
        })
    }


    var dicInfoBean: PersonalInformationDictBean? = null
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
        mViewModel.personDicLiveData.observe(this) {
            dicInfoBean = it
            mViewModel.getUserInfo()
        }

        mViewModel.saveWorkNomralLiveData.observe(this) {
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

            if (it.industry > -1) {
                industry = it.industry
                industryName = it.industryName
                mViewBind.occInfoInfoView.setContentText(industryName)
            }
            if (it.employmentStatus > -1) {
                emp_status = it.employmentStatus
                dicInfoBean?.employmentStatus?.get(it.employmentStatus)?.let { it1 -> mViewBind.empStatusInfoView.setContentText(it1) }
                if (it.employmentStatus in 0..2) {

                }
                if(it.employmentStatus==3){

                }

            }

        }


    }


    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            if (currentType == type_company) {
                checkNormalDone()
            }
            if (currentType == type_free) {
                checkFreeDone()
            }
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