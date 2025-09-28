package com.zip.zipandroid.adapter

import android.graphics.Color
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipProductPeriodItem

class ZipDurationAdapter : BaseQuickAdapter<ZipProductPeriodItem, BaseViewHolder>(R.layout.item_install_zip) {
    var selectPosition = 0
    override fun convert(holder: BaseViewHolder, item: ZipProductPeriodItem) {
        val item_install_tv = holder.getView<TextView>(R.id.item_install_tv)
        val item_install_rl = holder.getView<RelativeLayout>(R.id.item_install_rl)
        item_install_tv.setText(item.period.toString() + " Days")
        if (selectPosition == holder.adapterPosition) {
            item_install_rl.setBackgroundResource(R.drawable.item_install_select_bg)
            item_install_tv.setTextColor(Color.parseColor("#3667F0"))
        } else {
            item_install_tv.setTextColor(Color.parseColor("#A7B0CA"))
            item_install_rl.setBackgroundResource(R.drawable.item_install_normal_bg)

        }

    }
}