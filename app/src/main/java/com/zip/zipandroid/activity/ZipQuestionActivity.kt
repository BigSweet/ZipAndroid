package com.zip.zipandroid.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.zip.zipandroid.R
import com.zip.zipandroid.adapter.ZipQuestionAdapter
import com.zip.zipandroid.base.ZipBaseBindingActivity
import com.zip.zipandroid.bean.CreditListBeanItem
import com.zip.zipandroid.bean.ZipUploadQuestionBean
import com.zip.zipandroid.databinding.ActivityZipQuestionBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.utils.Constants
import com.zip.zipandroid.view.SetInfoEditView
import com.zip.zipandroid.viewmodel.PersonInfoViewModel

class ZipQuestionActivity : ZipBaseBindingActivity<PersonInfoViewModel, ActivityZipQuestionBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ZipQuestionActivity::class.java)
            context.startActivity(starter)
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        updateToolbarTopMargin(mViewBind.privateIncludeTitle.commonTitleRl)
        mViewBind.privateIncludeTitle.commonBackIv.setOnDelayClickListener {
            finish()
        }
        mViewBind.privateIncludeTitle.titleBarTitleTv.setText("Questionnaire")
        mViewModel.getPersonInfoDic()

        mViewModel.getCreditHistoryDict()
        mViewBind.infoNextBtn.setOnDelayClickListener {
            showLoading()
            mViewModel.saveQuestionList(convertQuestion())
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

    fun showCurrentSelectPop(title: String, data: List<String>?, type: Int, selectPosition: Int, infoView: SetInfoEditView?, bean: CreditListBeanItem) {
        //选择完成后检测
        KeyboardUtils.hideSoftInput(this)
        data ?: return
        infoView ?: return
        showSelectPop(title, data, type, selectPosition, infoView, object : ((String, Int, Int) -> Unit) {
            override fun invoke(tv: String, position: Int, type: Int) {
                bean.questionAnswer = tv
                infoView.setTagComplete()
                checkAllDone()
                checkPopDone()
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
        questionAdapter.data.forEach {
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

    override fun createObserver() {
        mViewModel.creditLiveData.observe(this) {
            questionAdapter.setNewData(it)
            mViewModel.getUserInfo()
        }
        mViewModel.failLiveData.observe(this) {
            dismissLoading()
        }
        mViewModel.userInfoLiveData.observe(this) {
            if (it.questions.isNullOrEmpty()) {
                checkPopDone()
            } else {
                questionAdapter.data.forEach { adaterData ->
                    val questionItem = it.questions.find {
                        it.questionIndex == adaterData.questionIndex
                    }
                    //同步adapter的信息
                    adaterData.questionAnswer = questionItem?.questionAnswer
                }
                questionAdapter.notifyDataSetChanged()
                ThreadUtils.runOnUiThreadDelayed({
                    checkPopDone()
                }, 500)

            }

        }
        mViewModel.saveInfoLiveData.observe(this) {
            //保存进件到第几部了
            mViewModel.saveMemberBehavior(Constants.TYPE_QA)
        }
        mViewModel.saveMemberInfoLiveData.observe(this) {
            if (it == Constants.TYPE_QA) {
                dismissLoading()
                ToastUtils.showShort("finish4")
            }
        }
    }

    override fun getData() {
    }
}