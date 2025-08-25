package com.zip.zipandroid.pop

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.zip.zipandroid.adapter.CommonSingleSelectAdapter
import com.zip.zipandroid.base.pop.ZipBaseBottomPop
import com.zip.zipandroid.databinding.PopSingleCommonSelectBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class SingleCommonSelectPop(context: Context, val title: String, val data: List<String>?, val type: Int) : ZipBaseBottomPop<PopSingleCommonSelectBinding>(context) {


    companion object {
        const val edu_type = 1
        const val ma_type = 2
        const val child_type = 3
        const val la_type = 4
    }

    var sureClick: ((String, Int, Int) -> Unit)? = null
    override fun onCreate() {
        super.onCreate()
        mBinding.singleCommonCloseTv.setOnDelayClickListener {
            dismiss()
        }
        mBinding.singleCommonTitleTv.setText(title)

        mBinding.singleSureTv.setOnDelayClickListener {
            //确认选择返回文字和index
            val text = selectAdapter.data[selectAdapter.selectPosition]
            val position = selectAdapter.selectPosition
            sureClick?.invoke(text, position, type)
            dismiss()
        }
        mBinding.singleCommonRv.layoutManager = LinearLayoutManager(context)
        mBinding.singleCommonRv.adapter = selectAdapter
        selectAdapter.setNewData(data)
        selectAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val item = baseQuickAdapter.getItem(i) as String
            selectAdapter.selectPosition = i
            selectAdapter.notifyDataSetChanged()
        }
    }

    var selectAdapter = CommonSingleSelectAdapter()
}