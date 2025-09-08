package com.zip.zipandroid.pop

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.zip.zipandroid.adapter.ZipRepaymentPlanAdapter
import com.zip.zipandroid.base.pop.ZipBaseBottomPop
import com.zip.zipandroid.bean.ZipTriaBean
import com.zip.zipandroid.databinding.PopZipRepayMentPlanBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener
import com.zip.zipandroid.view.toN

class ZipRepaymentPlanPop(context: Context, val zipTriaBean: ZipTriaBean?) : ZipBaseBottomPop<PopZipRepayMentPlanBinding>(context) {

    override fun onCreate() {
        super.onCreate()
        mBinding.popRepaymentPlanRv.layoutManager = LinearLayoutManager(context)
        mBinding.popRepaymentPlanRv.adapter = adapter
        adapter.setNewData(zipTriaBean?.repaymentList)
        mBinding.totalBottomTv.setText(zipTriaBean?.totalAmount?.toDouble()?.toN())
        mBinding.planBottomConfirmTv.setOnDelayClickListener {
            dismiss()
        }
        mBinding.planCloseTv.setOnDelayClickListener {
            dismiss()
        }
    }

    val adapter = ZipRepaymentPlanAdapter()
}