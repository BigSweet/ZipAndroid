package com.zip.zipandroid.bean

data class ZipCodeBean(val code:String) {
}

data class ZipLoginBean(val isRegister: Int, val mid: Long, val staticKey: String, val userNo: String)