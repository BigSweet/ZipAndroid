package com.zip.zipandroid.bean

class ZipTriaBean(
    val finePayDay: String,
    val fineScale: String,
    val gstFee: String,
    val payAmount: String,
    val platformServiceFee: String,
    val rate: Int,
    val repaymentList: List<ZipRepayment>? = null,
    val serviceFee: String,
    val shouldTime: Long,
    val totalAmount: String,
    val couponAmount: String,
    val count: Int,
    val totalFee: String,
    val totalInsterst: String,
) {
}


data class ZipRepayment(
    val factCapital: Int,
    val loanFeeDTO: List<ZipLoanFeeDTO>? = null,
    val rate: Int,
    val shouldCapital: Double,
    val shouldInterest: Double,
    val shouldAmount: Double,
    val shouldTime: Long,
)

data class ZipLoanFeeDTO(
    val chargeType: Int,
    val createUser: String,
    val factAmount: Int,
    val feeCode: String,
    val feeName: String,
    val feeRate: Int,
    val feeTotal: Int,
    val shouldAmount: Int,
    val updateUser: String,
)