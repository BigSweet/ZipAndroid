package com.zip.zipandroid.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.ParseException

class CurrencyIncomeTextWatcher(private val editText: EditText) : TextWatcher {
    private val decimalFormat = DecimalFormat("#,###")

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)

        try {
            // 1. 移除所有非数字字符（包括逗号和 ₦）
            val originalString = s.toString().replace(Regex("[^\\d]"), "")

            // 2. 限制长度 1-10 位
            val trimmedString = if (originalString.length > 10) {
                originalString.substring(0, 10)
            } else {
                originalString
            }

            if (trimmedString.isNotEmpty()) {
                // 3. 格式化数字（添加千分位逗号）
                val number = trimmedString.toLong()
                val formatted = "₦${decimalFormat.format(number)}"

                // 4. 更新 EditText 显示
                editText.setText(formatted)
                editText.setSelection(formatted.length) // 光标移到末尾
            } else {
                editText.setText("₦") // 保持货币符号
                editText.setSelection(1)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        editText.addTextChangedListener(this)
    }
}