package com.zip.zipandroid.bean

data class ZipHomeDataBean(
    val clientId: String,
    val creditOrderList: CreditOrderList,//最近一笔订单数据
    val mid: Long,
    val productDidInfo: ProductDidInfo,
    val productList: ProductList
)

data class CreditOrderList(
    val allAmountDue: Any,
    val amountDue: Any,
    val applyAmount: String,
    val applyPeriod: String,
    val applyPeriodNew: String,
    val applyTime: Long,
    val approveOpinion: Any,
    val approveTime: Long,
    val bankId: String,
    val bankName: String,
    val bizId: String,
    val capital: Any,
    val cardNo: String,
    val cardType: Any,
    val collectorName: Any,
    val count: Any,
    val couponAmount: Any,
    val creditNo: String,
    val custId: String,
    val custName: String,
    val extendStatus: Any,
    val factCapital: Any,
    val factFine: Any,
    val factInterest: Any,
    val factOtherTotalFee: Any,
    val factOverdueFee: Any,
    val fees: Any,
    val hairCutAmount: Any,
    val ifscCode: Any,
    val interest: Any,
    val interestTime: Any,
    val isLoan: String,
    val lid: Any,
    val loanAmount: Any,
    val loanCancelStatus: Int,
    val loanRefusedDuration: Any,
    val mid: String,
    val noPayStages: Any,
    val otherFee: Any,
    val overdueDays: Any,
    val penalty: Any,
    val period: Any,
    val periodTime: Any,
    val periodTimeStr: Any,
    val phoneNum: String,
    val productType: String,
    val rate: Any,
    val releaseTime: Long,
    val repaymentResponseList: Any,
    val shouldCapital: Any,
    val shouldFine: Any,
    val shouldInterest: Any,
    val shouldOtherTotalFee: Any,
    val shouldOverdueFee: Any,
    val stageCount: Int,
    val status: String,
    val subtractCapital: Any,
    val subtractFine: Any,
    val subtractInterest: Any,
    val subtractOtherTotalFee: Any,
    val subtractOverdueFee: Any,
    val totalIVA: Any,
    val totalInterestReal: Any,
    val updateTime: Long,
    val userConfirmStatus: Int,
    val waitRepayStatus: Any
)

data class ProductDidInfo(
    val did: Long,
    val highlight: Int,
    val intervalClose: Int,
    val intervalStart: Int,
    val intervalType: Int,
    val isfirst: Int,
    val minQuota: Int,
    val paidType: Int,
    val productDueFeeList: List<ProductDueFee>,
    val state: Int
)

data class ProductList(
    val frontDisplay: Int,
    val limitInterval: Int,
    val limitMax: String,
    val limitMin: String,
    val pid: String,
    val productName: String,
    val productType: Int,
    val remark: String,
    val productPeriods: ZipProductPeriod,
    val state: Int
)

data class ProductDueFee(
    val calcPattern: String,
    val calcType: Int,
    val calcValue: String,
    val feetype: Int,
    val fid: Long,
    val name: String,
    val riskGrade: String
)