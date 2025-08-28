package com.zip.zipandroid.bean

class CreditListBean : ArrayList<CreditListBeanItem>()

data class CreditListBeanItem(
    val answer: List<String>,
    val questionIndex: String,
    val questionValue: String,
    var questionAnswer: String?,
)