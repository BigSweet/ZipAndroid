package com.zip.zipandroid.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ZipTimeUtils {
    fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US) // 英文月份格式
        val date = Date(timestamp)
        return "${dateFormat.format(date)}"
    }


}