package com.zip.zipandroid.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.shape.ShapeTextView

class SingleButtonAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_single_button) {
    var selectPosition = -1
    override fun convert(holder: BaseViewHolder, item: String) {
        val item_single_button_tv = holder.getView<ShapeTextView>(R.id.item_single_button_tv)
        item_single_button_tv.setText(item)
        if (selectPosition == holder.adapterPosition) {
            item_single_button_tv.setBackground(Color.parseColor("#F1F5FF"))
            item_single_button_tv.setTextColor(Color.parseColor("#3667F0"))
        } else {
            item_single_button_tv.setBackground(Color.parseColor("#F7F7F7"))
            item_single_button_tv.setTextColor(Color.parseColor("#000000"))
        }

    }
}