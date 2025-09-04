package com.zip.zipandroid.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipCouponItemBean
import com.zip.zipandroid.ktx.visible
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZipCouponAdapter : BaseQuickAdapter<ZipCouponItemBean, BaseViewHolder>(R.layout.item_zip_coupon) {
    var couponStatus = 1
    var selectCoupon = false
    var couponId = ""
    var selectPosition = -1
    override fun convert(holder: BaseViewHolder, item: ZipCouponItemBean) {
        val item_coupon_price_tv = holder.getView<TextView>(R.id.item_coupon_price_tv)
        val coupon_cl = holder.getView<ConstraintLayout>(R.id.coupon_cl)

        val item_coupon_name_tv = holder.getView<TextView>(R.id.item_coupon_name_tv)
        val select_coupon_iv = holder.getView<ImageView>(R.id.select_coupon_iv)
        select_coupon_iv.visible = selectCoupon == true
        if (holder.adapterPosition == selectPosition || couponId == item.id.toString()) {
            select_coupon_iv.setImageResource(R.drawable.zip_item_select_coupon_icon)
        } else {
            select_coupon_iv.setImageResource(R.drawable.zip_item_not_select_coupon)
        }
        val item_coupon_date_tv = holder.getView<TextView>(R.id.item_coupon_date_tv)
        if (couponStatus == 1) {
            //背景图也要改变一下
            coupon_cl.setBackgroundResource(R.drawable.zip_bg_coupon)

            item_coupon_name_tv.setTextColor(Color.parseColor("#ffcd8f06"))
            item_coupon_date_tv.setTextColor(Color.parseColor("#ffb8ac94"))
        } else {
            coupon_cl.setBackgroundResource(R.drawable.zip_bg_coupon_over)

            item_coupon_name_tv.setTextColor(Color.parseColor("#FF909AAA"))
            item_coupon_date_tv.setTextColor(Color.parseColor("#FFACBACF"))
        }
        item_coupon_price_tv.setText(item.worth)
        item_coupon_name_tv.setText(item.typeStr)
        item_coupon_date_tv.setText(formatTimestampToDate(item.endTime))
    }


    fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // 英文月份格式
        val date = Date(timestamp)
        return "Expiration Date: ${dateFormat.format(date)}"
    }
}