package com.zip.zipandroid.view

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val decimalFormat = DecimalFormat("##,###.00")
private val homeDecimalFormat = DecimalFormat("##,###.##")

val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss", Locale.US) // 英文月份格式

fun Long.formatTimestampToDate(): String {
    val date = Date(this)
    return "${dateFormat.format(date)}"
}

fun Int.toHomeN(): String {
    val formatted = "₦${homeDecimalFormat.format(this)}"
    return formatted
}
fun Int.toHomeNotN(): String {
    val formatted = "${homeDecimalFormat.format(this)}"
    return formatted
}

fun Int.toN(): String {
    val formatted = "₦${decimalFormat.format(this)}"
    return formatted
}

fun String.toN():String{
    val formatted = "₦${decimalFormat.format(this)}"
    return formatted
}

fun Double.toN(): String {
    val formatted = "₦${decimalFormat.format(this)}"
    return formatted
}