package com.zip.zipandroid.bean

class ZipPayChannelListBean : ArrayList<ZipPayChannelListBeanItem>()

data class ZipPayChannelListBeanItem(
    val amount: Any,
    val bankCode: String,
    val bankName: String,
    val bizId: Any,
    val channelDescriptionImg: String,
    val channelDescriptionImgList: Any,
    val channelDescriptionText: String,
    val channelDescriptionTextList: Any,
    val channelId: Int,
    val channelName: String,
    val customerId: Any,
    val merchantId: Any,
    val mid: Any,
    val payType: Int,
    val paymentType: Int,
    val paymentTypeList: Any,
    val settlementType: Any,
    val tabName: String,
    val tabType: Int,
    val thridBankName: String
)