package com.zip.zipandroid.utils

import java.text.SimpleDateFormat
import java.util.Locale

object ForMateDateUtils {


    fun formatDateToEnglish(inputDate: String): String {
        return try {
            // 1. 定义解析原始字符串的格式
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // 2. 将输入的字符串解析为 Date 对象
            val date = inputFormat.parse(inputDate)
            // 3. 定义目标输出格式 (MMM 表示缩写月份，如 Sep)
            //    注意：使用 Locale.ENGLISH 以确保月份名是英文
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

            // 4. 将 Date 对象格式化为目标字符串
            outputFormat.format(date)
        } catch (e: Exception) {
            // 5. 如果输入格式错误，返回原字符串或进行错误处理
            e.printStackTrace()
            inputDate
        }
    }

}