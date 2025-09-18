package com.zip.zipandroid.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.bean.ZipContractBean
import com.zip.zipandroid.ktx.visiblein
import com.zip.zipandroid.view.SetInfoEditView

class ZipContractAdapter : BaseQuickAdapter<ZipContractBean, BaseViewHolder>(R.layout.item_zip_contract) {

    var popClick: ((Int, Int) -> Unit)? = null
    var checkCompleteListener: (() -> Unit)? = null
    override fun convert(holder: BaseViewHolder, item: ZipContractBean) {
        val item_contract_main_title_xx = holder.getView<TextView>(R.id.item_contract_main_title_xx)
        item_contract_main_title_xx.visiblein = item.showX ?: true
        val item_contract_main_title_tv = holder.getView<TextView>(R.id.item_contract_main_title_tv)
        if (!item.title.isNullOrEmpty()) {
            item_contract_main_title_tv.setText(item.title)
        }
        val item_contract_relation_tv = holder.getView<SetInfoEditView>(R.id.item_contract_relation_tv)
        item_contract_relation_tv.infoViewClick = {
            popClick?.invoke(holder.adapterPosition, item.id)
        }

        if (!item.relationValue.isNullOrEmpty()) {
            item_contract_relation_tv.setContentText(item.relationValue.toString())
        }
        val item_contract_name_tv = holder.getView<SetInfoEditView>(R.id.item_contract_name_tv)
        if (!item.contactName.isNullOrEmpty()) {
            item_contract_name_tv.setContentText(item.contactName.toString())
        }
        item_contract_name_tv.textChangeListener = {
            item.contactName = item_contract_name_tv.getEditText()
        }
        item_contract_name_tv.completeListener = {
            checkCompleteListener?.invoke()
        }
        val item_contract_number_tv = holder.getView<SetInfoEditView>(R.id.item_contract_number_tv)
        if (!item.contactPhone.isNullOrEmpty()) {
            item_contract_number_tv.setContentText(item.contactPhone.toString())
        }
        item_contract_number_tv.textChangeListener = {
            var realPhone = item_contract_number_tv.getEditText().toString()
            if (item_contract_number_tv.getEditText()?.length == 11) {
                realPhone = item_contract_number_tv.getEditText()?.substring(1, (item_contract_number_tv.getEditText()?.length
                    ?: 1)).toString()
            }
            item.contactPhone = realPhone
        }
        item_contract_number_tv.completeListener = {
            checkCompleteListener?.invoke()
        }

    }
}