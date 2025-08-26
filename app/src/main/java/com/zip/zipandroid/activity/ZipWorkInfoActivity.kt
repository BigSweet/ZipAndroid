package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.zip.zipandroid.adapter.SingleButtonAdapter
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

        //ume
        focusChangeCheck(mViewBind.lengthOfUmView)
        focusChangeCheck(mViewBind.umeIncomeView)
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

        mViewBind.umeRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mViewBind.umeRv.adapter = singleButtonAdapter
        singleButtonAdapter.setOnItemChildClickListener { baseQuickAdapter, view, i ->
            singleButtonAdapter.selectPosition = i
            ontherIncome = i
            singleButtonAdapter.notifyDataSetChanged()
            checkDoneByType()
        }

        mViewBind.infoNextBtn.setOnDelayClickListener {
            if (currentType == type_company || currentType == type_free) {
                //保存com的数据
                addressUploadBean.detail = mViewBind.detailWorkInfoView.getEditText()
                mViewModel.saveCompanyInfo(industry, mViewBind.occInfoInfoView.getEditText(), emp_status, mViewBind.companyNameInfoView.getEditText(), addressUploadBean, mViewBind.detailWorkInfoView.getEditText(), mViewBind.payDayView.getEditText(), mViewBind.incomeInfoView.getRawNumericValue(), mViewBind.timeWorkInfoView.getEditText())
            }
            if (currentType == type_ume) {
                mViewModel.saveWorkUmeInfo(industry, mViewBind.occInfoInfoView.getEditText(), emp_status, ontherIncome, mViewBind.lengthOfUmView.getEditText(), mViewBind.umeIncomeView.getRawNumericValue())
            }
            if (currentType == type_student) {
                mViewModel.saveStudentInfo(industry, mViewBind.occInfoInfoView.getEditText(), emp_status,  mViewBind.schoolNameInfoView.getEditText(), addressUploadBean,mViewBind.schoolDetailNameInfoView.getEditText(), mViewBind.schoolIncomeInfoView.getRawNumericValue(), mViewBind.schoolTimeWorkInfoView.getEditText())
            }
        }

    }


    var singleButtonAdapter = SingleButtonAdapter()


    var ontherIncome = -1
    var lengthOfUnemployment = ""

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

    fun checkUmeDone() {
        val done = mViewBind.umeIncomeView.getEditIsComplete() &&
                mViewBind.timeWorkInfoView.getEditIsComplete() &&
                mViewBind.lengthOfUmView.getEditIsComplete() &&
                ontherIncome != -1 &&
                mViewBind.empStatusInfoView.getEditIsComplete()
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }

    fun checkStudentDone(){
        val done = mViewBind.schoolIncomeInfoView.isTextNotEmpty() &&
                mViewBind.schoolTimeWorkInfoView.isTextNotEmpty() &&
                mViewBind.occInfoInfoView.getEditIsComplete() &&
                mViewBind.empStatusInfoView.getEditIsComplete() &&
                mViewBind.schoolNameInfoView.getEditIsComplete() &&
                mViewBind.schoolAddressInfoView.isTextNotEmpty() &&
                mViewBind.schoolDetailNameInfoView.getEditIsComplete()
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
                    checkEmpStatus(position)

                }
                if (type == SingleCommonSelectPop.occ_type) {
                    industry = position
                }

                checkAllDone()
            }
        })
    }

    private fun checkEmpStatus(position: Int) {
        if (position == 4) {
            currentType = type_ume
            mViewBind.companyCl.hide()
            mViewBind.studentCl.hide()
            mViewBind.umeCl.show()
        }
        if (position == 5) {
            currentType = type_student
            mViewBind.companyCl.hide()
            mViewBind.studentCl.show()
            mViewBind.umeCl.hide()
        }

        if (position in 0..3) {
            mViewBind.companyCl.show()
            mViewBind.studentCl.hide()
            mViewBind.umeCl.hide()
            if (position == 3) {
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
            checkDoneByType()
        }
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
            if(!it.ontherIncome.isNullOrEmpty()){
                singleButtonAdapter.setNewData(it.ontherIncome)
            }
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

            if ((it.industry ?: -1) > -1) {
                industry = it.industry ?: -1
                industryName = it.industryName
                mViewBind.occInfoInfoView.setContentText(industryName)
                mViewBind.occInfoInfoView.setTagComplete()
            }
            if ((it.employmentStatus ?: -1) > -1) {
                emp_status = (it.employmentStatus ?: -1)
                dicInfoBean?.employmentStatus?.get((it.employmentStatus
                    ?: -1))?.let { it1 -> mViewBind.empStatusInfoView.setContentText(it1) }
                checkEmpStatus((it.employmentStatus ?: -1))
                mViewBind.empStatusInfoView.setTagComplete()
            }
            if (!it.companyName.isNullOrEmpty()
            ) {
                mViewBind.companyNameInfoView.setContentText(it.companyName)
                mViewBind.companyNameInfoView.setTagComplete()
            }

            if (!it.companyLocation.isNullOrEmpty()) {
                addressUploadBean = Gson().fromJson(it.companyLocation, AddressUploadBean::class.java)
                val opt1tx = addressUploadBean.state
                val opt2tx = addressUploadBean.town
                val opt3tx = addressUploadBean.area
                val tx = "$opt1tx $opt2tx $opt3tx"
                mViewBind.companyAddressInfoView.setContentText(tx)
                mViewBind.companyAddressInfoView.setTagComplete()
            }
            if (!it.companyDistrict.isNullOrEmpty()) {
                mViewBind.detailWorkInfoView.setContentText(it.companyDistrict)
                mViewBind.detailWorkInfoView.setTagComplete()
            }
            if (!it.payDay.isNullOrEmpty()) {
                mViewBind.payDayView.setContentText(it.payDay)
                mViewBind.payDayView.setTagComplete()
            }
            if (!it.income.isNullOrEmpty()) {
                mViewBind.incomeInfoView.setContentText(it.income)
                mViewBind.incomeInfoView.setTagComplete()

                mViewBind.umeIncomeView.setContentText(it.income)
                mViewBind.umeIncomeView.setTagComplete()
            }
            if (it.timeWorkBegins > 0) {
                val brithDayStr = formatTimestamp(it.timeWorkBegins)
                mViewBind.timeWorkInfoView.setContentText(brithDayStr)
                mViewBind.timeWorkInfoView.setTagComplete()
            }


            //ume
            if (!it.lengthOfUnemployment.isNullOrEmpty()) {
                lengthOfUnemployment = it.lengthOfUnemployment
                mViewBind.lengthOfUmView.setContentText(it.lengthOfUnemployment)
            }
            if ((it.ontherIncome ?: -1) > -1) {
                ontherIncome = it.ontherIncome ?: -1
                singleButtonAdapter.selectPosition = it.ontherIncome ?: -1
                singleButtonAdapter.notifyDataSetChanged()
            }

        }


    }


    fun focusChangeCheck(infoView: SetInfoEditView) {
        infoView.completeListener = {
            checkDoneByType()
        }

    }

    private fun checkDoneByType() {
        if (currentType == type_company) {
            checkNormalDone()
        }
        if (currentType == type_free) {
            checkFreeDone()
        }
        if (currentType == type_ume) {
            checkUmeDone()
        }
        if (currentType == type_student) {
            checkStudentDone()
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