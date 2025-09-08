package com.zip.zipandroid.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipRepayment
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.utils.ZipTimeUtils
import com.zip.zipandroid.view.VerticalDashedLineView
import com.zip.zipandroid.view.toN

class ZipRepaymentPlanAdapter : BaseQuickAdapter<ZipRepayment, BaseViewHolder>(R.layout.item_zip_repayment_plan) {
    override fun convert(holder: BaseViewHolder, item: ZipRepayment) {
        val item_zip_repayment_count_tv = holder.getView<TextView>(R.id.item_zip_repayment_count_tv)
        val item_zip_repayment_time_tv = holder.getView<TextView>(R.id.item_zip_repayment_time_tv)
        val item_payment_price_tv = holder.getView<TextView>(R.id.item_payment_price_tv)
        val plan_top_line_view = holder.getView<VerticalDashedLineView>(R.id.plan_top_line_view)
        val plan_bottom_line_view = holder.getView<VerticalDashedLineView>(R.id.plan_bottom_line_view)
        item_zip_repayment_count_tv.setText((holder.adapterPosition + 1).toString() + "/" + data.size.toString())
        item_zip_repayment_time_tv.setText(ZipTimeUtils.formatTimestampToDate(item.shouldTime))
        item_payment_price_tv.setText(item.shouldAmount.toN())
        plan_top_line_view.visible = holder.adapterPosition != 0
        plan_bottom_line_view.visible = holder.adapterPosition != data.size - 1
    }
}