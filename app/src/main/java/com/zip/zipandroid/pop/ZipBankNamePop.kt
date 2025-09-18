package com.zip.zipandroid.pop

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.zip.zipandroid.adapter.ZipBankAdapter
import com.zip.zipandroid.base.pop.ZipBaseBottomPop
import com.zip.zipandroid.bean.ZipBankNameListBean
import com.zip.zipandroid.bean.ZipBankNameListBeanItem
import com.zip.zipandroid.databinding.PopZipBankNameBinding
import com.zip.zipandroid.ktx.setOnDelayClickListener

class ZipBankNamePop(context: Context, val list: ZipBankNameListBean?) : ZipBaseBottomPop<PopZipBankNameBinding>(context) {


    var selectBank: ((ZipBankNameListBeanItem) -> Unit)? = null
    override fun onCreate() {
        super.onCreate()
        mBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        val zipBankAdapter = ZipBankAdapter()
        mBinding.recyclerView.adapter = zipBankAdapter
        zipBankAdapter.setNewData(list)
        zipBankAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            //选中效果
            zipBankAdapter.selectPosition = i
            zipBankAdapter.notifyDataSetChanged()
        }
        mBinding.bankNameCloseTv.setOnDelayClickListener {
            dismiss()
        }
        mBinding.sideBar.setOnStrSelectCallBack { index, selectStr ->
            for (i in 0 until zipBankAdapter.getData().size) {
                if (selectStr.equals(zipBankAdapter.getData().get(i).firstLetter, true)) {
                    mBinding.recyclerView.smoothScrollToPosition(i)
                    return@setOnStrSelectCallBack
                }
            }
        }
        mBinding.singleSureTv.setOnDelayClickListener {
            val data = list?.get(zipBankAdapter.selectPosition)
            data?.let { it1 -> selectBank?.invoke(it1) }
            dismiss()
        }

    }
}