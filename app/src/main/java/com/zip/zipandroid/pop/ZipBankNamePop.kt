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
        val manager = LinearLayoutManager(context)
        mBinding.recyclerView.layoutManager = manager
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
        mBinding.sideBar.setOnTouchLetterChangeListenner { isTouch, letter ->
            for (i in 0 until zipBankAdapter.getData().size) {
                if (letter.equals(zipBankAdapter.getData().get(i).firstLetter, true)) {
                    manager.scrollToPositionWithOffset(i, 0)
                    return@setOnTouchLetterChangeListenner
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