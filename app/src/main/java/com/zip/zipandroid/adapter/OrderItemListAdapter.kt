package com.zip.zipandroid.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipOrderListBeanItem
import com.zip.zipandroid.ktx.hide
import com.zip.zipandroid.ktx.show
import com.zip.zipandroid.ktx.visible
import com.zip.zipandroid.shape.ZipShapeConstraintLayout
import com.zip.zipandroid.shape.ZipShapeTextView
import com.zip.zipandroid.view.toN
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderItemListAdapter : BaseQuickAdapter<ZipOrderListBeanItem, BaseViewHolder>(R.layout.item_zip_order_list) {
    fun formatTimestampToDate(preStr: String, timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // 英文月份格式
        val date = Date(timestamp)
        return "${preStr} ${dateFormat.format(date)}"
    }


//
//    WAITING 挂起状态，跳转到订单确认页面，等待用户确认，用户确认或取消后跳转到首页。
//
//
//
//    status：FAIL 表示放款失败
//
//
//    TRANSACTION 放款中
//
//    FINISH 已结清//默认页面
//    CANCELED 已取消//不处理 //默认页面
//    EXECUTING 执行中 跳转到审核中页面
//    PASSED 已通过
//    LENDING 已放款 //
//    NOTREPAID 未还款
//    OVERDUE 逾期
//    PARTIAL 部分还款
//
//    REFUSED 已拒绝  审核失败页面//审核失败
//

    override fun convert(holder: BaseViewHolder, item: ZipOrderListBeanItem) {
        holder.addOnClickListener(R.id.zip_order_item_show_detail_tv, R.id.zip_order_item_repay_btn, R.id.zip_order_item_finish_detail_tv)
        val zip_order_item_main_cl = holder.getView<ZipShapeConstraintLayout>(R.id.zip_order_item_main_cl)
        val zip_order_item_inner_cl = holder.getView<ZipShapeConstraintLayout>(R.id.zip_order_item_inner_cl)
        val zip_item_order_inner_top_tv = holder.getView<ZipShapeTextView>(R.id.zip_item_order_inner_top_tv)
        val zip_item_amount_update = holder.getView<ZipShapeTextView>(R.id.zip_item_amount_update)
        val zip_order_item_show_detail_tv = holder.getView<TextView>(R.id.zip_order_item_show_detail_tv)
        val bottom_status_tv = holder.getView<TextView>(R.id.bottom_status_tv)
        val zip_order_item_finish_detail_tv = holder.getView<TextView>(R.id.zip_order_item_finish_detail_tv)
        val zip_order_item_repay_btn = holder.getView<ZipShapeTextView>(R.id.zip_order_item_repay_btn)
        val item_zip_order_time_tv = holder.getView<TextView>(R.id.item_zip_order_time_tv)
        val zip_order_amount_place_tv = holder.getView<TextView>(R.id.zip_order_amount_place_tv)
        val zip_order_item_install_place_tv = holder.getView<TextView>(R.id.zip_order_item_install_place_tv)
        val zip_item_order_no_tv = holder.getView<TextView>(R.id.zip_item_order_no_tv)
        val zip_item_order_amount_tv = holder.getView<TextView>(R.id.zip_item_order_amount_tv)
        val zip_order_item_install_tv = holder.getView<TextView>(R.id.zip_order_item_install_tv)
        zip_item_order_no_tv.setText("Order No. " + item.bizId)
        if (!item.amountDue.isNullOrEmpty()) {
            zip_item_order_amount_tv.setText(item.amountDue.toDouble().toN())
        }


        zip_order_item_show_detail_tv.hide()
        zip_order_item_repay_btn.hide()
        bottom_status_tv.hide()
        zip_item_amount_update.hide()
        zip_order_item_finish_detail_tv.hide()



        zip_item_order_inner_top_tv.setBackground(Color.parseColor("#FFD2D2D2"))
        zip_order_amount_place_tv.setTextColor(Color.parseColor("#FFA5A5A5"))
        zip_order_item_install_place_tv.setTextColor(Color.parseColor("#FFA5A5A5"))
        zip_order_item_main_cl.setBackground2(Color.parseColor("#FF9F9F9F"))

        zip_order_item_inner_cl.setBackground2(Color.parseColor("#FFE4E4E4"))


        zip_item_order_inner_top_tv.setTextColor(Color.parseColor("#FF8D8D8D"))

        bottom_status_tv.setTextColor(Color.parseColor("#FF8D8D8D"))

        if (item.status == "FINISH" || item.status == "OVERDUEREPAYMENT") {
            item_zip_order_time_tv.setText(formatTimestampToDate("Completion Date:", item.periodTime))
            zip_order_item_finish_detail_tv.show()
            zip_item_order_inner_top_tv.setText("Repaid")
            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1
            zip_order_item_install_tv.setText("Installment" + item.period.toString() + "/" + item.stageCount.toString())
        }
        if (item.status == "REFUSED") {
            zip_item_order_amount_tv.setText(item.applyAmount.toDouble().toN())
            item_zip_order_time_tv.setText(formatTimestampToDate("Completion Date:", item.periodTime))
            //审核拒绝
            zip_item_order_inner_top_tv.setText("Rejected")

            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1
            bottom_status_tv.show()
            bottom_status_tv.setText("This loan order has been rejected")
            //订单列表
            //
            //每期多少天字段是哪个
            zip_order_item_install_tv.setText(item.stageCount.toString() + "Installment,\n Each 14 Days")

        }
        if (item.status == "CANCELED" || item.status == "CANCEL") {
            zip_item_order_amount_tv.setText(item.applyAmount.toDouble().toN())
            item_zip_order_time_tv.setText(formatTimestampToDate("Completion Date:", item.periodTime))
            bottom_status_tv.show()
            bottom_status_tv.setText("This loan order has been cancelled")

            zip_item_order_inner_top_tv.setText("Cancelled")

            //订单取消，用户自己取消或者后台取消
            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1

            //订单列表
            //
            //每期多少天字段是哪个
            zip_order_item_install_tv.setText(item.stageCount.toString() + "Installment,\n Each 14 Days")

        }

        if (item.status == "EXECUTING" || item.status == "WAITING") {
            zip_item_order_amount_tv.setText(item.applyAmount.toDouble().toN())
            item_zip_order_time_tv.setText(formatTimestampToDate("Approval Date:", item.applyTime))
//            if (item.status == "WAITING") {
            //自动放款模式下才有的状态，展现倒计时
//            }
            //执行中
            zip_order_item_main_cl.setBackground2(Color.parseColor("#FFFBBC32"))
            zip_order_item_inner_cl.setBackground2(Color.parseColor("#FFFFF6E1"))

            zip_item_order_inner_top_tv.setBackground(Color.parseColor("#FFFFE6AC"))
            zip_item_order_inner_top_tv.setText("Processing")
            zip_item_order_inner_top_tv.setTextColor(Color.parseColor("#FFDC9F19"))


            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1
            //订单列表
            //
            //每期多少天字段是哪个
            zip_order_item_install_tv.setText(item.stageCount.toString() + "Installment,\n Each 14 Days")
            bottom_status_tv.setText("You'll receive the review outcome in a\n" +
                    " few minutes")
            bottom_status_tv.show()
            bottom_status_tv.setTextColor(Color.parseColor("#FFDC9F19"))

            zip_order_amount_place_tv.setTextColor(Color.parseColor("#FFB08F64"))
            zip_order_item_install_place_tv.setTextColor(Color.parseColor("#FFB08F64"))

        }

        if (item.status == "TRANSACTION") {
            zip_item_order_amount_tv.setText(item.applyAmount.toDouble().toN())
            item_zip_order_time_tv.setText(formatTimestampToDate("Approval Date:", item.applyTime))
            bottom_status_tv.show()
            bottom_status_tv.setTextColor(Color.parseColor("#FF1E984D"))
            bottom_status_tv.setText("Your loan amount will be disbursed to your account shortly")
            //放款中
            zip_order_item_main_cl.setBackground2(Color.parseColor("#FF32C069"))
            zip_order_item_inner_cl.setBackground2(Color.parseColor("#FFD9EDE1"))

            zip_item_order_inner_top_tv.setBackground(Color.parseColor("#FFB3E6C7"))
            zip_item_order_inner_top_tv.setText("Disbursing")
            zip_item_order_inner_top_tv.setTextColor(Color.parseColor("#FF1E984D"))


            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1

            //订单列表
            //
            //每期多少天字段是哪个
            zip_order_item_install_tv.setText(item.stageCount.toString() + "Installment,\n Each 14 Days")


            zip_order_amount_place_tv.setTextColor(Color.parseColor("#FF649778"))
            zip_order_item_install_place_tv.setTextColor(Color.parseColor("#FF649778"))

        }



        if (item.status == "NOTREPAID" || item.status == "PARTIAL" || item.status == "LENDING" || item.status == "PASSED") {
            zip_item_amount_update.visible = item.status == "PARTIAL"
            item_zip_order_time_tv.setText(formatTimestampToDate("Due Date:", item.periodTime))
            zip_item_order_inner_top_tv.setText("Pending Repayment")
            zip_item_order_inner_top_tv.setTextColor(Color.parseColor("#FF3667F0"))

            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1
            //待还款
            zip_order_item_show_detail_tv.show()
            zip_order_item_repay_btn.show()
            zip_order_item_show_detail_tv.setBackgroundResource(R.drawable.bg_order_repay_detail)
            zip_item_order_inner_top_tv.setBackground(Color.parseColor("#FFC1D2FF"))
            zip_order_item_main_cl.setBackground2(Color.parseColor("#FF3667F0"))
            zip_order_item_inner_cl.setBackground2(Color.parseColor("#FFDFE8FF"))
            zip_order_item_show_detail_tv.setTextColor(Color.parseColor("#FF3667F0"))

            zip_order_item_repay_btn.setBackground(Color.parseColor("#FF3667F0"))
            zip_order_item_install_tv.setText("Installment" + item.period.toString() + "/" + item.stageCount.toString())

            zip_order_amount_place_tv.setTextColor(Color.parseColor("#FF6471B0"))
            zip_order_item_install_place_tv.setTextColor(Color.parseColor("#FF6471B0"))

        }
        if (item.status == "OVERDUE") {
            item_zip_order_time_tv.setText(formatTimestampToDate("Due Date:", item.periodTime))
            zip_order_item_install_tv.setText("Installment" + item.period.toString() + "/" + item.stageCount.toString())

            zip_item_order_inner_top_tv.setText("OVERDUE: ${item.overdueDays} DAYS")
            zip_item_order_inner_top_tv.setTextColor(Color.parseColor("#FFFF4343"))
            zip_order_amount_place_tv.setTextColor(Color.parseColor("#FFB06464"))
            zip_order_item_install_place_tv.setTextColor(Color.parseColor("#FFB06464"))

            zip_order_item_install_tv.visible = item.stageCount != 1
            zip_order_item_install_place_tv.visible = item.stageCount != 1
            //逾期
            zip_order_item_repay_btn.setBackground(Color.parseColor("#FFFF4343"))
            zip_order_item_show_detail_tv.show()
            zip_order_item_repay_btn.show()
            zip_order_item_show_detail_tv.setBackgroundResource(R.drawable.bg_order_over_detail)
            zip_order_item_show_detail_tv.setTextColor(Color.parseColor("#FFFF4343"))

            zip_item_order_inner_top_tv.setBackground(Color.parseColor("#FFFFBDBD"))

            zip_order_item_main_cl.setBackground2(Color.parseColor("#FFFF4343"))
            zip_order_item_inner_cl.setBackground2(Color.parseColor("#FFFFE4E4"))
        }
//        if (item.status == "PARTIAL") {
//            //部分还款，未还款的子状态
//        }

    }
}