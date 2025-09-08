package com.zip.zipandroid.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zip.zipandroid.R
import com.zip.zipandroid.shape.ZipShapeTextView

class ZipPersonInfoEmailAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_zip_person_email) {
    override fun convert(holder: BaseViewHolder, item: String) {

        val person_email_tv = holder.getView<ZipShapeTextView>(R.id.person_email_tv)
        person_email_tv.setText(item)
    }
}