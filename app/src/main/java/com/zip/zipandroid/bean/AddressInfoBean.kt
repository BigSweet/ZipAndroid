package com.zip.zipandroid.bean

import com.contrarywind.interfaces.IPickerViewData

data class AddressInfoBean(
    val name: String,
    val child: List<AddressInfoBean>?,
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return name
    }
}