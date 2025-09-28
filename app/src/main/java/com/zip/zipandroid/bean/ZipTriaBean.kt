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
    val totalInterestExcludeTaxFee: String,
) {
}


data class ZipRepayment(
    val factCapital: Int,
    val loanFeeDTO: List<ZipLoanFeeDTO>? = null,
    val rate: Int,
    val shouldCapital: Double,
    val shouldInterest: Double,
    val shouldAmount: String,
    val shouldTime: Long,
)
//indexs
//number
//true
//还款期次
//shouldAmount
//string
//true
//应还金额
//shouldCapital
//string
//true
//应还本金
//shouldInterest
//string
//true
//应还利息
//shouldOtherTotalFee
//string
//true
//应还其他费用
//shouldTime
//number
//true
//应还时间


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