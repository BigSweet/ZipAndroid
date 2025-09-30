package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ThreadUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipQuestionAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.CreditListBeanItem
import com.zip.zipandroid.bean.ZipUploadQuestionBean
import com.zip.zipandroid.databinding.ActivityZipQuestionBinding
import com.zip.zipandroid.event.ZipFinishInfoEvent
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.utils.ZipEventBusUtils
import com.zip.zipandroid.utils.ZipTrackUtils
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ZipQuestionActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipQuestionBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipQuestionActivity::class.java)
            context.startActivity(starter)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ZipFinishInfoEvent) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        ZipEventBusUtils.unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZipTrackUtils.track("enter_questionnaire")
        ZipEventBusUtils.register(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ZipTrackUtils.track("exit_questionnaire")

    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            ZipTrackUtils.track("exit_questionnaire")
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Questionnaire")
        mViewModel.getPersonInfoDic()
        showLoading()

        mViewBind.infoNextBtn.setOnDelayClickListener {
            showLoading()
            ZipTrackUtils.track("submit_questionnaire_ok")
            mViewModel.saveQuestionList(convertQuestion(), purpose, amountRange)
        }
        mViewBind.zipQuestionRv.layoutManager = LinearLayoutManager(this)
        questionAdapter.bindToRecyclerView(mViewBind.zipQuestionRv)
        questionAdapter.questionItemClick = { baseQuickAdapter, view, i ->
            val data = baseQuickAdapter.getItem(i) as CreditListBeanItem
//            val setInfoEditView = baseQuickAdapter.getViewByPosition(i, R.id.zip_question_loan) as SetInfoEditView
            var selectionPosition = 0
            if (data.questionAnswer.isNullOrEmpty()) {
                selectionPosition = 0
            } else {
                data.answer.forEachIndexed { index, s ->
                    if (s == data.questionAnswer) {
                        selectionPosition = index
                    }
                }
            }
            showCurrentSelectPop(data.questionValue, data.answer, data.questionIndex.toInt(), selectionPosition, view as SetInfoEditView, data)
        }


    }

    var purpose = -1//贷款用途
    var amountRange = -1//借款金额
    fun showCurrentSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int, infoView: SetInfoEditView?, bean: CreditListBeanItem) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        infoView ?: return
        showSelectPop(title, data, type, selectPosition, infoView, object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                bean.questionAnswer = tv
                infoView.setTagComplete()
                checkPopDone()
                checkAllDone()
                if (type == -1) {
                    //借款用途
                    purpose = position
                }
                if (type == -2) {
                    //借款金额
                    amountRange = position
                }
            }
        })
    }

    private fun checkAllDone() {
        val data = questionAdapter.data.find {
            it.questionAnswer.isNullOrEmpty()
        }
        var done = data == null
        mViewBind.infoNextBtn.setEnabledPlus(done)
    }


    fun convertQuestion(): List<ZipUploadQuestionBean> {
        val questionList = arrayListOf<ZipUploadQuestionBean>()
        val filterList = questionAdapter.data.filter {
            it.questionIndex != "-1" && it.questionIndex != "-2"
        }
        filterList.forEach {
            val bean = ZipUploadQuestionBean(it.questionAnswer
                ?: "", it.questionIndex.toInt(), it.questionValue)
            questionList.add(bean)
        }
        return questionList
    }

    private fun checkPopDone() {
        questionAdapter.data.forEachIndexed { index, creditListBeanItem ->
            if (creditListBeanItem.questionAnswer.isNullOrEmpty()) {
                val setInfoEditView = questionAdapter.getViewByPosition(index, R.id.zip_question_loan) as SetInfoEditView?
                showCurrentSelectPop(creditListBeanItem.questionValue, creditListBeanItem.answer, creditListBeanItem.questionIndex.toInt(), 0, setInfoEditView, creditListBeanItem)
                return
            }
        }
    }


    var questionAdapter = ZipQuestionAdapter()


//
//    "loanPurpose": [
//    "Business",
//    "Rent",
//    "Pharmacy",
//    "Education",
//    "Agric",
//    "Auto",
//    "Personal",
//    "Others"
//    ]
//    "loanAmountRange": [
//    "Less than ₦10,000",
//    "₦10,001 - ₦30,000",
//    "₦30,001 - ₦50,000",
//    "More than ₦50,000 "
//    ],
//    Desired range of the loan amount

    var loanPurpose = listOf<String>()
    var loanAmountRange = listOf<String>()

    //    取/api/v4/ziplead/dict/getPersonalInformationDict的"loanAmountRange"、"loanPurpose"
    override fun createObserver() {
        mViewModel.creditLiveData.observe(this) {
//            loanAmountRange
//            loanPurpose
            val realList = arrayListOf<CreditListBeanItem>()
            //接口用途
            val loanPurpose = CreditListBeanItem(loanPurpose, "-1", "Purpose of the loan", "")
            //期望接口金额
            val loanAmountRange = CreditListBeanItem(loanAmountRange, "-2", "Desired range of the loan amount", "")
            realList.add(loanPurpose)
            realList.add(loanAmountRange)
            realList.addAll(it)
            questionAdapter.setNewData(realList)

            mViewModel.getUserInfo()


        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.userInfoLiveData.observe(this) {
            dismissLoading()
            if (it.questions.isNullOrEmpty()) {
                checkPopDone()
            } else {
                if (it.purpose > -1) {
                    purpose = it.purpose
                    val data = questionAdapter.data.find {
                        it.questionIndex == "-1"
                    }
                    if (data != null) {
                        data.questionAnswer = loanPurpose.get(it.purpose)
                    }
                }
                if (it.amountRange > -1) {
                    amountRange = it.amountRange
                    val data = questionAdapter.data.find {
                        it.questionIndex == "-2"
                    }
                    if (data != null) {
                        data.questionAnswer = loanAmountRange.get(it.amountRange)
                    }
                }

                val filterList = questionAdapter.data.filter {
                    it.questionIndex != "-1" && it.questionIndex != "-2"
                }

                filterList.forEach { adaterData ->
                    val questionItem = it.questions.find {
                        it.questionIndex == adaterData.questionIndex
                    }
                    //同步adapter的信息
                    adaterData.questionAnswer = questionItem?.answer
                }

                questionAdapter.notifyDataSetChanged()
                ThreadUtils.runOnUiThreadDelayed({
                    checkPopDone()
                    checkAllDone()
                }, 500)

            }

        }
        mViewModel.personDicLiveData.observe(this) {
            loanPurpose = it.loanPurpose
            loanAmountRange = it.loanAmountRange
            mViewModel.getCreditHistoryDict()
        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_QA)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_QA) {
                dismissLoading()
//                ToastUtils.showShort("finish4")
                ZipBandCardActivity.start(this, false)
//                startActivity(ZipBandCardActivity::class.java)
            }
        }
    }

    override fun getData() {
    }
}