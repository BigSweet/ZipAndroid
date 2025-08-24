package com.zip.zipandroid.bean

data class ZipAppConfigBean(
    val APP_CLIENTID_NAME: String,
    val APP_CONTRACT_TEMPLATE: String,
    val APP_CUSTOMER_SERVICE_EMAIL: String,
    val APP_PRIVACY_AGREEMENT: String,
    val APP_REGISTER_AGREEMENT: String,
    val APP_CUSTOMER_SERVICE_WHATSAPP: String,
    val APP_CUSTOMER_SERVICE_ZALO: String,
    val APP_LOAN_CONTRACT: String,
    val APP_QA_ADV: String,
    val APP_REPAYMENT_AGREEMENT: String,
)

//
//APP_MX_BANNER_ADV
//
//首页banner配置，使用方式同常见问题
//
//1710000513011653
//
//APP_QA_ADV
//
//我的-》常见问题对应的广告位ID，用配置的ID，调用获取广告信息接口
//
//1710000513011654
//
//APP_HIDE_WORK_PROOF_PHOTO
//
//判断是否demo账号MID
//
//5030230000005731
//
//APP_CUSTOMER_SERVICE_ZALO
//
//首页下方导流参数
//
//[{"appGoogleLink":"https://paay.onelink.me/1lOW/mmlficpn","description":"Préstamos rápidos, desembolso de fondos en su cuenta en cuestión de minutos, seguros y cómodos","appName":"Paay","maxAmount":"20000","logo":"https://s3-mexico-file.s3.us-west-1.amazonaws.com/image/PAAY-logo.png"}]
//
//APP_LOAN_CONTRACT
//
//借款协议;
//
//kualis-contrato.html
//
//APP_REPAYMENT_AGREEMENT
//
//还款协议
//
//kualis-pagare.html
//
//APP_WITHHOLDING_AGREEMENT
//
//代扣协议
//
//kualis-autorización.html
//
//APP_PRIVACY_AGREEMENT
//
//隐私协议
//
//kualis-privacidad.html
//
//APP_REGISTER_AGREEMENT
//
//注册协议
//
//kualis-términosDeUso.html
//
//APP_CUSTOMER_SERVICE_EMAIL
//
//客服邮箱
//
//servicio@kualis.mx
//
//APP_MX_SOCIAL_MS_SWITCH
//
//客服电话跳转链接
//
//http://m.me/1234567890
//
//APP_CUSTOMER_SERVICE_WHATSAPP
//
//客服电话（同whatsapp）
//
//5578163164
//
//APP_UPLOAD_TRACK_EVENT
//
//app上传埋点开关（1/0），业务埋点
//
//1