package com.zip.zipandroid.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.CreditListBeanItem
import com.zip.zipandroid.view.SetInfoEditView

class ZipQuestionAdapter : BaseQuickAdapter<CreditListBeanItem, BaseViewHolder>(R.layout.item_zip_loan_question) {
    var questionItemClick: ((ZipQuestionAdapter, View, Int) -> Unit)? = null
    override fun convert(holder: BaseViewHolder, item: CreditListBeanItem) {
        val zip_question_loan = holder.getView<SetInfoEditView>(R.id.zip_question_loan)
        zip_question_loan.setTopName(item.questionValue)
        if (!item.questionAnswer.isNullOrEmpty()) {
            zip_question_loan.setContentText(item.questionAnswer.toString())
        }
        zip_question_loan.infoViewClick = {
            questionItemClick?.invoke(this, zip_question_loan, holder.adapterPosition)
        }

    }

//    mViewBind.zipQuestionLoan.infoViewClick = {
//        //借款用途
//    }
//    mViewBind.zipQuestionLoanAmount.infoViewClick = {
//        //期望借款金额范围
//    }
//    mViewBind.zipQuestionMaxLoanAmount.infoViewClick = {
//        //当前单笔最大借款
//    }
//    mViewBind.zipQuestionTotalOutLoanAmount.infoViewClick = {
//        //当前总欠款金额
//    }
//    mViewBind.zipQuestionHisLoanAmount.infoViewClick = {
////            历史贷款次数
//    }
//
//
//    mViewBind.zipQuestionNumberLoan.infoViewClick = {
//        //当前未还清贷款数
//    }
//    mViewBind.zipQuestionRepaidNumberLoan.infoViewClick = {
//        //按时还清的贷款数
//    }
//    mViewBind.zipQuestionOverNumberLoan.infoViewClick = {
//        //当前逾期贷款数
//    }
}