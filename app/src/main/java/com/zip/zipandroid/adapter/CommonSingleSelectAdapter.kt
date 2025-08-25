package com.zip.zipandroid.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.shape.ShapeConstraintLayout

class CommonSingleSelectAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_single_common_select) {
    var selectPosition = 0
    override fun convert(holder: BaseViewHolder, item: String) {
        val item_single_select_tv = holder.getView<TextView>(R.id.item_single_select_tv)
        val item_single_select_cl = holder.getView<ShapeConstraintLayout>(R.id.item_single_select_cl)
        item_single_select_tv.setText(item)
        if (holder.adapterPosition == selectPosition) {
            item_single_select_tv.typeface = Typeface.DEFAULT_BOLD
            item_single_select_cl.setBackground2(Color.parseColor("#FFE8EEFF"))
        } else {
            item_single_select_tv.typeface = Typeface.DEFAULT
            item_single_select_cl.setBackground2(Color.parseColor("#ffffff"))

        }


    }
}