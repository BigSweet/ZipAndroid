package com.zip.zipandroid.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WebLoanBean(
    val applyTime: String, val bankName: String, val bizId: String, val cardNo: String, val did: String,  val phoneNum: String,
    val productType: String, val riskLevel: String,  val applyAmount: Int,
):Parcelable {
}