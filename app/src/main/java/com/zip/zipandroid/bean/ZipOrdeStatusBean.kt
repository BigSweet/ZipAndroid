package com.zip.zipandroid.bean

class MacawOrderPayBean(val applyAmount: String,
                        val applyPeriod: String,
                        val applyTime: Long,
                        val bankId: String,
                        val creditxStatus: String,
                        val cardNo: String,
                        val creditNo: String,
                        val custId: String,
                        val custName: String,
                        val did: String,
                        val isLoan: String,
                        val lid: Long,
                        val mid: String,
                        val fees :List<FeesBean>?,
                        val phoneNum: String,
                        val productType: String,
                        val status: String,
                        val updateTime: Long,
                        val userConfirmStatus: Int,
                        val extendStatus: Int,) {
}

data class FeesBean(val amount:Int)