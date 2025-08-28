package com.zip.zipandroid.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipBankNameListBeanItem
import java.util.Locale

class ZipBankAdapter : BaseQuickAdapter<ZipBankNameListBeanItem, BaseViewHolder>(R.layout.item_zip_band_name) {
    var selectPosition = 0

    override fun convert(helper: BaseViewHolder, item: ZipBankNameListBeanItem) {
        val llIndex = helper.getView<LinearLayout>(R.id.ll_index)
        //根据position获取首字母作为目录catalog
        val catalog = item.firstLetter
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (helper.adapterPosition == getPositionForSection(catalog)) {
            llIndex.visibility = View.VISIBLE
            helper.setText(R.id.tv_index, item.firstLetter.uppercase(Locale.getDefault()))
        } else {
            llIndex.visibility = View.GONE
        }
        val tvName = helper.getView<TextView>(R.id.item_zip_band_name_tv)
        val item_bank_cl = helper.getView<ConstraintLayout>(R.id.item_bank_cl)
        tvName.text = item.bankName

        if (helper.adapterPosition == selectPosition) {
            tvName.typeface = Typeface.DEFAULT_BOLD
            item_bank_cl.setBackgroundColor(Color.parseColor("#FFE8EEFF"))
        } else {
            tvName.typeface = Typeface.DEFAULT
            item_bank_cl.setBackgroundColor(Color.parseColor("#ffffff"))

        }
    }

    /**
     * 获取catalog首次出现位置
     */
    fun getPositionForSection(catalog: String): Int {
        for (i in data.indices) {
            val sortStr = data[i].firstLetter
            if (catalog.equals(sortStr, ignoreCase = true)) {
                return i
            }
        }
        return -1
    }
}
