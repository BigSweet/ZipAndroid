package com.zip.zipandroid.bean

data class ZipLoginResponse(
    val isRegister: Int,
    val mid: Long,
    val staticKey: String,
    val userNo: String
)