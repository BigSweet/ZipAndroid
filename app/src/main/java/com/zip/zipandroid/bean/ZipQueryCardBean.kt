package com.zip.zipandroid.bean

class ZipQueryCardBean : ArrayList<ZipQueryCardBeanItem>()

data class ZipQueryCardBeanItem(
    val bankId: Int,
    val bankName: String,
    val cardNo: String,
    val cardType: Int,
    val cvv2: String,
    val eMail: Any,
    val expirationMonth: Any,
    val expirationYear: Any,
    val firstName: String,
    val fullName: String,
    val id: Long,
    val identityCardNo: String,
    val ifscCode: Any,
    val lastName: String,
    val mid: Long,
    val phone: String,
    val status: Int
)