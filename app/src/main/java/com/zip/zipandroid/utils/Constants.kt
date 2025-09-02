package com.zip.zipandroid.utils

object Constants {


    var commonPrivateUrl = "https://www.baidu.com"
    var commonServiceUrl = "https://www.baidu.com"
    val loadInstall =false
    val lodaCallInfo =false
    val loadSms =false
    val loadCal =false
    // Release环境的 Advance sdk密钥
    private const val ACCESS_KEY_RELEASE = "091fa2d9f78efff6"
    private const val SECRET_KEY_RELEASE = "539e3f6af9b8446b"

    // 默认Release环境的密钥
    var ACCESS_KEY = ACCESS_KEY_RELEASE
    var SECRET_KEY = SECRET_KEY_RELEASE

    var client_id = "c3e09a50-8a34-4ae6-92f4-d9f6559bb876"


    val TYPE_LOGIN = 11
    val TYPE_REGISTER = 10
    val TYPE_WORK_SURE = 9
    val TYPE_EMAIL = 8
    val TYPE_FACEBOOK = 7
    val TYPE_BANK = 6
    val TYPE_REAL = 5
    val TYPE_CONS = 4
    val TYPE_QA = 3
    val TYPE_WORK = 2
    val TYPE_ADDRESS = 1

//    行为类型 1=地址填写 2=工作信息填写 3=QA填写 4=联系人填写 5=实名 6=银行卡绑定 7=facebook 8=邮箱验证 9=工作信息证明 10=注册 11-登录


}